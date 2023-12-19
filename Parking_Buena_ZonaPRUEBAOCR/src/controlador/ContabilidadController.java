/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;


import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import modelo.DatosCochesDia;
import modelo.DatosDineroDia;


/**
 * FXML Controller class
 *
 * @author eduva
 */
public class ContabilidadController implements Initializable {
    @FXML
    private Label lblDe;
    @FXML
    private Label lblHasta;
    @FXML
    private BarChart<String, Number> barChartCochesDia;
    @FXML
    private BarChart<String, Number> barChartDineroDia;
    @FXML
    private DatePicker datePickerInicio;
    @FXML
    private DatePicker datePickerFin;
    @FXML
    private Button botonCochesDia;
    
    private Utilidades utilidades = new Utilidades();
    private CameraManagerQR cameraManagerQr = CameraManagerQR.getInstance();
    private CameraManagerOCR cameraManagerOcr = CameraManagerOCR.getInstance();
    
    String fechaInicio;
    String fechaFin;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       cameraManagerQr.stopCamera();
       cameraManagerOcr.stopCamera();
    }    
    
    
    public void graficaCochesDia(){
        datePickerInicio.setValue(LocalDate.now());
        datePickerFin.setValue(LocalDate.now());
        //Visualizo FXML
        lblDe.setVisible(true);
        lblHasta.setVisible(true);
        datePickerInicio.setVisible(true);
        datePickerFin.setVisible(true);
        barChartCochesDia.setVisible(true);
        barChartDineroDia.setVisible(false);
        
        
        datePickerInicio.setOnAction(event -> {
           dibujarGraficaCochesDia();
        });

        datePickerFin.setOnAction(event -> {
            dibujarGraficaCochesDia();
           
        });
         
    }
    
    public void graficaDineroDia(){
        datePickerInicio.setValue(LocalDate.now());
        datePickerFin.setValue(LocalDate.now());
        //Visualizo FXML
        lblDe.setVisible(true);
        lblHasta.setVisible(true);
        datePickerInicio.setVisible(true);
        datePickerFin.setVisible(true);
        barChartDineroDia.setVisible(true);
        barChartCochesDia.setVisible(false);
        
        
        datePickerInicio.setOnAction(event -> {
           dibujarGraficaDineroDia();
        });

        datePickerFin.setOnAction(event -> {
            dibujarGraficaDineroDia();
           
        });
    }
    
    public void dibujarGraficaCochesDia(){
        
        barChartCochesDia.getData().clear();
        
        
        if(datePickerInicio.getValue().isAfter(datePickerFin.getValue())){
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Mensaje");
            alert.setContentText("La fecha de Inicio no puede ser mayor que la fecha final");
            alert.showAndWait();
            
        
        }else{
            
            fechaInicio = datePickerInicio.getValue().toString();
            fechaFin = datePickerFin.getValue().toString();

            // Obtener los datos utilizando el método obtenerDatosEntreFechas
            ObservableList<DatosCochesDia> datos = utilidades.mostrarGraficaCochesDia(fechaInicio, fechaFin);

            // Crear el eje X e Y
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();

            // Configurar los ejes del gráfico
            xAxis.setLabel("Fecha");
            yAxis.setLabel("Cantidad");

            // Crear la serie para el gráfico de barras
            XYChart.Series<String, Number> series = new XYChart.Series<>();

            series.setName("Coches");

            // Agregar datos al gráfico de barras
            for (DatosCochesDia dato : datos) {
                series.getData().add(new XYChart.Data<>(dato.getFecha(), dato.getNumeroCoches()));
            }

            // Agregar la serie al gráfico de barras
            
            barChartCochesDia.getData().add(series);

            datos.removeAll(datos);
            
        }
     
    }
    
    public void dibujarGraficaDineroDia(){
        
        barChartDineroDia.getData().clear();
        
        if(datePickerInicio.getValue().isAfter(datePickerFin.getValue())){
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Mensaje");
            alert.setContentText("La fecha de Inicio no puede ser mayor que la fecha final");
            alert.showAndWait();
            
        }else{
            
            fechaInicio = datePickerInicio.getValue().toString();
            fechaFin = datePickerFin.getValue().toString();

            // Obtener los datos utilizando el método obtenerDatosEntreFechas
            ObservableList<DatosDineroDia> datos = utilidades.mostrarGraficaDineroDia(fechaInicio, fechaFin);

            // Crear el eje X e Y
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();

            // Configurar los ejes del gráfico
            xAxis.setLabel("Fecha");
            yAxis.setLabel("Cantidad");

            // Crear la serie para el gráfico de barras
            XYChart.Series<String, Number> series = new XYChart.Series<>();

            series.setName("Euros");

            // Agregar datos al gráfico de barras
            for (DatosDineroDia dato : datos) {
                series.getData().add(new XYChart.Data<>(dato.getFecha(), dato.getTotal()));
            }

            // Agregar la serie al gráfico de barras
           
            barChartDineroDia.getData().add(series);

            datos.removeAll(datos);
            
        }
    
    }
}
        
       
      
        
        
        
    
    

