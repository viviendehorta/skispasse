package com.vdehorta.skispasse.time;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    public static final ZoneId FRANCE_ZONE = ZoneId.of("Europe/Paris");

    //patterns
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm";

    //formatters
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
}
