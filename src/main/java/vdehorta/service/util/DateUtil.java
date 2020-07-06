package vdehorta.service.util;

import java.time.format.DateTimeFormatter;

/**
 * Utility class for generating random Strings.
 */
public final class DateUtil {

    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");

    private DateUtil() {
    }
}
