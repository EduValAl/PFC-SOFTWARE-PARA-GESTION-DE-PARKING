/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author eduva
 */
public class Main extends Application {
    
    
    @Override
    public void start(Stage primaryStage) {
        
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Inicio.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("SOFTWARE GESTIÓN PARKING BUENAZONA");
            primaryStage.setMaximized(true);
            Image icon = new Image(getClass().getResourceAsStream("/imagenes/parking.jpg"));
            primaryStage.getIcons().add(icon);
            primaryStage.show();
            
        }catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }             

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
