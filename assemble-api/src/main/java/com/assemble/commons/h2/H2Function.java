package com.assemble.commons.h2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class H2Function {

    public static String DATE_FORMAT(LocalDate date, String mysqlFormatPattern) {
        if (date == null) {
            return null;
        }
        String dateFormatPattern = mysqlFormatPattern
                .replace("%Y", "yyyy")
                .replace("%m", "MM")
                .replace("%d", "dd");

        return date.format(DateTimeFormatter.ofPattern(dateFormatPattern));
    }
}
