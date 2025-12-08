package aoc.days;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day08Test {

    @ParameterizedTest
    @CsvSource({
            "/day08-01.txt, 40"
    })
    void testPart1(String fileName, Long result) {
        Day08 day = new Day08();
        day.limiteLink = 10;
        day.init(fileName);
        assertEquals(result, day.part1());
    }

    @ParameterizedTest
    @CsvSource({
            "/day08-01.txt, 25272",
    })
    void testPart2(String fileName, Long result) {
        Day08 day = new Day08();
        day.init(fileName);
        assertEquals(result, day.part2());
    }

}
