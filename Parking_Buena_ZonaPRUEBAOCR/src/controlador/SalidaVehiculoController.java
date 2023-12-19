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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import modelo.Vehiculo;


/**
 * FXML Controller class
 *
 * @author eduva
 */
public class SalidaVehiculoController implements Initializable {
    
    @FXML TextField textMatricula;
    @FXML Button botonImporte;
    @FXML Button botonEscanear;
    @FXML Label lblMatricula;
    @FXML Label lblFechaEntrada;
    @FXML Label lblFechaSalida;
    @FXML Label lblTiempoParking;
    @FXML Label lblImporte;
    @FXML ImageView imgCamaraWeb;
    @FXML GridPane gridPaneInfo;
    
    private Utilidades utilidades = new Utilidades();
    String matricula;
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private CameraManagerQR cameraManager = CameraManagerQR.getInstance();
    private CameraManagerOCR cameraManagerOcr = CameraManagerOCR.getInstance();
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cameraManagerOcr.stopCamera();
        salidaVehiculo();
    } 
    
    @FXML
    private void salidaVehiculo(){
        cameraManager.startCamera(imgCamaraWeb, textMatricula, lblMatricula, gridPaneInfo, botonImporte, lblFechaEntrada, lblFechaSalida, lblTiempoParking, lblImporte);
        
    }
    
    @FXML
    private void pagarImporte(){
        matricula = lblMatricula.getText();
        Vehiculo vehiculo = new Vehiculo();
        
        vehiculo = utilidades.buscarVehiculo(matricula, vehiculo);
        vehiculo = utilidades.calcularImporte(vehiculo);
        vehiculo.mostrarVehiculo();
        utilidades.insertarVehiculoLista(vehiculo);
        utilidades.borrarVehiculoParking(matricula);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Mensaje");
        alert.setContentText("Buen Viaje");
        alert.showAndWait();
        
        textMatricula.setText("");
        lblMatricula.setVisible(false);
        gridPaneInfo.setVisible(false);
        botonImporte.setVisible(false);
        imgCamaraWeb.setVisible(true);
        textMatricula.setVisible(true);
        
        salidaVehiculo();
        
        
    }
    
    
}
    
    
    
    

        
   
    


