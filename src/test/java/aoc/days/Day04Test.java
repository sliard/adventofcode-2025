package aoc.days;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day04Test {

    @ParameterizedTest
    @CsvSource({
            "/day04-01.txt, 13"
    })
    void testPart1(String fileName, Long result) {
        Day04 day = new Day04();
        day.init(fileName);
        assertEquals(result, day.part1());
    }

    @ParameterizedTest
    @CsvSource({
            "/day04-01.txt, 43",
    })
    void testPart2(String fileName, Long result) {
        Day04 day = new Day04();
        day.init(fileName);
        assertEquals(result, day.part2());
    }

}
