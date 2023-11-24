package com.taskmanager.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

@UtilityClass
public class DateUtil {
    public String convertLocalDateToString(LocalDate localDate){
        if (Objects.nonNull(localDate)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.DEFAULT_FORMAT_DATE);
            return localDate.format(formatter);
        }
        return "";
    }

    public String convertLocalDateTimeToString(LocalDateTime localDateTime){
        if (Objects.nonNull(localDateTime)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.DEFAULT_FORMAT_DATE_TIME);
            return localDateTime.format(formatter);
        }
        return "";
    }

    public LocalDate convertToLocalDate(String date){
        if (Objects.isNull(date)) return null;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constant.DEFAULT_FORMAT_DATE, Locale.ENGLISH);
        return LocalDate.parse(date, dateTimeFormatter);
    }
}
