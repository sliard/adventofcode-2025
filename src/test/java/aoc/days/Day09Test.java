package aoc.days;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day09Test {

    @ParameterizedTest
    @CsvSource({
            "/day09-01.txt, 50"
    })
    void testPart1(String fileName, Long result) {
        Day09 day = new Day09();
        day.init(fileName);
        assertEquals(result, day.part1());
    }

    @ParameterizedTest
    @CsvSource({
            "/day09-01.txt, 24",
    })
    void testPart2(String fileName, Long result) {
        Day09 day = new Day09();
        day.init(fileName);
        assertEquals(result, day.part2());
    }

}
