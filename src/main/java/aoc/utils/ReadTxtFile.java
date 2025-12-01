package aoc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilities to read input file.
 *
 * @author Samuel Liard
 */
public class ReadTxtFile {

    /**
     * Read one txt UTF8 file and return one String by line.
     *
     * @param fileName name of txt file
     * @return all lines
     * @throws IOException in case of read file error
     */
    public static List<String> readFileAsStringList(String fileName) throws IOException {
        List<String> result = new ArrayList<>();
        InputStream is = ReadTxtFile.class.getResourceAsStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String line = br.readLine();
        while (line != null) {
            result.add(line);
            line = br.readLine();
        }
        return result;
    }

    /**
     * Read one txt UTF8 file and return a char 2D array.
     * The first element represne the abscissa and the second the ordinate with 0,0 is the top left element.
     *
     * @param fileName name of txt file
     * @return all lines
     * @throws IOException in case of read file error
     */
    public static char[][] readFileAsCharArray(String fileName) throws IOException {
        List<String> allLines = readFileAsStringList(fileName);
        int maxLineLength = 0;
        for(String line : allLines) {
            if(maxLineLength < line.length()) {
                maxLineLength = line.length();
            }
        }
        char[][] result = new char[maxLineLength][allLines.size()];
        int y = 0;
        for(String line : allLines) {
            for(int x = 0; x < line.length(); x++) {
                result[x][y] = line.charAt(x);
            }
        }
        return result;
    }

}
