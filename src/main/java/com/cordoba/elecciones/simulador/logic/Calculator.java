package com.cordoba.elecciones.simulador.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cordoba.elecciones.simulador.dto.CalculoBancas;
import com.cordoba.elecciones.simulador.dto.VotosBancas;
import com.cordoba.elecciones.simulador.dto.VotosPorPartido;

public class Calculator {

    public CalculoBancas calcularDatosElectorales(VotosBancas votosBancas) {
        CalculoBancas calculo = new CalculoBancas();
        List<VotosPorPartido> votosPorPartido = votosBancas.getVotos();
        Integer totalBancas = votosBancas.getBancas();
        Integer bancasARepartir = Integer.parseInt(BigDecimal
                .valueOf(totalBancas)
                .divide(BigDecimal.valueOf(2), 0, RoundingMode.HALF_DOWN)
                .toString());
        calculo.setBancasPorPartido(calcularBancas(votosPorPartido,
                totalBancas, bancasARepartir));
        Integer votosTotal = calcularVotosTotal(votosPorPartido);
        calculo.setTotalVotos(votosTotal);
        calculo.setPorcentajes(calcularPorcentajePorPartido(votosPorPartido,
                votosTotal));
        return calculo;
    }

    private Map<String, Integer> calcularBancas(
            List<VotosPorPartido> votosPorPartido, Integer totalBancas,
            Integer bancasARepartir) {
        Map<String, List<BigDecimal>> coeficientesPorPartido = calcularCoeficientes(
                removerMasVotado(votosPorPartido), bancasARepartir);
        List<BigDecimal> coeficientesOrdenados = ordenarCoeficientes(coeficientesPorPartido);
        Map<String, List<BigDecimal>> coeficientesPorBanca = repartirBancasPorCoeficientes(
                bancasARepartir, coeficientesPorPartido, coeficientesOrdenados);
        Map<String, Integer> bancasPorPartido = contarBancasPorPartido(
                votosPorPartido, coeficientesPorBanca, totalBancas);
        return bancasPorPartido;
    }

    private Map<String, Integer> contarBancasPorPartido(
            List<VotosPorPartido> votosPorPartido,
            Map<String, List<BigDecimal>> coeficientesPorBanca,
            Integer totalBancas) {
        Map<String, Integer> bancasPorPartido = new HashMap<String, Integer>();
        for (Map.Entry<String, List<BigDecimal>> result : coeficientesPorBanca
                .entrySet()) {
            bancasPorPartido.put(result.getKey(), result.getValue().size());
        }
        bancasPorPartido.putAll(calcularBancasMasVotado(votosPorPartido,
                totalBancas));
        return bancasPorPartido;
    }

    private VotosPorPartido obtenerMasVotado(
            List<VotosPorPartido> votosPorPartido) {
        VotosPorPartido partidoMasVotado = votosPorPartido.get(0);
        for (VotosPorPartido votos : votosPorPartido) {
            partidoMasVotado = partidoMasVotado.getVotos() > votos.getVotos() ? partidoMasVotado
                    : votos;
        }
        return partidoMasVotado;
    }

    private Map<String, Integer> calcularBancasMasVotado(
            List<VotosPorPartido> votosPorPartido, Integer totalBancas) {
        Map<String, Integer> partidoMasVotado = new HashMap<String, Integer>();
        VotosPorPartido masVotado = obtenerMasVotado(votosPorPartido);
        partidoMasVotado.put(
                masVotado.getCandidato(),
                Integer.parseInt(BigDecimal.valueOf(totalBancas)
                        .divide(BigDecimal.valueOf(2), 0, RoundingMode.HALF_UP)
                        .toString()));
        return partidoMasVotado;
    }

    private List<VotosPorPartido> removerMasVotado(
            List<VotosPorPartido> votosPorPartido) {
        List<VotosPorPartido> votosPorPartidoCopy = new ArrayList<VotosPorPartido>(
                votosPorPartido);
        votosPorPartidoCopy.remove(obtenerMasVotado(votosPorPartido));
        return votosPorPartidoCopy;
    }

    private Map<String, List<BigDecimal>> repartirBancasPorCoeficientes(
            Integer places,
            Map<String, List<BigDecimal>> coeficientesPorPartido,
            List<BigDecimal> coeficientesOrdenados) {
        Map<String, List<BigDecimal>> coeficientesPorBanca = new HashMap<String, List<BigDecimal>>();
        int counter = 0;
        for (BigDecimal i : coeficientesOrdenados) {
            for (Map.Entry<String, List<BigDecimal>> result : coeficientesPorPartido
                    .entrySet()) {
                if (result.getValue().contains(i)) {
                    String key = result.getKey();
                    if (coeficientesPorBanca.get(key) == null) {
                        coeficientesPorBanca.put(key,
                                new ArrayList<BigDecimal>(places));
                    }
                    if (!coeficientesPorBanca.get(key).contains(i)) {
                        coeficientesPorBanca.get(key).add(i);
                        counter++;
                        break;
                    }
                }
            }
            if (counter >= places) {
                break;
            }
        }
        return coeficientesPorBanca;
    }

    private List<BigDecimal> ordenarCoeficientes(
            Map<String, List<BigDecimal>> results) {
        List<BigDecimal> join = new ArrayList<BigDecimal>();
        for (Map.Entry<String, List<BigDecimal>> value : results.entrySet()) {
            join.addAll(value.getValue());
        }
        Collections.sort(join, Collections.reverseOrder());
        return join;
    }

    private Map<String, List<BigDecimal>> calcularCoeficientes(
            List<VotosPorPartido> votosPorPartido, Integer reparto) {
        Map<String, List<BigDecimal>> results = new HashMap<String, List<BigDecimal>>();
        for (VotosPorPartido value : votosPorPartido) {
            for (int j = 1; j <= reparto; j++) {
                if (results.get(value.getCandidato()) == null) {
                    results.put(value.getCandidato(),
                            new ArrayList<BigDecimal>());
                }
                results.get(value.getCandidato())
                        .add(BigDecimal.valueOf(value.getVotos()).divide(
                                BigDecimal.valueOf(j), 2, RoundingMode.HALF_UP));
            }
        }
        return results;
    }

    private Integer calcularVotosTotal(List<VotosPorPartido> votos) {
        Integer result = 0;
        for (VotosPorPartido votosPorPartido : votos) {
            result += votosPorPartido.getVotos();
        }
        return result;
    }

    private Map<String, BigDecimal> calcularPorcentajePorPartido(
            List<VotosPorPartido> votos, Integer votosTotal) {
        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        for (VotosPorPartido votosPartido : votos) {
            BigDecimal porcentajeVotos = BigDecimal
                    .valueOf(votosPartido.getVotos())
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(votosTotal), 2,
                            RoundingMode.HALF_UP);
            result.put(votosPartido.getCandidato(), porcentajeVotos);
        }
        return result;
    }

}
