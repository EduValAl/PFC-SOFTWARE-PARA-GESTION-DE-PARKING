/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author eduva
 */
public class ParkingLlenoController implements Initializable {
   
    
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

   
    
}

