package com.cordoba.elecciones.simulador.dto;

import java.io.Serializable;
import java.util.List;

public class VotosBancas implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer bancas;

    private List<VotosPorPartido> votos;

    public Integer getBancas() {
        return bancas;
    }

    public void setBancas(Integer bancas) {
        this.bancas = bancas;
    }

    public List<VotosPorPartido> getVotos() {
        return votos;
    }

    public void setVotos(List<VotosPorPartido> votos) {
        this.votos = votos;
    }

}
