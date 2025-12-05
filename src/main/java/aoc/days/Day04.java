package aoc.days;

import aoc.Day;
import aoc.utils.ReadTxtFile;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day04 extends Day<Long> {

    public static void main(String[] args) {
        Day04 d = new Day04();
        d.init("/day04.txt");
        d.printResult();
    }

    List<List<Character>> data;

    public void init(String ...args) {
        // init stuff
        if(args == null || args.length == 0) {
            println("No args");
            return;
        }
        data = new ArrayList<>();
        try {
            List<String> allLines = ReadTxtFile.readFileAsStringList(args[0]);
            for(String line : allLines) {
                var dataLine = new ArrayList<Character>();
                for(char c : line.toCharArray()) {
                    dataLine.add(c);
                }
                data.add(dataLine);
            }
        } catch (Exception ex) {
            println("Read file error ("+args[0]+") : "+ex.getMessage());
        }
    }

    public Long part1() {
        try {
            long result = 0;

            for(int y = 0; y < data.size(); y++) {
                var line = data.get(y);
                String l = "";
                for(int x = 0; x < line.size(); x++) {
                    boolean isRoll = (line.get(x) == '@');

                    if(isRoll) {
                        var r = canAccess(x,y);
                        result += r ? 1 : 0;
                        l += r ? 'x':'@';
                    } else {
                        l += '.';
                    }
                }
                println(l);
            }


            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1l;
        }
    }

    public boolean canAccess(int x, int y) {

        int nb = 0;
        for(int yy = y-1; yy <= y+1; yy++) {
            if(yy < 0 || yy >= data.size()) {
                continue;
            }
            var line = data.get(yy);
            for(int xx = x-1; xx <= x+1; xx++) {
                if(xx < 0 || xx >= line.size()) {
                    continue;
                }
                if(xx == x && yy == y) {
                    continue;
                }

                nb += (line.get(xx) == '@') ? 1 : 0;
            }
        }
//        println("("+x+","+y+")="+nb);
        return nb < 4;
    }

    public Long part2() {
        List<List<Character>> clone = data.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());

        long nbDeleteTotal = 0;

        try {
            long nbDelete = 10;

            while (nbDelete > 0) {
                nbDelete = 0;

                List<List<Character>> cloneTmp = new ArrayList<>();

                for(int y = 0; y < data.size(); y++) {
                    var line = data.get(y);
                    String l = "";
                    List<Character> newLine = new ArrayList<>();
                    for(int x = 0; x < line.size(); x++) {
                        boolean isRoll = (line.get(x) == '@');

                        if(isRoll) {
                            var r = canAccess(x,y);
                            nbDelete += r ? 1 : 0;
                            l += r ? 'x':'@';
                            newLine.add(r ? '.':'@');
                        } else {
                            l += '.';
                            newLine.add('.');
                        }
                    }
                    cloneTmp.add(newLine);
                    println(l);
                }
                nbDeleteTotal+= nbDelete;
                data = cloneTmp;
            }

            data = clone;

            return nbDeleteTotal;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1l;
        }
    }

}
