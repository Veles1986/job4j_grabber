package ru.job4j.grabber.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.format.DateTimeParseException;

class HabrCareerDateTimeParserTest {
    @Test
    public void whenParseIsTrue() {
        String input = "2024-11-05T12:17:09+03:00";
        long excepted = 1730791029000L;
        HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
        Assertions.assertThat(parser.parse(input)).isEqualTo(excepted);
    }

    @Test
    public void whenParseThenException() {
        String input = "2024-11-05T12:17:09";
        Assertions.assertThatThrownBy(() -> {
            HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
            parser.parse(input);
        }).isInstanceOf(DateTimeParseException.class);
    }
}