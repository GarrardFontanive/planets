import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class Sorter {

    //TODO scan the data base for duplicates (Separated class), choose between the values at random.

    final String URLDATABASE = "https://exoplanetarchive.ipac.caltech.edu/TAP/sync?query=";

    final String SQLSTATEMENT = "select+pl_name,disc_year,discoverymethod,pl_orbper,pl_rade,pl_masse,sy_dist" +
            "+from+ps&format=csv";
    final String OUTPUT = "saida.txt";

    public void queryData(){
        try {

            if (!fileExists(OUTPUT)){

            URL url = new URL(URLDATABASE + SQLSTATEMENT);
            URLConnection connection = url.openConnection();
            FileWriter file = new FileWriter(OUTPUT);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            String[] currentLine;
            String[] lastLine = {""};
            ArrayList<String[]> duplicates = new ArrayList<>();



            while ((inputLine = reader.readLine()) != null) {

                currentLine = inputLine.split(",");


                if (currentLine[0].equals(lastLine[0])) {
                    duplicates.add(lastLine);

                } else if(duplicates.size() > 1) {

                    for (int i = 0; i < duplicates.size(); i++) {
                        System.out.println(Arrays.toString(duplicates.get(i)));
                    }
                    System.out.println();


                    // dont delete !!!
                    duplicates.clear();

                }


                file.write(inputLine + "\n");

                lastLine = currentLine;
            }

            file.close();
            reader.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean fileExists(String filename){
        File file = new File(filename);
        //return file.exists();
        return false;
    }


}
