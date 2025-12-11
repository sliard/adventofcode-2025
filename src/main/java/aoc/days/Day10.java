package aoc.days;

import aoc.Day;
import aoc.utils.ReadTxtFile;
import lombok.Data;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day10 extends Day<Long> {


    public static void main(String[] args) {
        Day10 d = new Day10();
        d.init("/day10.txt");
        d.printResult();
    }

    List<Machine> data;


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
                var p = new Machine(line, lineNumber);
                data.add(p);
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

            for(Machine m : data) {
                var r = m.findMinimumPress();
//                println("R="+r);
                result += r;
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
            int failedMachines = 0;

            for(Machine m : data) {
                var r = m.findMinimumPressJoltage();
                if(r == -1) {
                    println(">>> Machine " + m.pos + " FAILED <<<");
                    failedMachines++;
                }
                result += Math.max(0, r); // Ne pas ajouter les -1
            }

            println("\n==================");
            println("Total: " + result);
            println("Failed machines: " + failedMachines);
            println("==================");

            return result;
            // 21756
            // 21695
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1l;
        }
    }

    public Pattern pattern = Pattern.compile("\\(([^)]+)\\)");

    @Data
    public class Machine {
        public int pos;
        public String indicatorLight = "";
        public List<List<Integer>> buttonWiring = new ArrayList<>();
        public List<Integer> joltage = new ArrayList<>();

        public Machine(String line, int pos) {
            var p = line.split(" ");
            indicatorLight = p[0].substring(1, p[0].length()-1);

            joltage = Arrays.stream(
                            p[p.length-1].replaceAll("[{}]", "")  // Enlève les accolades
                                    .split(","))              // Split sur les virgules
                    .map(String::trim)             // Trim les espaces éventuels
                    .map(Integer::parseInt)        // Parse en Integer
                    .collect(Collectors.toList());

            this.pos = pos;

            Matcher matcher = pattern.matcher(line);
            buttonWiring = new ArrayList<>();
            while (matcher.find()) {
                List<Integer> numbers = Arrays.stream(matcher.group(1).split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                buttonWiring.add(numbers);
            }
        }

        public List<List<Integer>> findListsContaining(Integer target) {
            return buttonWiring.stream()
                    .filter(list -> list.contains(target))
                    .sorted(Comparator.comparingInt(List::size))
                    .collect(Collectors.toList());
        }

        public Map<String, Long> posCache;

        public long findMinimumPress() {
            String initial = ".".repeat(indicatorLight.length());
            String target = indicatorLight;

            if(initial.equals(target)) return 0;

            Queue<State> queue = new LinkedList<>();
            Set<String> visited = new HashSet<>();

            queue.add(new State(initial, 0));
            visited.add(initial);

            while(!queue.isEmpty()) {
                State current = queue.poll();

                // Essayer chaque bouton
                for(List<Integer> button : buttonWiring) {
                    String newState = toggleLights(current.position, button);

                    if(newState.equals(target)) {
                        return current.presses + 1;
                    }

                    if(!visited.contains(newState)) {
                        visited.add(newState);
                        queue.add(new State(newState, current.presses + 1));
                    }
                }
            }

            return -1; // Pas de solution trouvée
        }

        private String toggleLights(String state, List<Integer> positions) {
            StringBuilder sb = new StringBuilder(state);
            for(int pos : positions) {
                char c = sb.charAt(pos);
                sb.setCharAt(pos, c == '.' ? '#' : '.');
            }
            return sb.toString();
        }

        public long findMinimumPressJoltage() {
            Loader.loadNativeLibraries();

            MPSolver solver = MPSolver.createSolver("SCIP");
            if (solver == null) {
                println("Could not create solver SCIP");
                return -1;
            }

            solver.setTimeLimit(30000);

            int maxValue = joltage.stream().max(Integer::compareTo).orElse(500);
            int upperBound = Math.max(500, maxValue * 3);

            MPVariable[] buttonPresses = new MPVariable[buttonWiring.size()];
            for (int i = 0; i < buttonWiring.size(); i++) {
                buttonPresses[i] = solver.makeIntVar(0, upperBound, "button_" + i);
            }

            for (int counterIdx = 0; counterIdx < joltage.size(); counterIdx++) {
                MPConstraint constraint = solver.makeConstraint(
                        joltage.get(counterIdx),
                        joltage.get(counterIdx)
                );

                for (int buttonIdx = 0; buttonIdx < buttonWiring.size(); buttonIdx++) {
                    if (buttonWiring.get(buttonIdx).contains(counterIdx)) {
                        constraint.setCoefficient(buttonPresses[buttonIdx], 1);
                    }
                }
            }

            MPObjective objective = solver.objective();
            for (MPVariable var : buttonPresses) {
                objective.setCoefficient(var, 1);
            }
            objective.setMinimization();

            MPSolver.ResultStatus resultStatus = solver.solve();

            if (resultStatus == MPSolver.ResultStatus.OPTIMAL ||
                    resultStatus == MPSolver.ResultStatus.FEASIBLE) {

                long result = Math.round(objective.value());

                // DEBUG DÉTAILLÉ pour les 3 premières machines
                if (pos < 3) {
                    println("\n=== Machine " + pos + " DETAILS ===");
                    println("Target joltage: " + joltage);
                    println("Buttons wiring: " + buttonWiring);

                    StringBuilder solution = new StringBuilder("Solution: [");
                    long totalPresses = 0;
                    for (int buttonIdx = 0; buttonIdx < buttonWiring.size(); buttonIdx++) {
                        long presses = Math.round(buttonPresses[buttonIdx].solutionValue());
                        totalPresses += presses;
                        if (buttonIdx > 0) solution.append(", ");
                        solution.append(presses);
                    }
                    solution.append("]");
                    println(solution.toString());
                    println("Total presses: " + totalPresses);

                    // Vérification manuelle
                    int[] verification = new int[joltage.size()];
                    for (int buttonIdx = 0; buttonIdx < buttonWiring.size(); buttonIdx++) {
                        long presses = Math.round(buttonPresses[buttonIdx].solutionValue());
                        for (int counterIdx : buttonWiring.get(buttonIdx)) {
                            verification[counterIdx] += presses;
                        }
                    }
                    println("Achieved: " + Arrays.toString(verification));
                    println("Expected: " + joltage);
                    println("Match: " + Arrays.equals(verification, joltage.stream().mapToInt(i -> i).toArray()));
                    println("========================\n");
                }

                return result;
            } else {
                println("Machine " + pos + " - NO SOLUTION");
                return -1;
            }
        }

        public String toString() {
            return pos+":"+indicatorLight;
        }
    }

    @Data
    private static class State {
        String position;
        int presses;

        State(String position, int presses) {
            this.position = position;
            this.presses = presses;
        }
    }



    @Data
    public static class JoltageState {
        int[] counters;
        int presses;

        public JoltageState(int[] counters, int presses) {
            this.counters = counters.clone();
            this.presses = presses;
        }

        public String key() {
            return Arrays.toString(counters);
        }

        public boolean exceedsTarget(List<Integer> target) {
            for(int i = 0; i < counters.length; i++) {
                if(counters[i] > target.get(i)) {
                    return true;
                }
            }
            return false;
        }

        public boolean matchesTarget(List<Integer> target) {
            for(int i = 0; i < counters.length; i++) {
                if(counters[i] != target.get(i)) {
                    return false;
                }
            }
            return true;
        }
    }
}
