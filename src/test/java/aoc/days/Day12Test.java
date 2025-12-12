package aoc.days;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day12Test {

    @ParameterizedTest
    @CsvSource({
            "/day12-01.txt, 2"
    })
    void testPart1(String fileName, Long result) {
        Day12 day = new Day12();
        day.init(fileName);
        assertEquals(result, day.part1());
    }

    @ParameterizedTest
    @CsvSource({
            "/day12-02.txt, 2",
    })
    void testPart2(String fileName, Long result) {
        Day12 day = new Day12();
        day.init(fileName);
        assertEquals(result, day.part2());
    }

}
