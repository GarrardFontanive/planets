import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) throws Exception {
        Random random = new Random();

        Sorter sorter = new Sorter();
        sorter.queryData();

    }
}