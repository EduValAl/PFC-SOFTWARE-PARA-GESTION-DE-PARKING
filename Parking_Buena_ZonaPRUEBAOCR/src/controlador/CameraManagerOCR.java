/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

/**
 *
 * @author eduva
 */
import java.awt.image.BufferedImage;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.Vehiculo;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class CameraManagerOCR {
    private static CameraManagerOCR instance;
    private VideoCapture videoCapture;
    private Thread cameraThread;
    private volatile boolean cameraRunning;
    private Utilidades utilidades = new Utilidades();
    private Vehiculo vehiculo = new Vehiculo();
    private boolean cocheDentro;
    
    

    private CameraManagerOCR() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        videoCapture = new VideoCapture();
        
    }

    public static CameraManagerOCR getInstance() {
        if (instance == null) {
            instance = new CameraManagerOCR();
        }
        return instance;
    }

    public void startCamera(ImageView imgCamaraWeb, TextField textMatricula, Button botonEntrada, ImageView imgOk, ImageView imgError) {
        
        cameraRunning = true;
        videoCapture.open(0); // Abre la cámara, 0 representa la cámara por defecto
        
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:\\Users\\eduva\\Documents\\NetBeansProjects\\Parking_Buena_ZonaPRUEBAOCR\\tessdata");

        cameraThread = new Thread(() -> {
            while (cameraRunning) {
                // Lógica para manejar la cámara dentro del bucle del hilo
                // Por ejemplo:
                // Captura de fotogramas, procesamiento, etc.
                Mat frame = new Mat();
                    if (videoCapture.read(frame)) {
                        Image imageToShow = utilidades.mat2Image(frame);
                        utilidades.updateImageView(imgCamaraWeb, imageToShow);
                        
                        // Convertir la imagen a BufferedImage
                        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageToShow, null);
                        
                        try {
                            String result = tesseract.doOCR(bufferedImage);
                            String resultado = result.replaceAll("\\s+", "");
                            System.out.println("Texto reconocido: " + resultado);
                            // Aquí puedes hacer lo que quieras con el texto reconocido
                             
                            
                            if(utilidades.validarMatricula(resultado)){
                                textMatricula.setText(resultado);
                                
                                Platform.runLater(() -> {
                                    botonEntrada.setDisable(false);
                                    imgOk.setVisible(true);
                                    imgError.setVisible(false);
                                    
                                });
                               
                            
                            }
                        } catch (TesseractException e) {
                            System.err.println("Error al realizar OCR: " + e.getMessage());
                        }
                       
                        
                        
                    }
            }
        });
        cameraThread.start();
    }

    public void stopCamera() {
        cameraRunning = false;
        if (cameraThread != null) {
            try {
                cameraThread.join(); // Espera a que el hilo termine
            } catch (InterruptedException e) {

            }
        }
        if (videoCapture.isOpened()) {
            videoCapture.release(); // Libera la cámara
        }
    }
}

