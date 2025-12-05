package aoc.days;

import aoc.Day;
import aoc.utils.ReadTxtFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day05 extends Day<Long> {

    public static void main(String[] args) {
        Day05 d = new Day05();
        d.init("/day05.txt");
        d.printResult();
    }

    List<Range> data;
    List<Range> realRange;
    List<Long> ingredientIDs;

    public void init(String ...args) {
        // init stuff
        if(args == null || args.length == 0) {
            println("No args");
            return;
        }
        data = new ArrayList<>();
        ingredientIDs = new ArrayList<>();
        int lineNumber = 0;
        try {
            List<String> allLines = ReadTxtFile.readFileAsStringList(args[0]);
            boolean range = true;
            for(String line : allLines) {
                lineNumber++;
                if(line.trim().isEmpty()) {
                    range = false;
                    continue;
                }

                if(range) {
                    var lineData = line.split("-");
                    var start = Long.parseLong(lineData[0]);
                    var end = Long.parseLong(lineData[1]);
                    data.add(new Range(start, end));
                } else {
                    ingredientIDs.add(Long.parseLong(line));
                }
            }

            realRange = mergeRanges(data);
        } catch (Exception ex) {
            ex.printStackTrace();
            println(lineNumber + " : Read file error ("+args[0]+") : "+ex.getMessage());
        }
    }

    public static List<Range> mergeRanges(List<Range> ranges) {
        if (ranges == null || ranges.isEmpty()) {
            return new ArrayList<>();
        }

        // 1. Trier les ranges par leur début
        List<Range> sorted = new ArrayList<>(ranges);
        sorted.sort(Comparator.comparingLong(r -> r.start));

        // 2. Fusionner les ranges qui se chevauchent
        List<Range> merged = new ArrayList<>();
        Range current = sorted.get(0);

        for (int i = 1; i < sorted.size(); i++) {
            Range next = sorted.get(i);

            // Si les ranges se chevauchent ou sont adjacents
            if (next.start <= current.end + 1) {
                // Fusionner : étendre le range courant
                current.end = Math.max(current.end, next.end);
            } else {
                // Pas de chevauchement : ajouter le range courant et passer au suivant
                merged.add(current);
                current = next;
            }
        }

        // Ajouter le dernier range
        merged.add(current);

        return merged;
    }

    public Long part1() {
        try {

            long result = 0;

            for(long oneId : ingredientIDs) {
                boolean isFresh = false;

                for(var range : realRange) {
                    if(range.start <= oneId && oneId <= range.end) {
                        isFresh = true;
                    }
                    if(oneId < range.end) {
                        break;
                    }
                }
                result +=  isFresh ? 1 : 0;
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
            for(var range : realRange) {
                result+= range.end - range.start + 1;
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1l;
        }
    }

    public class Range {
        public long start;
        public long end;

        public Range(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return start+"-"+end;
        }
    }
}
