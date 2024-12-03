package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class HabrCareerDateTimeParser implements DateTimeParser {

    @Override
    public long parse(String parse) {
        return LocalDateTime.parse(
                parse,
                DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}