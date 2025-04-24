package ua.shpp.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class CheckNumberGeneratorUtil {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static String currentDate = LocalDate.now().format(DATE_FORMAT);
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static synchronized String generate() {
        String today = LocalDate.now().format(DATE_FORMAT);
        if (!today.equals(currentDate)) {
            currentDate = today;
            counter.set(0);
        }
        int number = counter.incrementAndGet();
        return String.format("%s-%03d", today, number);
    }
}
