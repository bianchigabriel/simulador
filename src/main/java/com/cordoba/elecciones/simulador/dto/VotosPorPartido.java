package com.cordoba.elecciones.simulador.dto;

import java.io.Serializable;

public class VotosPorPartido implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer votos;

    private String candidato;

    public Integer getVotos() {
        return votos;
    }

    public void setVotos(Integer votos) {
        this.votos = votos;
    }

    public String getCandidato() {
        return candidato;
    }

    public void setCandidato(String candidato) {
        this.candidato = candidato;
    }

}
