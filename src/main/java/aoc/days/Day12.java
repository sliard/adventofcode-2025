package aoc.days;

import aoc.Day;
import aoc.utils.ReadTxtFile;
import lombok.Data;

import java.util.*;

public class Day12 extends Day<Long> {

    public static void main(String[] args) {
        Day12 d = new Day12();
        d.init("/day12.txt");
        d.printResult();
    }

    List<Shape> allShapes = new ArrayList<>();
    List<Space> allSpaces = new ArrayList<>();
    private long maxIterations = 50_000_000; // Limite d'itérations
    private long iterations;

    public void init(String ...args) {
        if(args == null || args.length == 0) {
            println("No args");
            return;
        }

        int lineNumber = 0;
        try {
            List<String> allLines = ReadTxtFile.readFileAsStringList(args[0]);
            Shape currentShape = new Shape(0);
            int shapeIndex = 0;

            for(String line : allLines) {
                int nbShapes = allShapes.size();
                if(nbShapes < 6) {
                    if(line.isEmpty()) {
                        allShapes.add(currentShape);
                        currentShape = new Shape(0);
                        shapeIndex = 0;
                        continue;
                    }
                    if(shapeIndex == 0) {
                        currentShape.setPos(nbShapes);
                        shapeIndex++;
                    } else {
                        currentShape.addLine(line, shapeIndex-1);
                        shapeIndex++;
                    }
                } else {
                    allSpaces.add(new Space(line));
                }
                lineNumber++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            println(lineNumber + " : Read file error ("+args[0]+") : "+ex.getMessage());
        }

        for(Shape shape : allShapes) {
            shape.generateVariants();
        }
    }

    public Long part1() {
        try {
            long result = 0;
            int spaceNum = 0;

            for(Space space : allSpaces) {
                spaceNum++;
                iterations = 0;
                boolean canFit = canPlaceAllShapesInSpace(space);

                if(canFit) {
                    result++;
                    println("✓ Space " + spaceNum + " (" + space.maxX + "x" + space.maxY +
                            ") - iterations: " + iterations);
                } else {
                    if(iterations >= maxIterations) {
                        println("✗ Space " + spaceNum + " TIMEOUT (" + space.maxX + "x" + space.maxY + ")");
                    }
                }

                if(spaceNum % 100 == 0) {
                    println("Processed " + spaceNum + "/" + allSpaces.size() +
                            " spaces. Valid: " + result);
                }
            }

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1L;
        }
    }

    public Long part2() {
        return 0L;
    }

    private boolean canPlaceAllShapesInSpace(Space space) {
        boolean[][] grid = new boolean[space.maxY][space.maxX];

        // Créer liste des formes triées
        List<ShapeGroup> groups = new ArrayList<>();
        for(int i = 0; i < space.nbShapes.size(); i++) {
            int count = space.nbShapes.get(i);
            if(count > 0) {
                groups.add(new ShapeGroup(allShapes.get(i), count));
            }
        }

        // Trier : placer d'abord les formes avec le plus d'exemplaires
        groups.sort((a, b) -> Integer.compare(b.count, a.count));

        // Vérification aire
        int totalCells = 0;
        for(ShapeGroup g : groups) {
            totalCells += g.shape.cellCount * g.count;
        }
        if(totalCells > space.maxX * space.maxY) {
            return false;
        }

        return solve(grid, groups, 0, 0);
    }

    private boolean solve(boolean[][] grid, List<ShapeGroup> groups, int groupIdx, int instanceIdx) {
        iterations++;
        if(iterations > maxIterations) {
            return false;
        }

        if(groupIdx >= groups.size()) {
            return true;
        }

        ShapeGroup group = groups.get(groupIdx);

        if(instanceIdx >= group.count) {
            return solve(grid, groups, groupIdx + 1, 0);
        }

        // Trouver la première cellule vide
        int startPos = findFirstEmpty(grid);
        if(startPos == -1) {
            return false; // Pas de place
        }

        int cols = grid[0].length;
        int rows = grid.length;

        // Essayer de placer à partir de startPos
        for(int pos = startPos; pos < rows * cols; pos++) {
            int y = pos / cols;
            int x = pos % cols;

            if(grid[y][x]) continue; // Déjà occupé

            for(ShapeVariant variant : group.shape.variants) {
                if(canPlace(grid, variant, x, y)) {
                    place(grid, variant, x, y, true);

                    if(solve(grid, groups, groupIdx, instanceIdx + 1)) {
                        return true;
                    }

                    place(grid, variant, x, y, false);
                }
            }
        }

        return false;
    }

    private int findFirstEmpty(boolean[][] grid) {
        for(int y = 0; y < grid.length; y++) {
            for(int x = 0; x < grid[0].length; x++) {
                if(!grid[y][x]) {
                    return y * grid[0].length + x;
                }
            }
        }
        return -1;
    }

    private boolean canPlace(boolean[][] grid, ShapeVariant variant, int startX, int startY) {
        for(Point p : variant.points) {
            int x = startX + p.x;
            int y = startY + p.y;

            if(x < 0 || x >= grid[0].length || y < 0 || y >= grid.length) {
                return false;
            }

            if(grid[y][x]) {
                return false;
            }
        }
        return true;
    }

    private void place(boolean[][] grid, ShapeVariant variant, int startX, int startY, boolean value) {
        for(Point p : variant.points) {
            grid[startY + p.y][startX + p.x] = value;
        }
    }

    @Data
    class ShapeGroup {
        Shape shape;
        int count;

        ShapeGroup(Shape s, int c) {
            this.shape = s;
            this.count = c;
        }
    }

    @Data
    class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(!(o instanceof Point)) return false;
            Point p = (Point) o;
            return x == p.x && y == p.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    @Data
    class ShapeVariant {
        List<Point> points;

        ShapeVariant(List<Point> points) {
            this.points = new ArrayList<>(points);
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(!(o instanceof ShapeVariant)) return false;
            ShapeVariant sv = (ShapeVariant) o;
            return new HashSet<>(points).equals(new HashSet<>(sv.points));
        }

        @Override
        public int hashCode() {
            return new HashSet<>(points).hashCode();
        }
    }

    @Data
    public class Shape {
        public int[][] allData;
        public int pos;
        public List<ShapeVariant> variants;
        public int cellCount;

        public Shape(int pos) {
            this.pos = pos;
            allData = new int[3][3];
            variants = new ArrayList<>();
            cellCount = 0;
        }

        public void addLine(String line, int y) {
            for(int i = 0; i < 3; i++) {
                allData[y][i] = line.charAt(i) == '#' ? 1 : 0;
                if(allData[y][i] == 1) cellCount++;
            }
        }

        public void generateVariants() {
            Set<ShapeVariant> uniqueVariants = new HashSet<>();
            int[][] current = copyArray(allData);

            for(int rot = 0; rot < 4; rot++) {
                uniqueVariants.add(createVariant(current));
                int[][] flipped = flipHorizontal(current);
                uniqueVariants.add(createVariant(flipped));
                current = rotate90(current);
            }

            variants = new ArrayList<>(uniqueVariants);
        }

        private ShapeVariant createVariant(int[][] data) {
            List<Point> points = new ArrayList<>();
            for(int y = 0; y < 3; y++) {
                for(int x = 0; x < 3; x++) {
                    if(data[y][x] == 1) {
                        points.add(new Point(x, y));
                    }
                }
            }
            return new ShapeVariant(points);
        }

        private int[][] rotate90(int[][] data) {
            int[][] result = new int[3][3];
            for(int y = 0; y < 3; y++) {
                for(int x = 0; x < 3; x++) {
                    result[x][2-y] = data[y][x];
                }
            }
            return result;
        }

        private int[][] flipHorizontal(int[][] data) {
            int[][] result = new int[3][3];
            for(int y = 0; y < 3; y++) {
                for(int x = 0; x < 3; x++) {
                    result[y][2-x] = data[y][x];
                }
            }
            return result;
        }

        private int[][] copyArray(int[][] data) {
            int[][] result = new int[3][3];
            for(int i = 0; i < 3; i++) {
                System.arraycopy(data[i], 0, result[i], 0, 3);
            }
            return result;
        }
    }

    @Data
    public class Space {
        public int maxX = 0;
        public int maxY = 0;
        public List<Integer> nbShapes;

        public Space(String line) {
            var ls = line.split(":");
            var sizeData = ls[0].split("x");
            maxX = Integer.parseInt(sizeData[0]);
            maxY = Integer.parseInt(sizeData[1]);

            nbShapes = new ArrayList<>();
            var shapeData = ls[1].trim().split(" ");
            for(int i = 0; i < shapeData.length; i++) {
                nbShapes.add(Integer.parseInt(shapeData[i]));
            }
        }
    }
}