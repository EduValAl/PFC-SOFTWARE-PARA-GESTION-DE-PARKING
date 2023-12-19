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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author eduva
 */
public class AdminController implements Initializable {
    @FXML Button botonSalir;
    @FXML Label textAdmin;
    @FXML BorderPane bp;
    
    boolean parkingLlenoOk;
    Utilidades utilidades = new Utilidades();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }

    @FXML
    public void salir (){
       try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Inicio.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setTitle("SOFTWARE GESTIÓN PARKING BUENAZONA");
            stage.setMaximized(true);
            stage.setScene(scene);
            Image icon = new Image(getClass().getResourceAsStream("/imagenes/parking.jpg"));
            stage.getIcons().add(icon);
            stage.show();
            
            
            Stage myStage = (Stage) this.botonSalir.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @FXML
    public void entradaVehiculo(MouseEvent event) {
        
        
        parkingLlenoOk = utilidades.parkingLleno();
        
        if(parkingLlenoOk == true){
            
            loadPage("/vista/ParkingLleno");
            
        }else{
            
            loadPage("/vista/EntradaVehiculo");
        }
        
    }

    @FXML
    private void salidaVehiculo(MouseEvent event) {
        
        loadPage("/vista/SalidaVehiculo");
    }

    @FXML
    private void listarVehiculos(MouseEvent event) {
        
        loadPage("/vista/ListarVehiculo");
    }
    
    @FXML
    private void contabilidad(MouseEvent event) {
        
        loadPage("/vista/Contabilidad");
    }
    
    public void loadPage(String page){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(page+".fxml"));
            
            
        } catch (IOException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        bp.setCenter(root);
    
    }
}

