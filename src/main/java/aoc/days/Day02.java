package aoc.days;

import aoc.Day;
import aoc.utils.ReadTxtFile;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class Day02 extends Day<Long> {

    public static void main(String[] args) {
        Day02 d = new Day02();
        d.init("/day02.txt");
        d.printResult();
    }

    List<IdRange> data;

    public void init(String ...args) {
        // init stuff
        if(args == null || args.length == 0) {
            println("No args");
            return;
        }
        data = new ArrayList<>();
        try {
            List<String> allLines = ReadTxtFile.readFileAsStringList(args[0]);
            var d = allLines.get(0).split(",");
            for(String range : d) {
                data.add(new IdRange(range));
            }
        } catch (Exception ex) {
            println("Read file error ("+args[0]+") : "+ex.getMessage());
        }
    }

    public Long part1() {
        try {

            long result = 0;
            for(IdRange range : data) {
                for(long i=range.start; i<=range.end; i++) {
                    result += isValid(i) ? i : 0;
                }
            }

            return result;
        } catch (Exception ex) {
            return -1l;
        }
    }

    public Long part2() {
        try {

            long result = 0;
            for(IdRange range : data) {
                for(long i=range.start; i<=range.end; i++) {
                    result += isValidPart2(i) ? i : 0;
                }
            }

            return result;
        } catch (Exception ex) {
            return -1l;
        }
    }

    public boolean isValid(long val) {

        var stringVal =  String.valueOf(val);
        int valLength = stringVal.length();
        if(valLength % 2 != 0) {
//            println(val+" NO");
            return false;
        }
        var valPart1 = stringVal.substring(0, valLength/2);
        var valPart2 = stringVal.substring(valLength/2);
//        println(val+(valPart1.equals(valPart2) ? " YES" : " NO"));
        return  valPart1.equals(valPart2);
    }

    public boolean isValidPart2(long val) {
        String stringVal = String.valueOf(val);
        int valLength = stringVal.length();

        // On teste toutes les longueurs de motif possibles (de 1 à longueur/2)
        for (int patternLength = 1; patternLength <= valLength / 2; patternLength++) {
            // La longueur totale doit être divisible par la longueur du motif
            if (valLength % patternLength == 0) {
                String pattern = stringVal.substring(0, patternLength);
                // Vérifier si le motif répété reconstitue le nombre
                if (pattern.repeat(valLength / patternLength).equals(stringVal)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Data
    public class IdRange {
        long start;
        long end;

        public IdRange(String line) {
            var res = line.split("-");
            start = Long.parseLong(res[0]);
            end = Long.parseLong(res[1]);
        }

        public String toString() {
            return start+"-"+end;
        }
    }
}
