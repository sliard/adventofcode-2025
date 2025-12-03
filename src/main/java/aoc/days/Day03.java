package aoc.days;

import aoc.Day;
import aoc.utils.ReadTxtFile;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class Day03 extends Day<Long> {

    public static void main(String[] args) {
        Day03 d = new Day03();
        d.init("/day03.txt");
        d.printResult();
    }

    List<Battery> data;

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
                data.add(new Battery(line));
            }
        } catch (Exception ex) {
            println("Read file error ("+args[0]+") : "+ex.getMessage());
        }
    }

    public Long part1() {
        try {
            long result = 0;
            for(Battery battery : data) {
                result += getBestValue(battery);
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1l;
        }
    }

    public Long part2() {
        try {
            long result = 0;
            for(Battery battery : data) {
                result += getBestValue12(battery);
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1l;
        }
    }

    public long getBestValue(Battery battery) {
        int valLength = battery.getLine().length();
        int firstMax = 0;
        int firstMaxI = 0;
        for(int i=0; i<valLength-1; i++) {
            int v = Integer.parseInt(battery.getLine().substring(i, i+1));
            if(v> firstMax) {
                firstMax = v;
                firstMaxI = i;
            }
        }
        int secondMax = 0;
        for(int i=firstMaxI+1; i<valLength; i++) {
            int v = Integer.parseInt(battery.getLine().substring(i, i+1));
            if(v> secondMax) {
                secondMax = v;
            }
        }
        return firstMax*10 + secondMax;
    }

    public long getBestValue12(Battery battery) {
        String line = battery.getLine();
        int inputLength = line.length();
        int nbToSelect = 12;
        StringBuilder result = new StringBuilder();
        int currentPos = 0;

        for (int posInResult = 0; posInResult < nbToSelect; posInResult++) {
            // Combien de chiffres reste-t-il à sélectionner après celui-ci ?
            int remainingToSelect = nbToSelect - posInResult;

            // On peut chercher jusqu'à quelle position ?
            // (il faut garder assez de chiffres après pour finir)
            int maxSearchPos = inputLength - remainingToSelect;

            // Chercher le chiffre maximum dans la plage valide
            int maxDigit = -1;
            int maxDigitPos = currentPos;

            for (int i = currentPos; i <= maxSearchPos; i++) {
                int digit = line.charAt(i) - '0';
                if (digit > maxDigit) {
                    maxDigit = digit;
                    maxDigitPos = i;
                }
            }

            result.append(maxDigit);
            currentPos = maxDigitPos + 1;
        }

        return Long.parseLong(result.toString());
    }

    @Data
    public class Battery {
        String line;

        public Battery(String line) {
            this.line = line;
        }

        public String toString() {
            return line;
        }
    }
}
