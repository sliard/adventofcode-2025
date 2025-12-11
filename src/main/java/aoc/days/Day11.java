package aoc.days;

import aoc.Day;
import aoc.utils.ReadTxtFile;
import lombok.Data;

import java.util.*;

public class Day11 extends Day<Long> {


    public static void main(String[] args) {
        Day11 d = new Day11();
        d.init("/day11.txt");
        d.printResult();
    }

    Map<String, Node> data;


    public void init(String ...args) {
        // init stuff
        if(args == null || args.length == 0) {
            println("No args");
            return;
        }
        data = new HashMap<>();

        int lineNumber = 0;
        try {
            List<String> allLines = ReadTxtFile.readFileAsStringList(args[0]);
            for(String line : allLines) {
                var p = line.split(":");
                var nodeName = p[0].trim();
                var n = data.getOrDefault(nodeName, new Node(nodeName));
                data.put(nodeName, n);
                var children = p[1].trim().split(" ");
                for(String child : children) {
                    var c = data.getOrDefault(child, new Node(child));
                    n.children.add(c);
                    data.put(child, c);

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

            var start = data.get("you");
            var end = data.get("out");

            return countNbWayBetween(start, end);
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1l;
        }
    }

    public long countNbWayBetween(Node start, Node end) {
        return countPaths(start, end, new java.util.HashSet<>());
    }

    private long countPaths(Node current, Node target, java.util.Set<String> visited) {
        // Si on a atteint la destination, c'est un chemin valide
        if (current.name.equals(target.name)) {
            return 1;
        }

        // Marquer le noeud actuel comme visité dans ce chemin
        visited.add(current.name);

        long totalPaths = 0;

        // Explorer tous les enfants du noeud actuel
        for (Node child : current.children) {
            // Ne pas revisiter un noeud déjà dans le chemin actuel (évite les cycles)
            if (!visited.contains(child.name)) {
                totalPaths += countPaths(child, target, visited);
            }
        }

        // Backtracking : retirer le noeud de la liste des visités
        // pour permettre à d'autres chemins de le visiter
        visited.remove(current.name);

        return totalPaths;
    }


    public Long part2() {
        try {
            var start = data.get("svr");
            var end = data.get("out");
            var dac = data.get("dac");
            var fft = data.get("fft");

            println("Calcul avec DP optimisée...");

            // DP bottom-up : calculer pour chaque noeud le nombre de chemins vers 'out'
            // États: 0=aucun, 1=dac seul, 2=fft seul, 3=dac+fft
            Map<String, long[]> dp = new HashMap<>();

            // Calculer la DP pour le noeud de départ
            calculateDPBottomUp(start, end, dac, fft, dp, new HashSet<>());

            long[] startDP = dp.get(start.name);
            long result = startDP[3]; // Chemins qui passent par dac ET fft

            println("Nombre de chemins trouvés: " + result);

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1L;
        }
    }

    private long[] calculateDPBottomUp(Node current, Node target,
                                       Node dac, Node fft,
                                       Map<String, long[]> dp,
                                       Set<String> inProgress) {

        // Si déjà calculé, retourner
        if (dp.containsKey(current.name)) {
            return dp.get(current.name);
        }

        // Détection de cycle (ne devrait pas arriver si DAG)
        if (inProgress.contains(current.name)) {
            return new long[4];
        }

        inProgress.add(current.name);

        // Initialiser le tableau DP
        long[] counts = new long[4];

        // Cas de base : le noeud cible
        if (current.name.equals(target.name)) {
            counts[0] = 1; // Un chemin sans visiter dac ni fft (juste arriver à out)
        } else {
            // Calculer récursivement pour tous les enfants
            for (Node child : current.children) {
                long[] childCounts = calculateDPBottomUp(child, target, dac, fft, dp, inProgress);

                // Additionner les chemins de chaque enfant
                for (int i = 0; i < 4; i++) {
                    counts[i] += childCounts[i];
                }
            }
        }

        // Transformation des états si on est sur dac ou fft
        // On crée un nouveau tableau pour les états après avoir visité ce noeud
        long[] newCounts = new long[4];

        if (current.name.equals(dac.name)) {
            // Arriver à dac : mettre à jour les états
            newCounts[0] = 0;              // Impossible de ne pas avoir visité dac si on est à dac
            newCounts[1] = counts[0] + counts[1]; // aucun ou dac → dac
            newCounts[2] = 0;              // Impossible d'avoir seulement fft si on est à dac
            newCounts[3] = counts[2] + counts[3]; // fft ou both → both
        } else if (current.name.equals(fft.name)) {
            // Arriver à fft : mettre à jour les états
            newCounts[0] = 0;              // Impossible de ne pas avoir visité fft si on est à fft
            newCounts[1] = 0;              // Impossible d'avoir seulement dac si on est à fft
            newCounts[2] = counts[0] + counts[2]; // aucun ou fft → fft
            newCounts[3] = counts[1] + counts[3]; // dac ou both → both
        } else {
            // noeud normal : pas de transformation
            newCounts = counts;
        }

        dp.put(current.name, newCounts);
        inProgress.remove(current.name);

        return newCounts;
    }

    public String printVisited(java.util.Set<String> visited) {
        StringBuilder sb = new StringBuilder();
        for(String n : visited) {
            sb.append(n).append(",");
        }
        return sb.toString();
    }

    @Data
    public class Node {
        public String name;
        public List<Node> children;

        public Node(String name) {
            this.name = name;
            children = new ArrayList<>();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(name).append(": ");
            for(Node child : children) {
                sb.append(child.name).append(" ");
            }
            return sb.toString();
        }
    }


}
