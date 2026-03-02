import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

//TODO scan the data base for duplicates (Separated class), choose between the values at random.

public class Main {
    public static void main(String[] args) throws Exception {

//        Sorter sorter = new Sorter();
//        sorter.queryData();

        ArrayList<String[]> duplicates = new ArrayList<>();
        String[] a = {"Kepler-33 f","2011","Transit","41.02800000000","4.00000000","9.60000000","1209.16000000"};
        String[] b = {"Kepler-33 f","2011","Transit","41.02900000000","4.00000000","9.65000000","1209.16000000"};
        String[] c = {"Kepler-33 f","2011","Transit","41.02806370000","4.14000000","","1209.16000000"};
        String[] d = {"Kepler-33 f","2011","Transit","","3.83100000","","1209.16000000"};

        duplicates.add(a);
        duplicates.add(b);
        duplicates.add(c);
        duplicates.add(d);

        for (int i = 0; i<duplicates.size();i++) {

        }
    }
}