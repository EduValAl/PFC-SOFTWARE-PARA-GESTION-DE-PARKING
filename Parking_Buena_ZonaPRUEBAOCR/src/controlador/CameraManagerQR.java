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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import modelo.Vehiculo;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class CameraManagerQR {
    private static CameraManagerQR instance;
    private VideoCapture videoCapture;
    private Thread cameraThread;
    private volatile boolean cameraRunning;
    private Utilidades utilidades = new Utilidades();
    private Vehiculo vehiculo = new Vehiculo();
    private boolean cocheDentro;

    private CameraManagerQR() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        videoCapture = new VideoCapture();
    }

    public static CameraManagerQR getInstance() {
        if (instance == null) {
            instance = new CameraManagerQR();
        }
        return instance;
    }

    public void startCamera(ImageView imgCamaraWeb, TextField textMatricula, Label lblMatricula, GridPane gridPaneInfo, Button botonImporte, Label lblFechaEntrada, Label lblFechaSalida, Label lblTiempoParking, Label lblImporte) {
        cameraRunning = true;
        videoCapture.open(0); // Abre la cámara, 0 representa la cámara por defecto

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

                        // Leer el código QR
                        String qrText = utilidades.readQRCode(bufferedImage);
                        
                        if (qrText != null) {
                            System.out.println("Código QR detectado: " + qrText);
                            // Aquí puedes realizar acciones con el código QR leído
                            
                            textMatricula.setText(qrText);
                            
                            cocheDentro = utilidades.buscarVehiculoDentro(qrText, vehiculo);
                            
                            
                            Platform.runLater(() -> {

                                videoCapture.release();
                                imgCamaraWeb.setVisible(false);
                                textMatricula.setVisible(false);
                                lblMatricula.setVisible(true);
                                gridPaneInfo.setVisible(true);
                                botonImporte.setVisible(true);

                                vehiculo = utilidades.buscarVehiculo(textMatricula.getText(), vehiculo);
                                utilidades.calcularImporte(vehiculo);

                                lblMatricula.setText(vehiculo.getMatricula());
                                lblFechaEntrada.setText(vehiculo.getFecha_entrada());
                                lblFechaSalida.setText(vehiculo.getFecha_salida());
                                lblTiempoParking.setText(vehiculo.getTiempo_parking());
                                lblImporte.setText(String.format("%.2f", vehiculo.getImporte()));

                            });
                                
                           
                            
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

