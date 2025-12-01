package aoc;

/**
 * Interface for all days.
 * We use a parameter type because all days do not return the same type.
 * @param <T>
 */
public abstract class Day<T> {

    public boolean debug = false;

    public abstract void init(String ...args);

    public abstract T part1();

    public abstract T part2();

    public void printResult() {
        println(this.getClass().getName() + " : Result part1="+part1());
        println(this.getClass().getName() + " : Result part2="+part2());
    }

    public void trace(String line) {
        if(debug) {
            System.out.println(line);
        }
    }

    public void println(String line) {
        System.out.println(line);
    }

}
