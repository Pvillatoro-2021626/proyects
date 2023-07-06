package org.patrickvillatoro.bean;

public class Medicamento {
    private int codigoMedicamento;
    private String nombresMedicamento;

    public Medicamento() {
    }

    public Medicamento(int codigoMedicamento, String nombresMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
        this.nombresMedicamento = nombresMedicamento;
    }

    public int getCodigoMedicamento() {
        return codigoMedicamento;
    }

    public void setCodigoMedicamento(int codigoMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
    }

    public String getNombresMedicamento() {
        return nombresMedicamento;
    }

    public void setNombresMedicamento(String nombresMedicamento) {
        this.nombresMedicamento = nombresMedicamento;
    }

    @Override
    public String toString() {
        return getCodigoMedicamento()+"). "+getNombresMedicamento();
    }
    
}
