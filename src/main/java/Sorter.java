import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Sorter {

    //TODO scan the data base for duplicates (Separated class), choose between the values at random.

    Random random = new Random();

    final String URLDATABASE = "https://exoplanetarchive.ipac.caltech.edu/TAP/sync?query=";

    final String SQLSTATEMENT = "select+pl_name,disc_year,discoverymethod,pl_orbper,pl_rade,pl_masse,sy_dist" +
            "+from+ps&format=csv";
    final String OUTPUT = "saida.txt";

    public void queryData() {
        try {

            if (!fileExists(OUTPUT)) {

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

                    if (currentLine.length < 7) {
                        //Skips if the current line has less than 7 elements.
                        continue;
                    } else if (currentLine[0].equals(lastLine[0])) {
                        duplicates.add(lastLine);

                    } else if (duplicates.size() > 1) {

                        String blankItem = "";
                        String[] line = {};
                        int randomNumber;
                        int blankPosition = containsBlank(duplicates.getFirst());

                        while (blankPosition > -1) {

                            int i = 0;

                            do {
                                randomNumber = random.nextInt(duplicates.size());
                                line = duplicates.get(randomNumber);
                                blankItem = line[blankPosition];

                                if (i >= 10) {
                                    blankItem = "0";
                                }

                                line[blankPosition] = blankItem;
                                duplicates.set(0, line);

                                i++;
                            } while (blankItem.isBlank());

                            blankPosition = containsBlank(duplicates.getFirst());
                        }

                        file.write(Arrays.toString(duplicates.getFirst()) + "\n");
                        duplicates.clear();

                    } else {

                        file.write(inputLine + "\n");
                        duplicates.clear();

                    }

                    lastLine = currentLine;

                }

                file.close();
                reader.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean fileExists(String filename) {
        File file = new File(filename);
        //TODO remove comment
        //return file.exists();
        return false;
    }

    public int containsBlank(String[] data) {
        /*
          Returns the index of the first occurrence of a blank value, if no blanks are found the return -1;
         */
        for (int i = 0; i < data.length; i++) {
            if (data[i].isBlank()) {
                return i;
            }
        }
        return -1;
    }


}

