package aoc.days;

import aoc.Day;
import aoc.utils.ReadTxtFile;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

public class Day09 extends Day<Long> {


    public static void main(String[] args) {
        Day09 d = new Day09();
        d.init("/day09.txt");
        d.printResult();
    }

    List<Point2d> data;


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
                var p = new Point2d(line, lineNumber);
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

            for(int i = 0; i < data.size(); i++) {
                var p1 = data.get(i);

                for(int j = i+1; j < data.size(); j++) {
                    var p2 = data.get(j);
                    var rectSize = rectSize(p1, p2);
                    if(rectSize > result) {
                        result = rectSize;

          //              println(p1 + " ** " + p2);
                    }
                }


            }

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1l;
        }
    }

    public long rectSize(Point2d p1, Point2d p2) {
        return (1+(long)Math.abs(p2.x - p1.x)) * (1+(long)Math.abs(p2.y - p1.y));
    }

    public Long part2() {
        try {
            long result = 0;

            PolygonChecker checker = new PolygonChecker(data);

            // Pour chaque paire de points rouges (coins opposés du rectangle)
            for(int i = 0; i < data.size(); i++) {
                var p1 = data.get(i);

                for(int j = i+1; j < data.size(); j++) {
                    var p2 = data.get(j);

                    // Vérifie si le rectangle est entièrement dans le polygone
                    if( checker.containsRectangle(p1, p2)) {
                        var rectSize = rectSize(p1, p2);
                        if(rectSize > result) {
                            result = rectSize;
                        }
                    }
                }
            }

            // 4600181596
            // 4591028022

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1l;
        }
    }

    public class PolygonChecker {

        private final int[] x;
        private final int[] y;
        private final int n;

        /**
         * Construit un vérificateur optimisé pour un polygone fixe
         * Précompute les coordonnées dans des tableaux pour un accès rapide
         */
        public PolygonChecker(List<Point2d> polygon) {
            this.n = polygon.size();
            this.x = new int[n];
            this.y = new int[n];

            for (int i = 0; i < n; i++) {
                Point2d p = polygon.get(i);
                x[i] = p.x;
                y[i] = p.y;
            }
        }


        /**
         * Version COMPLÈTE : teste les coins ET vérifie qu'aucun segment du polygone
         * ne traverse le rectangle
         * Plus lent : O(n²) mais précis à 100%
         *
         * @param corner1 Premier coin du rectangle
         * @param corner2 Coin opposé du rectangle
         * @return true si le rectangle est entièrement dans le polygone
         */
        public boolean containsRectangle(Point2d corner1, Point2d corner2) {
            int minX = Math.min(corner1.x, corner2.x);
            int maxX = Math.max(corner1.x, corner2.x);
            int minY = Math.min(corner1.y, corner2.y);
            int maxY = Math.max(corner1.y, corner2.y);

            // 1. Vérifier que les 4 coins sont dans le polygone
            if (!contains(minX, minY)) return false;
            if (!contains(maxX, minY)) return false;
            if (!contains(maxX, maxY)) return false;
            if (!contains(minX, maxY)) return false;

            // 2. Vérifier qu'aucun segment du polygone ne traverse le rectangle
            int x1 = x[n - 1];
            int y1 = y[n - 1];

            for (int i = 0; i < n; i++) {
                int x2 = x[i];
                int y2 = y[i];

                // Vérifie si le segment (x1,y1)-(x2,y2) traverse l'intérieur du rectangle
                if (segmentCrossesRectangle(x1, y1, x2, y2, minX, maxX, minY, maxY)) {
                    return false;
                }

                x1 = x2;
                y1 = y2;
            }

            return true;
        }

        /**
         * Vérifie si un segment du polygone traverse l'intérieur du rectangle
         */
        private boolean segmentCrossesRectangle(int x1, int y1, int x2, int y2,
                                                int minX, int maxX, int minY, int maxY) {
            // Si les deux points sont entièrement d'un côté du rectangle, pas de traversée
            if ((x1 < minX && x2 < minX) || (x1 > maxX && x2 > maxX) ||
                    (y1 < minY && y2 < minY) || (y1 > maxY && y2 > maxY)) {
                return false;
            }

            // Si un des points est strictement à l'intérieur du rectangle
            // alors un segment du polygone traverse le rectangle
            boolean p1Inside = x1 > minX && x1 < maxX && y1 > minY && y1 < maxY;
            boolean p2Inside = x2 > minX && x2 < maxX && y2 > minY && y2 < maxY;

            if (p1Inside || p2Inside) {
                return true;
            }

            // Vérifie l'intersection avec chaque côté du rectangle
            if (segmentsIntersect(x1, y1, x2, y2, minX, minY, minX, maxY)) return true; // Gauche
            if (segmentsIntersect(x1, y1, x2, y2, maxX, minY, maxX, maxY)) return true; // Droite
            if (segmentsIntersect(x1, y1, x2, y2, minX, minY, maxX, minY)) return true; // Bas
            if (segmentsIntersect(x1, y1, x2, y2, minX, maxY, maxX, maxY)) return true; // Haut

            return false;
        }

        /**
         * Vérifie si deux segments se croisent (produit vectoriel)
         */
        private boolean segmentsIntersect(int x1, int y1, int x2, int y2,
                                          int x3, int y3, int x4, int y4) {
            long d = (long)(x1 - x2) * (y3 - y4) - (long)(y1 - y2) * (x3 - x4);
            if (d == 0) return false; // Segments parallèles

            long t = (long)(x1 - x3) * (y3 - y4) - (long)(y1 - y3) * (x3 - x4);
            long u = (long)(x1 - x3) * (y1 - y2) - (long)(y1 - y3) * (x1 - x2);

            // Vérifie si l'intersection est dans les deux segments [0, 1]
            if (d > 0) {
                return t >= 0 && t <= d && u >= 0 && u <= d;
            } else {
                return t <= 0 && t >= d && u <= 0 && u >= d;
            }
        }

        /**
         * Test ultra-rapide si un point est dans le polygone
         * Optimisé pour des millions d'appels
         */
        public boolean contains(Point2d point) {
            return contains(point.x, point.y);
        }

        public boolean contains(int px, int py) {
            // ÉTAPE 1 : Vérifier si le point est un SOMMET du polygone
            for (int i = 0; i < n; i++) {
                if (x[i] == px && y[i] == py) {
                    return true;
                }
            }

            // ÉTAPE 2 : Vérifier si le point est SUR un SEGMENT du polygone
            int x1 = x[n - 1];
            int y1 = y[n - 1];

            for (int i = 0; i < n; i++) {
                int x2 = x[i];
                int y2 = y[i];

                // Segment horizontal
                if (y1 == y2 && y1 == py) {
                    int minX = Math.min(x1, x2);
                    int maxX = Math.max(x1, x2);
                    if (px >= minX && px <= maxX) {
                        return true;
                    }
                }

                // Segment vertical
                if (x1 == x2 && x1 == px) {
                    int minY = Math.min(y1, y2);
                    int maxY = Math.max(y1, y2);
                    if (py >= minY && py <= maxY) {
                        return true;
                    }
                }

                x1 = x2;
                y1 = y2;
            }

            // ÉTAPE 3 : Ray casting pour les points à l'intérieur
            boolean inside = false;
            x1 = x[n - 1];
            y1 = y[n - 1];

            for (int i = 0; i < n; i++) {
                int x2 = x[i];
                int y2 = y[i];

                if (((y1 > py) != (y2 > py))) {
                    // Multiplication croisée pour éviter la division
                    long dx = x2 - x1;
                    long dy = y2 - y1;
                    long cross = (long)(px - x1) * dy - (long)(py - y1) * dx;

                    if ((dy > 0 && cross < 0) || (dy < 0 && cross > 0)) {
                        inside = !inside;
                    }
                }

                x1 = x2;
                y1 = y2;
            }

            return inside;
        }
    }

    @Data
    public class Point2d {
        public int pos;
        public int x,y;
        public String key;

        public Point2d(String line, int pos) {
            var p = line.split(",");
            x = Integer.parseInt(p[0]);
            y = Integer.parseInt(p[1]);
            this.pos = pos;
            key = ""+pos;
        }

        public String toString() {
            return key+":"+x+","+y;
        }
    }


}
