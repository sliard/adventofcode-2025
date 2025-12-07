package aoc.days;

import aoc.Day;
import aoc.utils.ReadTxtFile;

import java.util.*;
import java.util.stream.Collectors;

public class Day07 extends Day<Long> {

    public static void main(String[] args) {
        Day07 d = new Day07();
        d.init("/day07.txt");
        d.printResult();
    }

    Map<String, Long> cacheMap = new HashMap<>();
    List<String> data;
    int start = 0;

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
                if(lineNumber == 0) {
                    start = line.indexOf("S");
                }
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
            Set<Integer> tachyonList = new HashSet<>();
            tachyonList.add(start);

            for(int y=1; y<data.size(); y++) {

                /*
                var dataDisplay = tachyonList.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining("-"));
                println(dataDisplay);
                 */

                Set<Integer> newTachyonList = new HashSet<>();
                for(int p : tachyonList) {
                    char c = data.get(y).charAt(p);
                    if(c == '.') {
                        newTachyonList.add(p);
                    } else {
                        result++;
                        if(p-1 >= 0) {
                            newTachyonList.add(p-1);
                        }
                        if(p+1 < data.get(y).length()) {
                            newTachyonList.add(p+1);
                        }
                    }
                }
                tachyonList =  newTachyonList;
            }

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1l;
        }
    }

    public long getNbWay(int p, int y) {
        if(y == data.size() -1) {
            return 1;
        }
        String key = p+"-"+y;
        if(cacheMap.containsKey(key)) {
            return cacheMap.get(key);
        }
        char c = data.get(y).charAt(p);
        long result = 0;
        if(c == '.') {
            result = getNbWay(p, y+1);
            cacheMap.put(key, result);
            return result;
        } else {
            if(p-1 >= 0) {
                result += getNbWay(p-1, y+1);
            }
            if(p+1 < data.get(y).length()) {
                result += getNbWay(p+1, y+1);
            }
            cacheMap.put(key, result);
            return result;
        }
    }

    public Long part2() {
        try {
            long result = 0;
            Set<Integer> tachyonList = new HashSet<>();

            return (long)getNbWay(start, 1);
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1l;
        }
    }

}
