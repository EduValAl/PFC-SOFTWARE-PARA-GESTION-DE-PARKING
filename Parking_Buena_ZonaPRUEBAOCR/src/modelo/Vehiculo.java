/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author eduva
 */
public class Vehiculo {
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private String matricula;
    private String fecha_entrada = sdf.format(date);
    private String fecha_salida = sdf.format(date);
    private String tiempo_parking;
    private double importe = 0.0;

    public Vehiculo() {
        
    }
    
    public Vehiculo(String matricula, String fecha_entrada){
        this.matricula = matricula;
        this.fecha_entrada = fecha_entrada;
        
    }

    public Vehiculo(String matricula, String fecha_entrada, String fecha_salida, String tiempo_parking, double importe) {
        this.matricula = matricula;
        this.fecha_entrada = fecha_entrada;
        this.fecha_salida = fecha_salida;
        this.tiempo_parking = tiempo_parking;
        this.importe = importe;

    }
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getFecha_entrada() {
        return fecha_entrada;
    }

    public void setFecha_entrada(String fecha_entrada) {
        this.fecha_entrada = fecha_entrada;
    }

    public String getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(String fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    public String getTiempo_parking() {
        return tiempo_parking;
    }

    public void setTiempo_parking(String tiempo_parking) {
        this.tiempo_parking = tiempo_parking;
    }
    
    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }
    
    public void mostrarVehiculo(){
        System.out.println("Matricula: "+matricula);
        System.out.println("Fecha Entrada;"+fecha_entrada);
        System.out.println("Fecha Salida:"+fecha_salida);
        System.out.println("Tiemp Parking: "+tiempo_parking);
        System.out.println("Importe: "+importe);
    }

}

