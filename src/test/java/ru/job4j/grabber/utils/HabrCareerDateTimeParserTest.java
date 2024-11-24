package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.*;

class HabrCareerDateTimeParserTest {

    @Test
    public void whenParseIsTrue() {
        String input = "2024-11-05T12:17:09+03:00";
        LocalDateTime excepted = LocalDateTime.parse("2024-11-05T12:17:09", DateTimeFormatter.ISO_DATE_TIME);
        HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
        assertThat(parser.parse(input)).isEqualTo(excepted);
    }

    @Test
    public void whenParseThenException() {
        String input = "2024-11-05T12:17:09";
        assertThatThrownBy(() -> {
            HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
            parser.parse(input);
        }).isInstanceOf(DateTimeParseException.class);
    }
}