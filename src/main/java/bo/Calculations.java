package bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculations {

    public double calcularMedia(List<Double> dados) {
        if (dados == null || dados.isEmpty()) {
            return 0;
        }

        double soma = 0;
        for (double valor : dados) {
            soma += valor;
        }
        return soma / dados.size();
    }

    public double calcularMediana(List<Double> dados) {
        if (dados == null || dados.isEmpty()) {
            return 0;
        }

        List<Double> copia = new ArrayList<>(dados);
        Collections.sort(copia);
        int meio = copia.size() / 2;

        if (copia.size() % 2 == 0) {
            return (copia.get(meio - 1) + copia.get(meio)) / 2.0;
        }

        return copia.get(meio);
    }

    public double calcularVariancia(List<Double> dados) {
        if (dados == null || dados.isEmpty()) {
            return 0;
        }

        double media = calcularMedia(dados);
        double soma = 0;
        for (double valor : dados) {
            double diferenca = valor - media;
            soma += diferenca * diferenca;
        }
        return soma / dados.size();
    }

    public double calcularModa(List<Double> dados) {
        if (dados == null || dados.isEmpty()) {
            return 0;
        }

        Map<Double, Integer> frequencias = new HashMap<>();
        double moda = dados.get(0);
        int maiorFrequencia = 0;

        for (double valor : dados) {
            int frequencia = frequencias.getOrDefault(valor, 0) + 1;
            frequencias.put(valor, frequencia);

            if (frequencia > maiorFrequencia) {
                maiorFrequencia = frequencia;
                moda = valor;
            }
        }

        return moda;
    }
}