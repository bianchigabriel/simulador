package com.cordoba.elecciones.simulador.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class CalculoBancas implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, Integer> bancasPorPartido;

    private Map<String, BigDecimal> porcentajes;

    Integer totalVotos;

    public Map<String, Integer> getBancasPorPartido() {
        return bancasPorPartido;
    }

    public void setBancasPorPartido(Map<String, Integer> bancasPorPartido) {
        this.bancasPorPartido = bancasPorPartido;
    }

    public Map<String, BigDecimal> getPorcentajes() {
        return porcentajes;
    }

    public void setPorcentajes(Map<String, BigDecimal> porcentajes) {
        this.porcentajes = porcentajes;
    }

    public Integer getTotalVotos() {
        return totalVotos;
    }

    public void setTotalVotos(Integer totalVotos) {
        this.totalVotos = totalVotos;
    }

}
