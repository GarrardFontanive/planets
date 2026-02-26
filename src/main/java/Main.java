import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Main {
    public static void main(String[] args) throws Exception {

        final String URLDATABASE = "https://exoplanetarchive.ipac.caltech.edu/TAP/sync?query=";

        //data gathered from the query: planet name, discovery year, discovery method, orbital period,
        // planet radius (earth), planet mass (earth) and distance (pc).
        final String SQLSTATEMENT = "select+pl_name,disc_year,discoverymethod,pl_orbper,pl_rade,pl_masse,sy_dist" +
                "+from+ps&format=csv";

        URL url = new URL(URLDATABASE + SQLSTATEMENT);
        URLConnection connection = url.openConnection();
        FileWriter file = new FileWriter("output.txt");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        String lastPlanet = "";
        String[] line;

        while ((inputLine = reader.readLine()) != null) {

            line = inputLine.split(",");


            if (lastPlanet.equals(line[0])) {
                System.out.println(lastPlanet + " is a duplicate.");
            } else {
                file.write(inputLine + "\n");
            }

            lastPlanet = line[0];

        }
        file.close();

        reader.close();



    }
}