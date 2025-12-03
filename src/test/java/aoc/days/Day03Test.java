package aoc.days;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day03Test {

    @ParameterizedTest
    @CsvSource({
            "/day03-01.txt, 357"
    })
    void testPart1(String fileName, Long result) {
        Day03 day = new Day03();
        day.init(fileName);
        assertEquals(result, day.part1());
    }

    @ParameterizedTest
    @CsvSource({
            "/day03-01.txt, 3121910778619",
    })
    void testPart2(String fileName, Long result) {
        Day03 day = new Day03();
        day.init(fileName);
        assertEquals(result, day.part2());
    }

}
