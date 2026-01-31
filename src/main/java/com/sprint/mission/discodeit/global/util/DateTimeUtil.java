package com.sprint.mission.discodeit.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String format(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

        return formatter.format(localDateTime);
    }
}
