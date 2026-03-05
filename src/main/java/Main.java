import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bo.Calculations;
import bo.DataProcessor;
import bo.Sorter;
import dao.Conexao;
import dao.Persistencia;
import dto.Planet;
import dto.PlanetAggregate;

public class Main {

    private static final String OUTPUT_FILE = "saida.txt";
    private static final String TEMPLATE_FILE = "src/web/template.html";
    private static final String JSON_FILE = "planetas.json";
    private static final String XML_FILE = "planetas.xml";

    private static final double EARTH_ORBITAL_PERIOD_DAYS = 365.25;
    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double EARTH_MASS_KG = 5.972e24;
    private static final double PARSEC_TO_LIGHT_YEAR = 3.26156;

    private static boolean jsonGerado = false;
    private static boolean xmlGerado = false;
    private static boolean bancoAtualizado = false;

    public static void main(String[] args) throws Exception {
        Sorter sorter = new Sorter();
        sorter.queryData();

        DataProcessor processor = new DataProcessor();
        Calculations calc = new Calculations();

        Map<String, PlanetAggregate> dadosAgrupados;
        try {
            dadosAgrupados = processor.extrairDadosValidos(OUTPUT_FILE);
        } catch (Exception e) {
            e.printStackTrace();
            dadosAgrupados = new HashMap<>();
        }

        List<Planet> planetasConsolidados = new ArrayList<>();
        String todosOsDadosJson = montarJson(dadosAgrupados, calc, planetasConsolidados);
        persistirDadosIniciais(planetasConsolidados);

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", exchange -> {
            byte[] htmlBytes = Files.readAllBytes(Path.of(TEMPLATE_FILE));
            responder(exchange, 200, htmlBytes, "text/html; charset=UTF-8");
        });

        server.createContext("/dados", exchange ->
                responder(exchange, 200, todosOsDadosJson, "application/json; charset=UTF-8"));

        server.createContext("/atualizar-banco", exchange -> {
            if (bancoAtualizado) {
                responder(exchange, 200, "Banco de dados ja esta atualizado.", "text/plain; charset=UTF-8");
                return;
            }

            executarAcao(
                    exchange,
                    () -> {
                        new Conexao().salvarNoMySQL(planetasConsolidados);
                        bancoAtualizado = true;
                    },
                    "Banco de dados MySQL atualizado com sucesso!"
            );
        });

        server.createContext("/limpar-banco", exchange -> executarAcao(
                exchange,
                () -> {
                    new Conexao().limparBaseDeDados();
                    bancoAtualizado = false;
                },
                "Base de dados limpa com sucesso!"
        ));

        server.createContext("/gerar-json", exchange -> {
            if (jsonGerado && arquivoGerado(JSON_FILE)) {
                responder(exchange, 200, "Arquivo planetas.json ja foi gerado.", "text/plain; charset=UTF-8");
                return;
            }

            executarAcao(
                    exchange,
                    () -> {
                        new Persistencia().salvarJSON(planetasConsolidados);
                        jsonGerado = arquivoGerado(JSON_FILE);
                    },
                    "Arquivo planetas.json gerado com sucesso!"
            );
        });

        server.createContext("/gerar-xml", exchange -> {
            if (xmlGerado && arquivoGerado(XML_FILE)) {
                responder(exchange, 200, "Arquivo planetas.xml ja foi gerado.", "text/plain; charset=UTF-8");
                return;
            }

            executarAcao(
                    exchange,
                    () -> {
                        new Persistencia().salvarXML(planetasConsolidados);
                        xmlGerado = arquivoGerado(XML_FILE);
                    },
                    "Arquivo planetas.xml gerado com sucesso!"
            );
        });

        server.setExecutor(null);
        server.start();
        abrirNoNavegador();
    }

    private static String montarJson(Map<String, PlanetAggregate> dadosAgrupados, Calculations calc, List<Planet> planetasConsolidados) {
        StringBuilder jsonArray = new StringBuilder("[");
        boolean first = true;

        for (Map.Entry<String, PlanetAggregate> entry : dadosAgrupados.entrySet()) {
            PlanetAggregate agregado = entry.getValue();

            if (agregado.getOrbitalPeriods().isEmpty() || agregado.getRadii().isEmpty() || agregado.getMasses().isEmpty()) {
                continue;
            }

            int qtdAmostras = agregado.getSampleCount();
            String nomeSeguro = entry.getKey().replace("\"", "").replace("\\", "").trim();
            String nomeComAmostras = nomeSeguro + " (" + qtdAmostras + " amostras)";

            int discoveryYear = obterMaiorAno(agregado.getDiscoveryYears());
            String discoveryMethod = obterMetodoMaisFrequente(agregado.getDiscoveryMethods());
            List<String> discoveryMethods = obterMetodosUnicosOrdenados(agregado.getDiscoveryMethods());

            double periodoMedia = numeroSeguro(calc.calcularMedia(agregado.getOrbitalPeriods()));
            double periodoMediana = numeroSeguro(calc.calcularMediana(agregado.getOrbitalPeriods()));
            double periodoVariancia = numeroSeguro(calc.calcularVariancia(agregado.getOrbitalPeriods()));
            double periodoModa = numeroSeguro(calc.calcularModa(agregado.getOrbitalPeriods()));

            double raioMedia = numeroSeguro(calc.calcularMedia(agregado.getRadii()));
            double raioMediana = numeroSeguro(calc.calcularMediana(agregado.getRadii()));
            double raioVariancia = numeroSeguro(calc.calcularVariancia(agregado.getRadii()));
            double raioModa = numeroSeguro(calc.calcularModa(agregado.getRadii()));

            double massaMedia = numeroSeguro(calc.calcularMedia(agregado.getMasses()));
            double massaMediana = numeroSeguro(calc.calcularMediana(agregado.getMasses()));
            double massaVariancia = numeroSeguro(calc.calcularVariancia(agregado.getMasses()));
            double massaModa = numeroSeguro(calc.calcularModa(agregado.getMasses()));

            double distanciaMediaParsec = numeroSeguro(calc.calcularMedia(agregado.getDistances()));
            double distanciaMediaLy = numeroSeguro(distanciaMediaParsec * PARSEC_TO_LIGHT_YEAR);

            double periodoTerraX = numeroSeguro(periodoMedia / EARTH_ORBITAL_PERIOD_DAYS);
            double raioTerraX = numeroSeguro(raioMedia);
            double massaTerraX = numeroSeguro(massaMedia);

            double raioPlanetaKm = numeroSeguro(raioMedia * EARTH_RADIUS_KM);
            double massaPlanetaKg = numeroSeguro(massaMedia * EARTH_MASS_KG);

            Planet planeta = new Planet();
            planeta.setName(nomeComAmostras);
            planeta.setDiscoveryYear(discoveryYear);
            planeta.setDiscoveryMethod(discoveryMethod);
            planeta.setOrbitalPeriod(periodoMedia);
            planeta.setRadius(raioMedia);
            planeta.setMass(massaMedia);
            planeta.setDistance(distanciaMediaParsec);
            planetasConsolidados.add(planeta);

            if (!first) {
                jsonArray.append(",");
            }

            jsonArray.append("{")
                    .append("\"planeta\":\"").append(nomeComAmostras).append("\",")
                    .append("\"discoveryYear\":").append(discoveryYear).append(",")
                    .append("\"discoveryMethod\":\"").append(escapeJson(discoveryMethod)).append("\",")
                    .append("\"discoveryMethods\":").append(toJsonArray(discoveryMethods)).append(",")
                    .append("\"distanceParsec\":").append(distanciaMediaParsec).append(",")
                    .append("\"distanceLightYears\":").append(distanciaMediaLy).append(",")
                    .append("\"periodoMedia\":").append(periodoMedia).append(",")
                    .append("\"periodoMediana\":").append(periodoMediana).append(",")
                    .append("\"periodoVariancia\":").append(periodoVariancia).append(",")
                    .append("\"periodoModa\":").append(periodoModa).append(",")
                    .append("\"raioMedia\":").append(raioMedia).append(",")
                    .append("\"raioMediana\":").append(raioMediana).append(",")
                    .append("\"raioVariancia\":").append(raioVariancia).append(",")
                    .append("\"raioModa\":").append(raioModa).append(",")
                    .append("\"massaMedia\":").append(massaMedia).append(",")
                    .append("\"massaMediana\":").append(massaMediana).append(",")
                    .append("\"massaVariancia\":").append(massaVariancia).append(",")
                    .append("\"massaModa\":").append(massaModa).append(",")
                    .append("\"periodoTerraX\":").append(periodoTerraX).append(",")
                    .append("\"raioTerraX\":").append(raioTerraX).append(",")
                    .append("\"massaTerraX\":").append(massaTerraX).append(",")
                    .append("\"periodoTerraDias\":").append(EARTH_ORBITAL_PERIOD_DAYS).append(",")
                    .append("\"raioTerraKm\":").append(EARTH_RADIUS_KM).append(",")
                    .append("\"massaTerraKg\":").append(EARTH_MASS_KG).append(",")
                    .append("\"periodoPlanetaDias\":").append(periodoMedia).append(",")
                    .append("\"raioPlanetaKm\":").append(raioPlanetaKm).append(",")
                    .append("\"massaPlanetaKg\":").append(massaPlanetaKg).append(",")
                    .append("\"amostrasPeriodo\":").append(agregado.getOrbitalPeriods().size()).append(",")
                    .append("\"amostrasRaio\":").append(agregado.getRadii().size()).append(",")
                    .append("\"amostrasMassa\":").append(agregado.getMasses().size())
                    .append("}");

            first = false;
        }

        return jsonArray.append("]").toString();
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
        List<String> metodosOrdenados = obterMetodosUnicosOrdenados(metodos);
        if (metodosOrdenados.isEmpty()) {
            return "Nao informado";
        }
        return metodosOrdenados.get(0);
    }

    private static List<String> obterMetodosUnicosOrdenados(List<String> metodos) {
        Map<String, Integer> frequencias = new HashMap<>();

        for (String metodo : metodos) {
            if (metodo == null) {
                continue;
            }

            String metodoLimpo = metodo.trim();
            if (metodoLimpo.isBlank()) {
                continue;
            }

            int contagemAtual = frequencias.getOrDefault(metodoLimpo, 0) + 1;
            frequencias.put(metodoLimpo, contagemAtual);
        }

        if (frequencias.isEmpty()) {
            List<String> naoInformado = new ArrayList<>();
            naoInformado.add("Nao informado");
            return naoInformado;
        }

        List<Map.Entry<String, Integer>> entradas = new ArrayList<>(frequencias.entrySet());
        entradas.sort((a, b) -> {
            int porFrequencia = Integer.compare(b.getValue(), a.getValue());
            if (porFrequencia != 0) {
                return porFrequencia;
            }
            return a.getKey().compareToIgnoreCase(b.getKey());
        });

        List<String> metodosOrdenados = new ArrayList<>();
        for (Map.Entry<String, Integer> entrada : entradas) {
            metodosOrdenados.add(entrada.getKey());
        }

        return metodosOrdenados;
    }

    private static String toJsonArray(List<String> valores) {
        StringBuilder jsonArray = new StringBuilder("[");

        for (int i = 0; i < valores.size(); i++) {
            if (i > 0) {
                jsonArray.append(",");
            }
            jsonArray.append("\"").append(escapeJson(valores.get(i))).append("\"");
        }

        return jsonArray.append("]").toString();
    }

    private static String escapeJson(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static double numeroSeguro(double valor) {
        if (!Double.isFinite(valor) || valor < 0) {
            return 0;
        }
        return valor;
    }

    private static boolean arquivoGerado(String nomeArquivo) {
        try {
            Path caminho = Path.of(nomeArquivo);
            return Files.exists(caminho) && Files.size(caminho) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static void persistirDadosIniciais(List<Planet> planetasConsolidados) {
        Persistencia persistencia = new Persistencia();

        try {
            if (arquivoGerado(JSON_FILE)) {
                jsonGerado = true;
                System.out.println("Arquivo planetas.json ja existe.");
            } else {
                persistencia.salvarJSON(planetasConsolidados);
                jsonGerado = arquivoGerado(JSON_FILE);
                System.out.println("Arquivo planetas.json gerado.");
            }
        } catch (Exception e) {
            jsonGerado = false;
            System.out.println("Falha ao gerar JSON automaticamente: " + e.getMessage());
        }

        try {
            if (arquivoGerado(XML_FILE)) {
                xmlGerado = true;
                System.out.println("Arquivo planetas.xml ja existe.");
            } else {
                persistencia.salvarXML(planetasConsolidados);
                xmlGerado = arquivoGerado(XML_FILE);
                System.out.println("Arquivo planetas.xml gerado.");
            }
        } catch (Exception e) {
            xmlGerado = false;
            System.out.println("Falha ao gerar XML automaticamente: " + e.getMessage());
        }

        try {
            new Conexao().salvarNoMySQL(planetasConsolidados);
            bancoAtualizado = true;
            System.out.println("Persistencia no MySQL concluida.");
        } catch (Exception e) {
            bancoAtualizado = false;
            System.out.println("Falha ao atualizar MySQL automaticamente: " + e.getMessage());
        }
    }

    private static void executarAcao(HttpExchange exchange, Acao acao, String mensagemSucesso) throws IOException {
        try {
            acao.executar();
            responder(exchange, 200, mensagemSucesso, "text/plain; charset=UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            responder(exchange, 500, "Erro ao processar solicitacao.", "text/plain; charset=UTF-8");
        }
    }

    private static void responder(HttpExchange exchange, int status, String conteudo, String contentType) throws IOException {
        responder(exchange, status, conteudo.getBytes(StandardCharsets.UTF_8), contentType);
    }

    private static void responder(HttpExchange exchange, int status, byte[] conteudo, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, conteudo.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(conteudo);
        }
    }

    private static void abrirNoNavegador() {
        if (!Desktop.isDesktopSupported()) {
            return;
        }

        try {
            Desktop.getDesktop().browse(new URI("http://localhost:8080"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FunctionalInterface
    private interface Acao {
        void executar() throws Exception;
    }
}
