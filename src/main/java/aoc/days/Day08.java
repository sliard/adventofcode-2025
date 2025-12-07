package aoc.days;

import aoc.Day;
import aoc.utils.ReadTxtFile;

import java.util.*;

public class Day08 extends Day<Long> {

    public static void main(String[] args) {
        Day08 d = new Day08();
        d.init("/day08.txt");
        d.printResult();
    }

    List<String> data;

    public void init(String ...args) {
        // init stuff
        if(args == null || args.length == 0) {
            println("No args");
            return;
        }
        data = new ArrayList<>();

        int lineNumber = 0;
        try {
            List<String> allLines = ReadTxtFile.readFileAsStringList(args[0]);
            for(String line : allLines) {
                data.add(line);
                lineNumber++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            println(lineNumber + " : Read file error ("+args[0]+") : "+ex.getMessage());
        }
    }


    public Long part1() {
        try {
            long result = 0;

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1l;
        }
    }

    public Long part2() {
        try {
            long result = 0;
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1l;
        }
    }

}
