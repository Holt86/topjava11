package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;

/**
 * Created by user on 11.09.2017.
 */
public class LocalDateTimeFormatter {
    public static class LocalDateFormatter implements Formatter<LocalDate>{

        @Override
        public LocalDate parse(String text, Locale locale) throws ParseException {
            return parseLocalDate(text);
        }

        @Override
        public String print(LocalDate object, Locale locale) {
            return DateTimeUtil.toString(object);
        }
    }

    public static class LocalTimeFormatter implements Formatter<LocalTime>{
        @Override
        public LocalTime parse(String text, Locale locale) throws ParseException {
            return parseLocalTime(text);
        }

        @Override
        public String print(LocalTime object, Locale locale) {
            return DateTimeUtil.toString(object);
        }
    }
}
