package aoc.days;

import aoc.Day;
import aoc.utils.ReadTxtFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

public class Day08 extends Day<Long> {

    public int limiteLink = 1000;

    public static void main(String[] args) {
        Day08 d = new Day08();
        d.init("/day08.txt");
        d.printResult();
    }

    List<Point3d> data;
    Map<String, Point3d> allPoint = new HashMap<>();


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
                var p = new Point3d(line, lineNumber);
                allPoint.put(p.key,p);
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
            Map<String, Double> allDistances = new HashMap<>();

            for(int i = 0; i < data.size(); i++) {
                for(int j = i+1; j < data.size(); j++) {
                    var d = straightLineDistance(data.get(i), data.get(j));
                    allDistances.put(coupleKey(data.get(i), data.get(j)), d);
                }
            }

            Map<String, Double> sortedMap = allDistances.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            List<Set<Integer>> allGroup = new ArrayList<>();

            int nbPairs = 0;
            for(var  entry : sortedMap.entrySet()) {
                if (nbPairs >= limiteLink) {  // 1000 pour input, 10 pour exemple
                    break;
                }
                nbPairs++;

                var l = new Line(entry.getKey());
                int groupAId = -1;
                int groupBId = -1;
                for(int i=0; i<allGroup.size() && groupAId<0; i++) {
                    var g =  allGroup.get(i);
                    if(g.contains(l.a.pos)) {
                        groupAId = i;
                    }
                }
                for(int i=0; i<allGroup.size() && groupBId<0; i++) {
                    var g =  allGroup.get(i);
                    if(g.contains(l.b.pos)) {
                        groupBId = i;
                    }
                }
                boolean connected = false;  // Flag pour savoir si on a connecté

                if(groupAId>=0 && groupBId>=0) {
                    // Fusion
                    if(groupAId != groupBId) {

                        int minId = Math.min(groupAId, groupBId);
                        int maxId = Math.max(groupAId, groupBId);

                        var gMin = allGroup.get(minId);
                        var gMax = allGroup.get(maxId);
                        gMin.addAll(gMax);
                        allGroup.remove(maxId);  // ✅ Supprimer le plus grand index
                        connected = true;
                    }
                } else if(groupAId>=0) {
                    var ga =  allGroup.get(groupAId);
                    ga.add(l.b.pos);
                    connected = true;
                } else if(groupBId>=0) {
                    var gb =  allGroup.get(groupBId);
                    gb.add(l.a.pos);
                    connected = true;
                } else {
                    Set<Integer> gs = new HashSet<>();
                    gs.add(l.b.pos);
                    gs.add(l.a.pos);
                    allGroup.add(gs);
                    connected = true;
                }
            }
            result = allGroup.stream()
                    .map(Set::size)
                    .sorted(Comparator.reverseOrder())
                    .limit(3)
                    .reduce(1, (a, b) -> a * b);

            // 201903000 is too high
            // 1000 is too low
            // 21897
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1l;
        }
    }

    public Long part2() {
        try {
            long result = 0;
            Map<String, Double> allDistances = new HashMap<>();

            for(int i = 0; i < data.size(); i++) {
                for(int j = i+1; j < data.size(); j++) {
                    var d = straightLineDistance(data.get(i), data.get(j));
                    allDistances.put(coupleKey(data.get(i), data.get(j)), d);
                }
            }

            Map<String, Double> sortedMap = allDistances.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            List<Set<Integer>> allGroup = new ArrayList<>();

            Point3d lastA = null;
            Point3d lastB = null;


            int nbPairs = 0;
            for(var  entry : sortedMap.entrySet()) {
                if (nbPairs >= limiteLink) {  // 1000 pour input, 10 pour exemple
     //               break;
                }
                nbPairs++;

                var l = new Line(entry.getKey());
                int groupAId = -1;
                int groupBId = -1;
                for(int i=0; i<allGroup.size() && groupAId<0; i++) {
                    var g =  allGroup.get(i);
                    if(g.contains(l.a.pos)) {
                        groupAId = i;
                    }
                }
                for(int i=0; i<allGroup.size() && groupBId<0; i++) {
                    var g =  allGroup.get(i);
                    if(g.contains(l.b.pos)) {
                        groupBId = i;
                    }
                }
                boolean connected = false;  // Flag pour savoir si on a connecté

                if(groupAId>=0 && groupBId>=0) {
                    // Fusion
                    if(groupAId != groupBId) {

                        int minId = Math.min(groupAId, groupBId);
                        int maxId = Math.max(groupAId, groupBId);

                        var gMin = allGroup.get(minId);
                        var gMax = allGroup.get(maxId);
                        gMin.addAll(gMax);
                        allGroup.remove(maxId);  // ✅ Supprimer le plus grand index
                        connected = true;
                    }
                } else if(groupAId>=0) {
                    var ga =  allGroup.get(groupAId);
                    ga.add(l.b.pos);
                    connected = true;
                } else if(groupBId>=0) {
                    var gb =  allGroup.get(groupBId);
                    gb.add(l.a.pos);
                    connected = true;
                } else {
                    Set<Integer> gs = new HashSet<>();
                    gs.add(l.b.pos);
                    gs.add(l.a.pos);
                    allGroup.add(gs);
                    connected = true;
                }

                if(connected) {
                    lastA = l.a;
                    lastB = l.b;
                }
            }
            result = (long)lastA.x * (long)lastB.x;

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1l;
        }    }

    public int straightLineDistanceTest(Point3d p1, Point3d p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y) + Math.abs(p1.z - p2.z);
    }
    public double straightLineDistance(Point3d p1, Point3d p2) {
        long dx = (long)p1.x - (long)p2.x;
        long dy = (long)p1.y - (long)p2.y;
        long dz = (long)p1.z - (long)p2.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public String coupleKey(Point3d p1, Point3d p2) {
        return p1.key+"-"+p2.key;
    }


    public class Line {
        public Point3d a;
        public Point3d b;
        public Line(String key) {
            var p = key.split("-");
            a = allPoint.get(p[0]);
            b = allPoint.get(p[1]);
        }
        public String toString() {
            return "("+a+")-("+b+")";
        }
    }

    @Data
    public class Point3d {
        public int pos;
        public int x,y,z;
        public String key;

        public Point3d(String line, int pos) {
            var p = line.split(",");
            x = Integer.parseInt(p[0]);
            y = Integer.parseInt(p[1]);
            z = Integer.parseInt(p[2]);
            this.pos = pos;
            key = ""+pos;
        }

        public String toString() {
            return key+":"+x+","+y+":"+z;
        }
    }


}
