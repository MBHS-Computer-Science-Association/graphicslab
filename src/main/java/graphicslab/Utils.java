package graphicslab;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class Utils {

    public static String loadResource(String fileName) throws Exception {
        String result = "";
        System.out.println(fileName);
        try (InputStream in = new FileInputStream(fileName)) {
        	Scanner scanner;
            result = (scanner = new Scanner(in, "UTF-8")).useDelimiter("\\A").next();
            scanner.close();
        }
        System.out.println(result);
        return result;
    }
}