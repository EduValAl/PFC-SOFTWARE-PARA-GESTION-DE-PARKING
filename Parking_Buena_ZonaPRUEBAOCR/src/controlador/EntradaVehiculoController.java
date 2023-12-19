/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;


import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import modelo.Vehiculo;

/**
 * FXML Controller class
 *
 * @author eduva
 */
public class EntradaVehiculoController implements Initializable {
    @FXML TextField textMatricula;
    @FXML Button botonEntrada;
    @FXML ImageView imgError;
    @FXML ImageView imgOk;
    @FXML ImageView imgCamaraOcr;
    @FXML ImageView imgParkingLleno;
    @FXML Label lblParkingLleno;
    
    boolean matriculaOk;
    boolean cocheDentroOk;
    boolean parkingLlenoOk;
    String rutaCodigoQR = "";
    
    AdminController controlador = new AdminController();
    Utilidades utilidades = new Utilidades();
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private CameraManagerQR cameraManagerQr = CameraManagerQR.getInstance();
    private CameraManagerOCR cameraManagerOcr = CameraManagerOCR.getInstance();
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        cameraManagerQr.stopCamera();
        entradaVehiculo();
        
        if (utilidades.validarMatricula(textMatricula.getText())) {
            botonEntrada.setDisable(false);
            imgOk.setVisible(true);
            imgError.setVisible(false);

        } else {
            botonEntrada.setDisable(true);
            imgError.setVisible(true);
            imgOk.setVisible(false);
        }
        
    }
    
    @FXML
    private void entradaVehiculo(){
        cameraManagerOcr.startCamera(imgCamaraOcr, textMatricula, botonEntrada, imgOk, imgError);
    }
    
    
    
    @FXML
    private void vehiculoDentro(){
        
        String matricula = textMatricula.getText();
        String fecha_entrada = sdf.format(date);
        Vehiculo vehiculo = new Vehiculo(matricula, fecha_entrada);

        String rutaPdf = "C:\\Users\\eduva\\Documents\\NetBeansProjects\\Parking_Buena_ZonaPRUEBAOCR\\src\\tickets_pdfs\\"+matricula+".pdf";
        String fuente = "TYR.ttf";
 
        cocheDentroOk = utilidades.insertarVehiculoParking(vehiculo);
        
        if (cocheDentroOk == true) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Mensaje");
            alert.setContentText("Recoja su ticket");
            alert.showAndWait();
            textMatricula.setText("");
            botonEntrada.setDisable(true);
            imgOk.setVisible(false);
            imgError.setVisible(true);
            rutaCodigoQR = "C:\\Users\\eduva\\Documents\\NetBeansProjects\\Parking_Buena_ZonaPRUEBAOCR\\src\\codigosQR\\"+matricula+".png";
            utilidades.generarQR(matricula, rutaCodigoQR);
            utilidades.generarEImprimirPdf(rutaPdf, fuente, rutaCodigoQR, matricula, vehiculo.getFecha_entrada());
            parkingLlenoOk = utilidades.parkingLleno();

            if (parkingLlenoOk == true) {
                
                cameraManagerOcr.stopCamera();
                imgCamaraOcr.setVisible(false);
                imgParkingLleno.setVisible(true);
                lblParkingLleno.setVisible(true);
                textMatricula.setVisible(false);
                botonEntrada.setVisible(false);
                imgError.setVisible(false);
                imgOk.setVisible(false);

            }
            

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Mensaje");
            alert.setContentText("La matricula no es valida o ha ocurrido un error en el sistema");
            alert.showAndWait();
            textMatricula.setText("");

        }

    }
    
    @FXML
    private void formatoMatricula(){
        
            
           textMatricula.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,4}[A-Z]{0,3}")) {
                textMatricula.setText(oldValue);

            }
        });
           
          
        
        textMatricula.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (utilidades.validarMatricula(newValue)) {
                    botonEntrada.setDisable(false);
                    imgOk.setVisible(true);
                    imgError.setVisible(false);

                } else {
                    botonEntrada.setDisable(true);
                    imgError.setVisible(true);
                    imgOk.setVisible(false);
                }

            }
        });
        
    }
    
}
