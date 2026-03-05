package bo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dto.Planet;
import dto.PlanetAggregate;

public class DataProcessor {

    public Map<String, PlanetAggregate> extrairDadosValidos(String arquivo) throws Exception {
        Map<String, PlanetAggregate> mapa = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                if (linha.contains("pl_name")) {
                    continue;
                }

                String[] dados = parseCsvLine(linha);
                if (dados.length < 7) {
                    continue;
                }

                String nome = limparTexto(dados[0]);
                if (nome.isBlank()) {
                    continue;
                }

                PlanetAggregate agregado = mapa.computeIfAbsent(nome, k -> new PlanetAggregate());
                adicionarInteiro(agregado.getDiscoveryYears(), dados[1]);
                adicionarTexto(agregado.getDiscoveryMethods(), dados[2]);
                adicionarValor(agregado.getOrbitalPeriods(), dados[3]);
                adicionarValor(agregado.getRadii(), dados[4]);
                adicionarValor(agregado.getMasses(), dados[5]);
                adicionarValor(agregado.getDistances(), dados[6]);
            }
        }

        return mapa;
    }

    public List<Planet> consolidarPlanetas(Map<String, PlanetAggregate> mapa, Calculations calc) {
        List<Planet> consolidados = new ArrayList<>();

        for (Map.Entry<String, PlanetAggregate> entry : mapa.entrySet()) {
            PlanetAggregate agregado = entry.getValue();
            if (agregado.getSampleCount() == 0) {
                continue;
            }

            Planet planeta = new Planet();
            planeta.setName(entry.getKey());
            planeta.setDiscoveryYear(obterMaiorAno(agregado.getDiscoveryYears()));
            planeta.setDiscoveryMethod(obterMetodoMaisFrequente(agregado.getDiscoveryMethods()));
            planeta.setOrbitalPeriod(calc.calcularMedia(agregado.getOrbitalPeriods()));
            planeta.setRadius(calc.calcularMedia(agregado.getRadii()));
            planeta.setMass(calc.calcularMedia(agregado.getMasses()));
            planeta.setDistance(calc.calcularMedia(agregado.getDistances()));
            consolidados.add(planeta);
        }

        return consolidados;
    }

    private static String limparTexto(String texto) {
        return texto == null ? "" : texto.replace("\"", "").trim();
    }

    private static void adicionarValor(List<Double> lista, String valorStr) {
        if (valorStr == null) {
            return;
        }

        String valorLimpo = limparTexto(valorStr);
        if (valorLimpo.isBlank()) {
            return;
        }

        try {
            lista.add(Double.parseDouble(valorLimpo));
        } catch (NumberFormatException ignored) {
        }
    }

    private static void adicionarInteiro(List<Integer> lista, String valorStr) {
        if (valorStr == null) {
            return;
        }

        String valorLimpo = limparTexto(valorStr);
        if (valorLimpo.isBlank()) {
            return;
        }

        try {
            lista.add(Integer.parseInt(valorLimpo));
        } catch (NumberFormatException ignored) {
        }
    }

    private static void adicionarTexto(List<String> lista, String valorStr) {
        String valorLimpo = limparTexto(valorStr);
        if (!valorLimpo.isBlank()) {
            lista.add(valorLimpo);
        }
    }

    private static int obterMaiorAno(List<Integer> anos) {
        int maiorAno = 0;
        for (int ano : anos) {
            if (ano > maiorAno) {
                maiorAno = ano;
            }
        }
        return maiorAno;
    }

    private static String obterMetodoMaisFrequente(List<String> metodos) {
        if (metodos.isEmpty()) {
            return "Nao informado";
        }

        Map<String, Integer> frequencias = new HashMap<>();
        String melhorMetodo = metodos.get(0);
        int maiorFrequencia = 0;

        for (String metodo : metodos) {
            int freq = frequencias.getOrDefault(metodo, 0) + 1;
            frequencias.put(metodo, freq);

            if (freq > maiorFrequencia) {
                maiorFrequencia = freq;
                melhorMetodo = metodo;
            }
        }

        return melhorMetodo;
    }

    private static String[] parseCsvLine(String linha) {
        List<String> campos = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        boolean emAspas = false;

        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);

            if (c == '"') {
                if (emAspas && i + 1 < linha.length() && linha.charAt(i + 1) == '"') {
                    atual.append('"');
                    i++;
                } else {
                    emAspas = !emAspas;
                }
                continue;
            }

            if (c == ',' && !emAspas) {
                campos.add(atual.toString());
                atual.setLength(0);
                continue;
            }

            atual.append(c);
        }

        campos.add(atual.toString());
        return campos.toArray(new String[0]);
    }
}