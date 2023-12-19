/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import modelo.DatosCochesDia;
import modelo.DatosDineroDia;
import modelo.Vehiculo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.printing.PDFPageable;
import org.opencv.core.Mat;

/**
 *
 * @author eduva
 */
public class Utilidades {
    private Connection conexion;
    private boolean parkingLleno;
    private boolean usuarioOk = false;
    private boolean cocheDentro;
    private boolean buscarVehiculoDentro = false;
    private final int numeroPlazas = 10;
    private int contadorPlazas = 0;
    private DatosCochesDia datosCochesDia;
   
   
    
    
    public void conectar() {

	String url = "jdbc:mysql://127.0.0.1:3306/parking_buenazona";
	System.out.println(url);

        try {
            conexion = DriverManager.getConnection(url, "root", "1234");
        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        	
        }
        System.out.println("Conexión establecida correctamente");

    }
    
    public void desconectar() {
        try {
            conexion.close();
            System.out.println("Fin de la conexión");
        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        	
        }
    }
    
    public boolean verificarAdministrador(String comprobarUser, String comprobarPass){
        try {
            conectar();
            String consulta = "SELECT * FROM ADMINISTRADORES WHERE USUARIO = ? AND CONTRASEÑA = ? ";
            PreparedStatement instruccion = conexion.prepareStatement(consulta);
            
            instruccion.setString(1, comprobarUser);
            instruccion.setString(2, comprobarPass);
            ResultSet registros = instruccion.executeQuery();
            
            while(registros.next()){
                usuarioOk = true;
                       
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            desconectar();
        }
        
        return usuarioOk;
        
        
    }
    
    public boolean parkingLleno(){
       
        conectar();
        String consulta = "SELECT * FROM VEHICULOS_DENTRO";
        contadorPlazas = 0;
        
        try {
            Statement instruccion = conexion.createStatement();
            ResultSet registros = instruccion.executeQuery(consulta);

            while(registros.next()) {
                contadorPlazas++;
            }
            
            parkingLleno = contadorPlazas >= numeroPlazas;

        } catch (SQLException e) {
            
        }finally {
                desconectar();
        }
        return parkingLleno;
        
    }
    
    public boolean validarMatricula(String matricula){
        return matricula.matches("[0-9]{4}[A-Z]{3}");
    }
    
    public boolean insertarVehiculoParking(Vehiculo vehiculo){
        
        try {
            conectar();
            
            String consulta = "INSERT INTO VEHICULOS_DENTRO VALUES (?,?)";
            PreparedStatement instruccion = conexion.prepareStatement(consulta);
            instruccion.setString(1, vehiculo.getMatricula());
            instruccion.setString(2, vehiculo.getFecha_entrada());
            instruccion.executeUpdate();
            cocheDentro = true;
            
        } catch (SQLException ex) {
            cocheDentro = false;
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            desconectar();
        }
        
        return cocheDentro;
        
    }
    
    public void generarQR(String datosQR, String rutaCodigoQR){
        
        try {
            // Crear un mapa de hints para configurar el formato del código QR
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Crear un objeto QRCodeWriter
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            // Generar una matriz de bits para el código QR
            BitMatrix bitMatrix = qrCodeWriter.encode(datosQR, BarcodeFormat.QR_CODE, 100, 100, hints);

            // Crear una imagen en blanco del mismo tamaño que la matriz de bits
            BufferedImage image = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(), BufferedImage.TYPE_INT_RGB);

            // Rellenar la imagen con los datos del código QR
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                for (int y = 0; y < bitMatrix.getHeight(); y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            // Guardar la imagen como un archivo PNG
            ImageIO.write(image, "png", new File(rutaCodigoQR));

            System.out.println("Código QR generado y guardado exitosamente en: " + rutaCodigoQR);
        } catch (WriterException | IOException e) {
            
        }
    }
    
    public void generarEImprimirPdf(String ruta, String fuente, String rutaCodigoQR, String textoMatricula, String hora){
        
        try {
            // Crear un nuevo documento PDF
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(new PDRectangle(165,180));
            document.addPage(page);
            
            PDType0Font customFont = PDType0Font.load(document, new File(fuente));
            PDImageXObject image = PDImageXObject.createFromFile(rutaCodigoQR, document);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            

            // Añadir la imagen a la página
            contentStream.drawImage(image, 15,90); // Ajusta las coordenadas según sea necesario

            // Añadir texto a la página -Matricula-
            contentStream.setFont(customFont, 20);
            contentStream.beginText();
            contentStream.newLineAtOffset(20, 75); // Ajusta las coordenadas según sea necesario
            contentStream.showText(textoMatricula);
            contentStream.endText();
            
            // Añadir texto a la página -Hora-
            contentStream.setFont(customFont, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(20, 60); // Ajusta las coordenadas según sea necesario
            contentStream.showText(hora);
            contentStream.endText();
            
            // Añadir texto a la página -Mensaje-
            contentStream.setFont(customFont, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(20, 45); // Ajusta las coordenadas según sea necesario
          
             
            contentStream.showText("Gracias por utilizar");
            contentStream.endText();
            
            //Añadir texto Parking Buenazona
            contentStream.setFont(customFont, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(20, 30); // Ajusta las coordenadas según sea necesario
            contentStream.showText("PARKING BUENAZONA");
            contentStream.endText();
           
            contentStream.close();

            // Guardar el documento PDF
            document.save(ruta);
            
            System.out.println("El PDF se ha creado correctamente.");
            
            // Imprimir el documento PDF
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

            if (printServices.length > 0) {
                PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();

                PrinterJob printerJob = PrinterJob.getPrinterJob();
                printerJob.setPrintService(defaultPrintService);

                PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
                attributeSet.add(new Copies(1));

                // Imprimir el documento PDF recién creado
               
                printerJob.setPageable(new PDFPageable(document));
                printerJob.print(attributeSet);
                document.close();
                System.out.println("El PDF se está imprimiendo...");
            } else {
                document.close();
                System.out.println("No se encontró ninguna impresora.");
            }
        } catch (IOException | PrinterException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Vehiculo buscarVehiculo(String matricula, Vehiculo vehiculo){
        
        //Busco vehiculo de la tabla vehiculos dentro
        conectar();

        String consulta = "SELECT * FROM VEHICULOS_DENTRO WHERE MATRICULA = ?";
        PreparedStatement instruccion;

        try {
            instruccion = conexion.prepareStatement(consulta);
            instruccion.setString(1, matricula);
            ResultSet registros = instruccion.executeQuery();

            while (registros.next()) {

                String matricula2 = registros.getString("MATRICULA");
                String fecha_entrada = registros.getString("FECHA_ENTRADA");
                vehiculo = new Vehiculo(matricula2, fecha_entrada);

            }

        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            desconectar();
        }
       
        return vehiculo;

    }
    
    public boolean buscarVehiculoDentro(String matricula, Vehiculo vehiculo){
        
        //Busco vehiculo de la tabla vehiculos dentro
        conectar();

        String consulta = "SELECT * FROM VEHICULOS_DENTRO WHERE MATRICULA = ?";
        PreparedStatement instruccion;

        try {
            instruccion = conexion.prepareStatement(consulta);
            instruccion.setString(1, matricula);
            ResultSet registros = instruccion.executeQuery();

            while (registros.next()) {

                String matricula2 = registros.getString("MATRICULA");
                String fecha_entrada = registros.getString("FECHA_ENTRADA");
                vehiculo = new Vehiculo(matricula2, fecha_entrada);
                
                if(matricula.equalsIgnoreCase(matricula2)){
                    buscarVehiculoDentro = true;
                
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            desconectar();
        }
       
        return buscarVehiculoDentro;

    }
    
    
    
    public void insertarVehiculoLista(Vehiculo vehiculo){
        
        try {
            conectar();

            String consultaInsertar = "INSERT INTO LISTA_VEHICULOS VALUES (?,?,?,?,?)";
            PreparedStatement instruccionInsertar = conexion.prepareStatement(consultaInsertar);
            instruccionInsertar.setString(1, vehiculo.getMatricula());
            instruccionInsertar.setString(2, vehiculo.getFecha_entrada());
            instruccionInsertar.setString(3, vehiculo.getFecha_salida());
            instruccionInsertar.setString(4, vehiculo.getTiempo_parking());
            instruccionInsertar.setDouble(5, vehiculo.getImporte());

            instruccionInsertar.executeUpdate();
            

        } catch (SQLException ex) {
            
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            desconectar();
        }
    
    }
    
    public void borrarVehiculoParking(String matricula){
        conectar();

        String consultaBorrar = "DELETE FROM VEHICULOS_DENTRO WHERE MATRICULA = ?";
        PreparedStatement instruccionBorrar;

        try {
            instruccionBorrar = conexion.prepareStatement(consultaBorrar);
            instruccionBorrar.setString(1, matricula);
            instruccionBorrar.executeUpdate();
            System.out.println("Vehiculo eliminado satisfactoriamente");

        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            desconectar();
        }

    }
    
    public Vehiculo calcularImporte(Vehiculo vehiculo){
        
        
        try {
            String fecha_entrada = vehiculo.getFecha_entrada();
            String fecha_salida = vehiculo.getFecha_salida();
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            Date date = sdf.parse(fecha_entrada);
            Date date2 = sdf.parse(fecha_salida);
            
            long fechaEntrada = date.getTime();
            long fechaSalida = date2.getTime();
            
            long diferencia = (fechaSalida - fechaEntrada) / 1000;
            
            //Calculo tiempo_parking
            
            long horas = diferencia / 3600;
            long minutos = (diferencia % 3600) / 60;
            long segundos = diferencia % 60;

          
            // Paso a String
            //String diasParking = String.valueOf(dias);
            String horasParking = String.valueOf(horas);
            String minutosParking = String.valueOf(minutos);
            String segundosParking = String.valueOf(segundos);
            
            vehiculo.setTiempo_parking(horasParking+" horas "+minutosParking+" minutos "+segundosParking+" segundos");
            
            //Calculo importe
            double tiempo  = (double) diferencia;
            tiempo= diferencia/60;
            double importeFinal = tiempo * 0.05;
            
            vehiculo.setImporte(importeFinal);
            
            
        } catch (ParseException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return vehiculo;
            
    }
    
    public ObservableList<Vehiculo> mostrarVehiculosLista(){
        ObservableList<Vehiculo> obs = FXCollections.observableArrayList();
        conectar();
        String consulta = "SELECT * FROM LISTA_VEHICULOS";
        try {
            Statement instruccion = conexion.createStatement();
            ResultSet registros = instruccion.executeQuery(consulta);
            
            while (registros.next()) {
                String matricula = registros.getString("MATRICULA");
                String fecha_entrada = registros.getString("FECHA_ENTRADA");
                String fecha_salida = registros.getString("FECHA_SALIDA");
                String tiempo_parking = registros.getString("TIEMPO_PARKING");
                double importe = registros.getDouble("IMPORTE");
                Vehiculo vehiculo = new Vehiculo(matricula, fecha_entrada, fecha_salida, tiempo_parking, importe);
                obs.add(vehiculo);
       
            }
            

        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            desconectar();
        }
        
        return obs;
    }
    
    public ObservableList<Vehiculo> mostrarVehiculosDentro(){
        ObservableList<Vehiculo> obs = FXCollections.observableArrayList();
        conectar();
        String consulta = "SELECT * FROM VEHICULOS_DENTRO";
        try {
            Statement instruccion = conexion.createStatement();
            ResultSet registros = instruccion.executeQuery(consulta);
            
            while (registros.next()) {
                String matricula = registros.getString("MATRICULA");
                String fecha_entrada = registros.getString("FECHA_ENTRADA");
                
                Vehiculo vehiculo = new Vehiculo(matricula, fecha_entrada, null, null, 0.0);
                obs.add(vehiculo);
       
            }
            

        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            desconectar();
        }
        
        return obs;
        
        
    }
    
    //Metodos leer codigoQR desde camara Web
    
    public void updateImageView(ImageView view, Image image) {
        if (view != null && image != null) {
            view.setImage(image);
        }
    }

    public Image mat2Image(Mat frame) {
        try {
            BufferedImage image = new BufferedImage(frame.width(), frame.height(), BufferedImage.TYPE_3BYTE_BGR);
            byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            frame.get(0, 0, data);
            return SwingFXUtils.toFXImage(image, null);
        } catch (Exception e) {
            System.err.println("Error al convertir el frame a imagen: " + e);
            return null;
        }
    }
    
    public String readQRCode(BufferedImage image) {
        try {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            Result result = new MultiFormatReader().decode(binaryBitmap);
            return result.getText();
        } catch (NotFoundException e) {
            System.err.println("Código QR no encontrado: " + e);
            return null;
        }
    }
    
    public ObservableList<DatosCochesDia>  mostrarGraficaCochesDia(String fechaInicio, String fechaFin){
        ObservableList<DatosCochesDia> datos = FXCollections.observableArrayList();
       
        conectar();
        
        String consulta = "SELECT DATE(fecha_entrada) as fecha, COUNT(*) AS cantidad_registros \n"
                + "FROM lista_vehiculos \n"
                + "WHERE fecha_entrada BETWEEN ? AND ? \n"
                + "GROUP BY DATE(fecha_entrada)";
        
        try {
            PreparedStatement instruccion = conexion.prepareStatement(consulta);
            
            instruccion.setString(1, fechaInicio);
            instruccion.setString(2, fechaFin);
            
            ResultSet registros = instruccion.executeQuery();
            
            while (registros.next()) {
                
                String fecha_entrada = registros.getString("fecha");
                int cantidad = registros.getInt("cantidad_registros");
                
               
                DatosCochesDia dato = new DatosCochesDia(fecha_entrada, cantidad);
                datos.add(dato);
                
                
                System.out.println("fecha entrada: "+fecha_entrada+" cantidad: "+cantidad);
       
            }
            

        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            desconectar();
        }
        
       
        
        return datos;
    
    }
    
    public ObservableList<DatosDineroDia>  mostrarGraficaDineroDia(String fechaInicio, String fechaFin){
        ObservableList<DatosDineroDia> datos = FXCollections.observableArrayList();
       
        conectar();
        String consulta = "SELECT DATE(fecha_entrada) AS fecha, SUM(importe) AS total\n"
                + "FROM lista_vehiculos\n"
                + "WHERE fecha_entrada BETWEEN ? AND ? \n"
                + "GROUP BY DATE(fecha_entrada)";
        
        
        try {
            PreparedStatement instruccion = conexion.prepareStatement(consulta);
            
            instruccion.setString(1, fechaInicio);
            instruccion.setString(2, fechaFin);
            
            ResultSet registros = instruccion.executeQuery();
            
            while (registros.next()) {
                
                String fecha_entrada = registros.getString("fecha");
                double total = registros.getDouble("total");
                
               
                DatosDineroDia dato = new DatosDineroDia(fecha_entrada, total);
                datos.add(dato);
                
                
                System.out.println("fecha entrada: "+fecha_entrada+" cantidad: "+total);
       
            }
            

        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            desconectar();
        }
        
        return datos;
    
    }
     
}

