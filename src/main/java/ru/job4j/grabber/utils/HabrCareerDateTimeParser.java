package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabrCareerDateTimeParser implements DateTimeParser {

    @Override
    public LocalDateTime parse(String parse) {
        return LocalDateTime.parse(
                LocalDateTime.parse(
                        parse,
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME
                ).format(DateTimeFormatter.ISO_DATE_TIME),
                DateTimeFormatter.ISO_DATE_TIME
        );
    }

}