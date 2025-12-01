package aoc.days;

import aoc.Day;
import aoc.utils.ReadTxtFile;

import java.util.List;

public class Day01 extends Day<Integer> {

    public static void main(String[] args) {
        Day01 d = new Day01();
        d.init("/day01.txt");
        d.printResult();
    }

    String data;

    public void init(String ...args) {
        // init stuff
        if(args == null || args.length == 0) {
            println("No args");
            return;
        }
        try {
            List<String> allLines = ReadTxtFile.readFileAsStringList(args[0]);
            data = allLines.get(0);
        } catch (Exception ex) {
            println("Read file error ("+args[0]+") : "+ex.getMessage());
        }
    }

    public Integer part1() {
        try {
            return Integer.parseInt(data);
        } catch (Exception ex) {
            return -1;
        }
    }

    public Integer part2() {
        try {
            return Integer.parseInt(data)*2;
        } catch (Exception ex) {
            return -1;
        }
    }

}
