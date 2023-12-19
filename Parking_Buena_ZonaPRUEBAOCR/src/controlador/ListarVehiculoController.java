/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Vehiculo;


/**
 * FXML Controller class
 *
 * @author eduva
 */
public class ListarVehiculoController implements Initializable {
    @FXML
    Button botonListaSalidas;
    @FXML
    Button botonListaEntradas;
    @FXML
    Button botonListaEntradasSalidas;
    
    
    
    @FXML
    private TableView<Vehiculo> tabVehiculos;
    @FXML
    private TableColumn <Vehiculo, String>colMatricula;
    @FXML
    private TableColumn <Vehiculo, String>colFechaEntrada;
    @FXML
    private TableColumn <Vehiculo, String>colFechaSalida;
    @FXML
    private TableColumn <Vehiculo, String>colTiempoParking;
    @FXML
    private TableColumn <Vehiculo, Double>colImporte;
    
    private ObservableList<Vehiculo> vehiculosEntrada = FXCollections.observableArrayList();
    private ObservableList<Vehiculo> vehiculosSalida = FXCollections.observableArrayList();
    private ObservableList<Vehiculo> vehiculosEntradasSalidas = FXCollections.observableArrayList();
    
    
    private Utilidades utilidades = new Utilidades();
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
    private void listarSalidas(){
        tabVehiculos.setVisible(true);
        this.colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        this.colFechaEntrada.setCellValueFactory(new PropertyValueFactory("fecha_entrada"));
        this.colFechaSalida.setCellValueFactory(new PropertyValueFactory("fecha_salida"));
        this.colTiempoParking.setCellValueFactory(new PropertyValueFactory("tiempo_parking"));
        this.colImporte.setCellValueFactory(new PropertyValueFactory("importe"));
        
        vehiculosSalida = utilidades.mostrarVehiculosLista();
        this.tabVehiculos.setItems(vehiculosSalida);
       
    }
    
    @FXML
    private void listarEntradas(){
        tabVehiculos.setVisible(true);
        this.colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        this.colFechaEntrada.setCellValueFactory(new PropertyValueFactory("fecha_entrada"));
        this.colImporte.setCellValueFactory(new PropertyValueFactory("importe"));
        
        vehiculosEntrada = utilidades.mostrarVehiculosDentro();
        this.tabVehiculos.setItems(vehiculosEntrada);
        
    
    
    }
    
    @FXML
    private void listarEntradasSalidas(){
        tabVehiculos.setVisible(true);
        vehiculosEntradasSalidas.removeAll(vehiculosEntradasSalidas);
        
        this.colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        this.colFechaEntrada.setCellValueFactory(new PropertyValueFactory("fecha_entrada"));
        this.colFechaSalida.setCellValueFactory(new PropertyValueFactory("fecha_salida"));
        this.colTiempoParking.setCellValueFactory(new PropertyValueFactory("tiempo_parking"));
        this.colImporte.setCellValueFactory(new PropertyValueFactory("importe"));
        
        vehiculosEntrada = utilidades.mostrarVehiculosDentro();
        vehiculosSalida = utilidades.mostrarVehiculosLista();
        
        for(Vehiculo vehiculo : vehiculosEntrada){
            vehiculosEntradasSalidas.add(vehiculo);
        
        }
        
        for(Vehiculo vehiculo : vehiculosSalida){
            vehiculosEntradasSalidas.add(vehiculo);
        
        }
        
        this.tabVehiculos.setItems(vehiculosEntradasSalidas);
        
    }
  
}
