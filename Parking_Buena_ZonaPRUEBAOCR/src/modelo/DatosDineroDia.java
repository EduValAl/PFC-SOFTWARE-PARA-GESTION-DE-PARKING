/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author eduva
 */
public class DatosDineroDia {
    private String fecha;
    private double total;

    public DatosDineroDia() {
    }

    public DatosDineroDia(String fecha, double total) {
        this.fecha = fecha;
        this.total = total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    public void mostrarDatosDineroDia(){
        System.out.println("Fecha: "+fecha);
        System.out.println("Total: "+total);
    }
}
