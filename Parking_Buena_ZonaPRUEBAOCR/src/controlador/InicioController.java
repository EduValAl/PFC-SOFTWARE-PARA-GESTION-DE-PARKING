/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author eduva
 */
public class InicioController implements Initializable {
    @FXML Button botonLogin;
    @FXML TextField textUsuario;
    @FXML TextField textPass;
    
    Utilidades utilidades = new Utilidades();
    boolean userOk;
    boolean parkingLleno;
    
     private CameraManagerQR cameraManagerQr = CameraManagerQR.getInstance();
     private CameraManagerOCR cameraManagerOcr = CameraManagerOCR.getInstance();
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
          
        cameraManagerQr.stopCamera();
        cameraManagerOcr.stopCamera();
    }   
    
    @FXML
    public void abrirVentanaAdmin(ActionEvent event){
        String comprobarUser = textUsuario.getText();
        String comprobarPass = textPass.getText();
        
        userOk = utilidades.verificarAdministrador(comprobarUser, comprobarPass);
        
        if(userOk == true){
                   
                try {
                
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Admin.fxml"));
                    Parent root = loader.load();
                    AdminController controlador = loader.getController();
                    
                    controlador.textAdmin.setText("HOLA, "+comprobarUser);

                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    
                    stage.setTitle("SOFTWARE GESTIÓN PARKING BUENAZONA");
                    stage.setMaximized(true);
                    stage.setScene(scene);
                    
                    Image icon = new Image(getClass().getResourceAsStream("/imagenes/parking.jpg"));
                    stage.getIcons().add(icon);
                    stage.show();

                    stage.setOnCloseRequest(e -> controlador.salir());
                    Stage myStage = (Stage) this.botonLogin.getScene().getWindow();
                    myStage.close();
                       
                } catch (IOException ex) {
                    Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
                }   
              
        }else if(userOk == false){
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Mensaje");
            alert.setContentText("Usuario o contraseña incorrecta");
            alert.showAndWait();
    
        }
        
        
    }
    
    
}
