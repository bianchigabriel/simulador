package com.cordoba.elecciones.simulador.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.cordoba.elecciones.simulador.dto.CalculoBancas;
import com.cordoba.elecciones.simulador.dto.VotosBancas;
import com.cordoba.elecciones.simulador.logic.Calculator;

@Path("/simulator")
public class Simulator {
    @POST
    @Path("/calculate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CalculoBancas calculate(VotosBancas votosBancas) {
        Calculator calculator = new Calculator();
        return calculator.calcularDatosElectorales(votosBancas);
    }
}
