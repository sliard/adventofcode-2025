package aoc.days;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day07Test {

    @ParameterizedTest
    @CsvSource({
            "/day07-01.txt, 21"
    })
    void testPart1(String fileName, Long result) {
        Day07 day = new Day07();
        day.init(fileName);
        assertEquals(result, day.part1());
    }

    @ParameterizedTest
    @CsvSource({
            "/day07-01.txt, 40",
    })
    void testPart2(String fileName, Long result) {
        Day07 day = new Day07();
        day.init(fileName);
        assertEquals(result, day.part2());
    }

}
