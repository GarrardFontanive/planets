import org.w3c.dom.html.HTMLDocument;

import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args){

        String filePath = "src/main/resources/planet_data.csv";
        int startLine = 49;
        String line;
        double totalOrbitalDays = 0;
        int totalPlanets = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int currentLine = 1;

            while (currentLine < startLine && (line = br.readLine()) != null) {
                currentLine++;
            }

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                for(int i = 0; i < values.length; i++){
                    if(values[i].isEmpty()){
                        values[i] = "0";
                    }
                }
                Planet planet = new Planet(values[0], Integer.parseInt(values[6]), Double.parseDouble(values[11]),
                        Double.parseDouble(values[15]));



                System.out.println(planet);
                totalOrbitalDays += Double.parseDouble(values[11]);
                totalPlanets++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Average of total Orbital Days: " + totalOrbitalDays/totalPlanets);
        System.out.println("Total planets: " + totalPlanets);



    }
}
