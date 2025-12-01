package aoc.days;

import aoc.Day;
import aoc.utils.ReadTxtFile;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class Day01 extends Day<Integer> {

    public static void main(String[] args) {
        Day01 d = new Day01();
        d.init("/day01.txt");
        d.printResult();
    }

    List<Rotation> data;

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
                data.add(new Rotation(line));
            }
        } catch (Exception ex) {
            println("Read file error ("+args[0]+") : "+ex.getMessage());
        }
    }

    public Integer part1() {
        try {
            int result = 0;
            int current = 50;

            for (Rotation rotation : data) {
                current = (current+rotation.getAddDistance()+100)%100;
                println("c="+current);
                if(current == 0) {
                    result++;
                }
            }
            return result;
        } catch (Exception ex) {
            return -1;
        }
    }

    public Integer part2() {
        try {
            int result = 0;
            int current = 50;

            for (Rotation rotation : data) {
                int delta = rotation.getAddDistance();
                int target = current + delta;
                int passages = 0;

                // Compter les passages par 0
                if (delta >= 0) {
                    // Rotation à droite : compter les multiples de 100 atteints
                    passages = target / 100;
                } else {
                    // Rotation à gauche
                    // CAS CRITIQUE : si on part de 0, on compte quand même les retours à 0 !
                    if (current == 0) {
                        // Partir de 0 vers la gauche : on compte les tours complets
                        passages = Math.abs(delta) / 100;
                    } else if (target <= 0) {
                        // On traverse 0 en descendant (target peut être 0 ou négatif)
                        // 1er passage + tours complets supplémentaires
                        passages = 1 + Math.abs(target) / 100;
                    }
                    // Si current > 0 et target > 0, on reste positif, passages = 0
                }

                result += passages;
                current = (target % 100 + 100) % 100;

                println(rotation + " -> pos=" + current + " (target=" + target + "), passages=" + passages + ", total=" + result);
            }

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Data
    public class Rotation {
        char sense;
        int distance;

        public int getAddDistance() {
            if(sense == 'L') {
                return -distance;
            }
            return distance;
        }

        public Rotation(String line) {
            sense = line.charAt(0);
            distance = Integer.parseInt(line.substring(1));
        }

        public String toString() {
            return sense+""+distance;
        }
    }
}
