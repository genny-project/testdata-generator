package life.genny.datagenerator.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

public class DateUtil {
    private final Random random = new Random();

    /* Pick a random int value from a certain range */
    public int pickRandom(int start, int end) {
        return start + (random.nextInt(end - start));
    }

    /* Convert data type Date into Local Date */
    public LocalDate convertDateIntoLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /* Convert birthdate into age */
    public int turnBirthDateToAge(Date birthDate) {
        Date now = new Date();
        if (birthDate != null) {
            return Period.between(
                    convertDateIntoLocalDate(birthDate),
                    convertDateIntoLocalDate(now)
            ).getYears();
        }

        return 0;
    }

    public static String formatToString(LocalTime time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        return time.format(dtf);
    }

    public static LocalTime parseLocalTime(String time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        return dtf.parse(time, LocalTime::from);
    }

    public static Date localTimeToDate(LocalTime time) {
        Instant instant = time.atDate(LocalDate.of(0, 1, 1)).
                atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

}
