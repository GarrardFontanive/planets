package bo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Sorter {

    private static final String URL_DATABASE = "https://exoplanetarchive.ipac.caltech.edu/TAP/sync?query=";
    private static final String SQL_STATEMENT = "select+pl_name,disc_year,discoverymethod,pl_orbper,pl_rade,pl_masse,sy_dist+from+ps&format=csv";
    private static final String OUTPUT = "saida.txt";

    public void queryData() {
        if (fileExists(OUTPUT)) {
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
}