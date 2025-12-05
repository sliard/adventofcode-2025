package aoc.days;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day05Test {

    @ParameterizedTest
    @CsvSource({
            "/day05-01.txt, 3"
    })
    void testPart1(String fileName, Long result) {
        Day05 day = new Day05();
        day.init(fileName);
        assertEquals(result, day.part1());
    }

    @ParameterizedTest
    @CsvSource({
            "/day05-01.txt, 14",
    })
    void testPart2(String fileName, Long result) {
        Day05 day = new Day05();
        day.init(fileName);
        assertEquals(result, day.part2());
    }

}
