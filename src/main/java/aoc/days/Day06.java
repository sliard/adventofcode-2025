package aoc.days;

import aoc.Day;
import aoc.utils.ReadTxtFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day06 extends Day<Long> {

    public static void main(String[] args) {
        Day06 d = new Day06();
        d.init("/day06.txt");
        d.printResult();
    }

    List<List<Long>> data;
    List<String> operator;

    List<String> rowData;

    public void init(String ...args) {
        // init stuff
        if(args == null || args.length == 0) {
            println("No args");
            return;
        }
        data = new ArrayList<>();
        operator = new ArrayList<>();
        rowData = new ArrayList<>();

        int lineNumber = 0;
        try {
            List<String> allLines = ReadTxtFile.readFileAsStringList(args[0]);

            int nbLine = allLines.size();

            for(String line : allLines) {
                rowData.add(line);
                if(lineNumber == nbLine - 1) {
                    Scanner lineScanner = new Scanner(line);
                    while (lineScanner.hasNext()) {
                        operator.add(lineScanner.next());
                    }
                    break;
                }

                if (!line.isEmpty()) {
                    List<Long> row = new ArrayList<>();
                    Scanner lineScanner = new Scanner(line);
                    while (lineScanner.hasNextLong()) {
                        row.add(lineScanner.nextLong());
                    }
                    data.add(row);
                }
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

            int nbRow = data.get(0).size();

            for(int i = 0; i < nbRow; i++) {
                long rowValue = data.get(0).get(i);
                String op =  operator.get(i);
                switch (op) {
                    case "+":
                        for(int j = 1; j < data.size(); j++) {
                            rowValue +=  data.get(j).get(i);
                        }
                        break;
                    case "*":
                        for(int j = 1; j < data.size(); j++) {
                            rowValue *=  data.get(j).get(i);
                        }
                        break;
                }
                result += rowValue;
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

            int nbRow = rowData.get(0).length();

            long currentRow = 0;
            long currentPart = -1;
            char op = 'a';
            for(int i = 0; i < nbRow; i++) {

                currentRow = 0;
                boolean emptyRow = true;
                for(int j = 0; j < rowData.size(); j++) {
                    char c = rowData.get(j).charAt(i);
                    emptyRow &= c == ' ';
                    boolean isDigit = Character.isDigit(c);
                    if (isDigit) {
                        currentRow = currentRow*10 + (c - '0');
                    } else if (c == '+') {
                        op = c;
                        if(currentPart < 0) {
                            currentPart = 0;
                        }
                    } else if (c == '*') {
                        op = c;
                        if(currentPart < 0) {
                            currentPart = 1;
                        }
                    }
                }

                if(emptyRow) {
                    result +=  currentPart;
                    currentPart = -1;
                } else if (op == '+') {
                    currentPart += currentRow;
                } else {
                    currentPart *= currentRow;
                }
            }
            result +=  currentPart;
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1l;
        }
    }

}
