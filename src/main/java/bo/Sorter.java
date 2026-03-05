package bo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Sorter {

    private static final String URL_DATABASE = "https://exoplanetarchive.ipac.caltech.edu/TAP/sync?query=";
    private static final String SQL_STATEMENT = "select+pl_name,disc_year,discoverymethod,pl_orbper,pl_rade,pl_masse,sy_dist+from+ps&format=csv";
    private static final String OUTPUT = "saida.txt";

    public void queryData() {
        if (arquivoJaCarregado(OUTPUT)) {
            System.out.println("Arquivo de entrada ja carregado: " + OUTPUT);
            return;
        }

        if (fileExists(OUTPUT) && arquivoEmUso(OUTPUT)) {
            System.out.println("Arquivo de entrada esta aberto em outro programa. Feche e tente novamente.");
            return;
        }

        try {
            URL url = new URL(URL_DATABASE + SQL_STATEMENT);
            URLConnection connection = url.openConnection();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                 FileWriter file = new FileWriter(OUTPUT)) {
                String inputLine;

                while ((inputLine = reader.readLine()) != null) {
                    String[] currentLine = inputLine.split(",");
                    if (currentLine.length >= 7) {
                        file.write(inputLine + "\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean fileExists(String filename) {
        return new File(filename).exists();
    }

    private boolean arquivoJaCarregado(String filename) {
        File arquivo = new File(filename);
        if (!arquivo.exists() || arquivo.length() == 0) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String cabecalho = reader.readLine();
            String primeiraLinha = reader.readLine();

            if (cabecalho == null || primeiraLinha == null) {
                return false;
            }

            return cabecalho.contains("pl_name")
                    && cabecalho.contains("disc_year")
                    && cabecalho.contains("discoverymethod")
                    && cabecalho.contains("pl_orbper")
                    && cabecalho.contains("pl_rade")
                    && cabecalho.contains("pl_masse")
                    && cabecalho.contains("sy_dist")
                    && !primeiraLinha.isBlank();
        } catch (IOException e) {
            return false;
        }
    }

    private boolean arquivoEmUso(String filename) {
        File arquivo = new File(filename);
        if (!arquivo.exists()) {
            return false;
        }

        try (FileWriter ignored = new FileWriter(arquivo, true)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
