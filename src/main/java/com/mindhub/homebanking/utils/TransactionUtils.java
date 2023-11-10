package com.mindhub.homebanking.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TransactionUtils {
    public static LocalDateTime dateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        return LocalDateTime.parse(formattedDateTime, formatter);
    }
};
