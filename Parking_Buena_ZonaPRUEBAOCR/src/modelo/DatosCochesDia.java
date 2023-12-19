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
public class DatosCochesDia {
    private String fecha;
    private int numeroCoches;

    public DatosCochesDia() {
    }

    public DatosCochesDia(String fecha, int numeroCoches) {
        this.fecha = fecha;
        this.numeroCoches = numeroCoches;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getNumeroCoches() {
        return numeroCoches;
    }

    public void setNumeroCoches(int numeroCoches) {
        this.numeroCoches = numeroCoches;
    }
    
    public void mostrarDatosCochesDia(){
        System.out.println("Fecha: "+fecha);
        System.out.println("NumeroCoches: "+numeroCoches);
    }
    
    
}
