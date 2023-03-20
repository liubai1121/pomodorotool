package com.tool.pomodoro.technique.tool.controller.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TypeConversionUtil {
    private TypeConversionUtil() {
    }

    public static Optional<LocalDate> toDate(String dateStr) {
        try {
            DateTimeFormatter isoLocalDate = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate date = LocalDate.from(isoLocalDate.parse(dateStr));
            return Optional.of(date);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<Integer> toInteger(String intStr) {
        try {
            return Optional.of(Integer.parseInt(intStr));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
