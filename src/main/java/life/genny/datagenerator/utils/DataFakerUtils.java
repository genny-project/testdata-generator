package life.genny.datagenerator.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.mifmif.common.regex.Generex;

import life.genny.datagenerator.Regex;

/**
 * A static utility class used for standard operations
 * involving generate simple data especially using Regex.
 * 
 * @author Amrizal Fajar
 */
public class DataFakerUtils {
    
    @Inject
    static Logger log;

    /**
     * Initialize {@link Random} class
     * 
     * @return {@link Random}
     */
    private static Random random() {
        return new Random();
    }

    /**
     * Preprocessing the Regex pattern before going to use to generate data
     * 
     * @param regex    The regex pattern
     * @param newRegex The new regex pattern that's going to implement
     * @return Modified regex pattern
     */
    private static String regexPreprocessing(String regex, String newRegex) {
        String regexTemp = regex.replace(".*", newRegex + Regex.ASTERISK);
        regexTemp = regexTemp.replace(".+", newRegex + Regex.PLUS);
        return regexTemp;
    }

    /**
     * Cleaning the value which generated from the regex
     * 
     * @param result The generated value
     * @return Modified value
     */
    private static String regexPostProcessing(String result) {
        return result.replaceAll("[$^]*", "");
    }

    /**
     * Generate random {@link String} from regex pattern
     * 
     * @param regex The regex pattern
     * @return The generated value
     */
    public static String randStringFromRegex(String regex) {
        String newRegex = DataFakerUtils.regexPreprocessing(regex, Regex.WORD_CHARS);
        Generex generator = new Generex(newRegex);
        String randString = generator.random();
        randString = DataFakerUtils.regexPostProcessing(randString);
        return randString;
    }

    /**
     * Generate random {@link Integer} from regex pattern
     * 
     * @param regex The regex pattern
     * @return The generated value
     */
    public static int randIntFromRegex(String regex) {
        String newRegex = DataFakerUtils.regexPreprocessing(regex, Regex.DIGIT_ONLY);
        int counter = 0;
        boolean matched = false;
        int generatedNum = Integer.MIN_VALUE;

        while (counter <= 10 || !matched) {
            String generatedString = new Generex(newRegex).random();
            generatedString = DataFakerUtils.regexPostProcessing(generatedString);
            try {
                generatedNum = Integer.parseInt(generatedString);
                matched = true;
            } catch (NumberFormatException e) {
                if (generatedNum == Integer.MIN_VALUE && counter == 10) {
                    log.error("Failed to generate Integer object from regex: " + regex);
                    throw new IllegalArgumentException(
                            "Failed generated number 5 times. Please check your regex again.");
                }
            } finally {
                counter++;
            }
        }

        return generatedNum;
    }

    /**
     * Generate random {@link Double} from regex pattern
     * 
     * @param regex The regex pattern
     * @return The generated value
     */
    public static double randDoubleFromRegex(String regex) {
        String newRegex = DataFakerUtils.regexPreprocessing(regex, Regex.DIGIT_ONLY);
        int counter = 0;
        boolean matched = false;
        double generatedNum = Double.MIN_VALUE;

        while (counter <= 5 || !matched) {
            String generatedString = new Generex(newRegex).random();
            generatedString = DataFakerUtils.regexPostProcessing(generatedString);
            try {
                generatedNum = Double.parseDouble(generatedString);
                if (generatedNum == Double.POSITIVE_INFINITY || generatedNum == Double.NEGATIVE_INFINITY)
                    generatedNum = randDouble();
                matched = true;
            } catch (NumberFormatException e) {
                if (generatedNum == Double.MIN_VALUE && counter == 5) {
                    log.error("Failed to generate Double object from regex: " + regex);
                    throw new IllegalArgumentException(
                            "Failed generated number 5 times. Please check your regex again.");
                }
            } finally {
                counter++;
            }
        }

        return generatedNum;
    }

    /**
     * Generate random {@link Long} from regex pattern
     * 
     * @param regex The regex pattern
     * @return The generated value
     */
    public static long randLongFromRegex(String regex) {
        String newRegex = DataFakerUtils.regexPreprocessing(regex, Regex.DIGIT_ONLY);
        int counter = 0;
        boolean matched = false;
        long generatedNum = Long.MIN_VALUE;

        while (counter <= 5 || !matched) {
            String generatedString = new Generex(newRegex).random();
            generatedString = DataFakerUtils.regexPostProcessing(generatedString);
            try {
                generatedNum = Long.parseLong(generatedString);
                matched = true;
            } catch (NumberFormatException e) {
                if (generatedNum == Long.MIN_VALUE && counter == 5) {
                    log.error("Failed to generate Long object from regex: " + regex);
                    throw new IllegalArgumentException(
                            "Failed generated number 5 times. Please check your regex again.");
                }
            } finally {
                counter++;
            }
        }

        return generatedNum;
    }

    /**
     * Generate random {@link String}
     * 
     * @return The generated value
     */
    public static String randString() {
        return randString(0, 10);
    }

    /**
     * Generate random {@link String}
     * 
     * @param maxLength The max length of the value
     * @return The generated value
     */
    public static String randString(int maxLength) {
        if (maxLength < 1)
            throw new IllegalArgumentException("Length supposed to be higher than 0.");

        return randString(0, maxLength);
    }

    /**
     * Generate random {@link String}
     * 
     * @param minLength The min length of the value
     * @param maxLength The max length of the value
     * @return The generated value
     */
    public static String randString(int minLength, int maxLength) {
        if (maxLength < 1)
            throw new IllegalArgumentException("Invalid string length.");

        return new Generex(Regex.WORD_CHARS + "{" + minLength + "," + maxLength + "}").random();
    }

    /**
     * Generate random {@link Integer}
     * 
     * @return The generated value
     */
    public static int randInt() {
        return random().nextInt();
    }

    /**
     * Generate random {@link Integer}
     * 
     * @param maxValue The max value
     * @return The generated value
     */
    public static int randInt(int maxValue) {
        return random().nextInt(maxValue);
    }

    /**
     * Generate random {@link Integer}
     * 
     * @param minValue The min value
     * @param maxValue The max value
     * @return The generated value
     */
    public static int randInt(int minValue, int maxValue) {
        return random().nextInt(minValue, maxValue);
    }

    /**
     * Generate random {@link Double}
     * 
     * @return The generated value
     */
    public static double randDouble() {
        return random().nextDouble();
    }

    /**
     * Generate random {@link Double}
     * 
     * @param maxValue The max value
     * @return The generated value
     */
    public static double randDouble(double maxValue) {
        return random().nextDouble(maxValue);
    }

    /**
     * Generate random {@link Double}
     * 
     * @param minValue The min value
     * @param maxValue The max value
     * @return The generated value
     */
    public static double randDouble(double minValue, double maxValue) {
        return random().nextDouble(minValue, maxValue);
    }

    /**
     * Generate random {@link Boolean}
     * 
     * @return The generated value
     */
    public static boolean randBoolean() {
        return random().nextBoolean();
    }

    /**
     * Generate random {@link Long}
     * 
     * @return The generated value
     */
    public static long randLong() {
        return random().nextLong();
    }

    /**
     * Generate random {@link Long}
     * 
     * @param maxValue The max value
     * @return The generated value
     */
    public static long randLong(long maxValue) {
        return random().nextLong(maxValue);
    }

    /**
     * Generate random {@link Long}
     * 
     * @param minValue The min value
     * @param maxValue The max value
     * @return The generated value
     */
    public static long randLong(long minValue, long maxValue) {
        return random().nextLong(minValue, maxValue);
    }

    /**
     * Generate random {@link LocalDateTime}
     * 
     * @return The generated value
     */
    public static LocalDateTime randDateTime() {
        LocalDateTime startDate = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
        return DataFakerUtils.randDateTime(startDate.toLocalDate(), LocalDateTime.now().toLocalDate());
    }

    /**
     * Generate random {@link LocalDateTime}
     * 
     * @param startDate The starting date
     * @return The generated value
     */
    public static LocalDateTime randDateTime(LocalDate startDate) {
        return DataFakerUtils.randDateTime(startDate, LocalDateTime.now().toLocalDate());
    }

    /**
     * Generate random {@link LocalDateTime}
     * 
     * @param startDate The min value of date
     * @param endDate   The max value of date
     * @return The generated value
     */
    public static LocalDateTime randDateTime(LocalDate startDate, LocalDate endDate) {
        long start = startDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        long end = endDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        return new Timestamp(random().nextLong(start, end)).toLocalDateTime();
    }

    /**
     * Pick random item from a list
     * 
     * @param <T>  The list data type
     * @param list The list
     * @return The random item from list
     */
    public static <T> T randItemFromList(List<T> list) {
        return list.get(random().nextInt(list.size()));
    }

    /**
     * Pick random item from a set
     * 
     * @param <T> The set data type
     * @param set The set
     * @return The random item from set
     */
    public static <T> T randItemFromList(Set<T> set) {
        List<T> list = new ArrayList<>(set);
        return randItemFromList(list);
    }

}