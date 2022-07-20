package life.genny.datagenerator.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
    /* Pick a random int value from a certain range */
    public int pickRandom(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    /* Convert data type Date into Local Date */
    public LocalDate convertDateIntoLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /* Convert birthdate into age */
    public int turnBirthDateToAge(Date birthDate) {
        Date now = new Date();
        if (birthDate != null && now != null) {
            return Period.between(
                    convertDateIntoLocalDate(birthDate),
                    convertDateIntoLocalDate(now)
            ).getYears();
        }

        return 0;
    }

}
