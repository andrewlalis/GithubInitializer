package nl.andrewlalis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Contains some methods which come in handy in lots of other places.
 */
public class FileUtils {

    /**
     * Reads the contents of the file specified by the filename into a String.
     * @param filename The filename to read the file of, either relative or absolute.
     * @return A string containing the file's contents.
     */
    public static String readStringFromFile(String filename) {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(FileUtils.class.getResourceAsStream(filename)))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
