package vdehorta.service.util;

import java.time.format.DateTimeFormatter;

/**
 * Utility class for generating random Strings.
 */
public final class DateUtil {

    public static DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateUtil() {
    }
}
