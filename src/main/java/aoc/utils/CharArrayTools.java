package aoc.utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * tools for Character Array management.
 *
 * @author Samuel Liard
 */
public class CharArrayTools {

    /**
     * Find the position of one character in array.
     * @param allData data array
     * @param c character to find
     * @return character position or null if it's not present
     */
    public static Point findFirstChar(char[][] allData, char c) {
        for(int x = 0; x < allData.length; x++) {
            for(int y = 0; y < allData[x].length; y++) {
                if(allData[x][y] == c) {
                    return new Point(x,y);
                }
            }
        }
        return null;
    }

    /**
     * Find all positions of one character in array.
     * @param allData data array
     * @param c character to find
     * @return character positions
     */
    public static List<Point> findAllChar(char[][] allData, char c) {
        List<Point> result = new ArrayList<>();
        for(int x = 0; x < allData.length; x++) {
            for(int y = 0; y < allData[x].length; y++) {
                if(allData[x][y] == c) {
                    result.add(new Point(x,y));
                }
            }
        }
        return result;
    }

    /**
     * Test if point is inside the array
     * @param allData data array
     * @param p position
     * @return true if the point is inside the array
     */
    public static boolean inRange(char[][] allData, Point p) {
        return p.x >=0 && p.x < allData.length && p.y >= 0 && p.y < allData[p.x].length;
    }

}
