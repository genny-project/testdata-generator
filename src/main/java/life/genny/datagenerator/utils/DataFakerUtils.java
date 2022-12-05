package life.genny.datagenerator.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import com.mifmif.common.regex.Generex;

public class DataFakerUtils {

    private static Random random() {
        return new Random();
    }

    private static String regexPreprocessing(String regex, String newRegex) {
        String regexTemp = regex.replace(".*", newRegex + Regex.ASTERISK);
        regexTemp = regexTemp.replace(".+", newRegex + Regex.PLUS);
        return regexTemp;
    }

    private static String regexPostProcessing(String result) {
        return result.replaceAll("[$^]*", "");
    }

    public static String randStringFromRegex(String regex) {
        String newRegex = DataFakerUtils.regexPreprocessing(regex, Regex.WORD_CHARS);
        Generex generator = new Generex(newRegex);
        String randString = generator.random();
        randString = DataFakerUtils.regexPostProcessing(randString);
        return randString;
    }

    public static int randIntFromRegex(String regex) {
        String newRegex = DataFakerUtils.regexPreprocessing(regex, Regex.DIGIT_ONLY);
        int counter = 0;
        boolean matched = false;
        int generatedNum = Integer.MIN_VALUE;

        while (counter <= 5 || !matched) {
            String generatedString = new Generex(newRegex).random();
            generatedString = DataFakerUtils.regexPostProcessing(generatedString);
            try {
                generatedNum = Integer.parseInt(generatedString);
                matched = true;
            } catch (NumberFormatException e) {
                if (generatedNum == Integer.MIN_VALUE && counter == 5)
                    throw new IllegalArgumentException(
                            "Failed generated number 5 times. Please check your regex again.");
            } finally {
                counter++;
            }
        }

        return generatedNum;
    }

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
                if (generatedNum == Double.MIN_VALUE && counter == 5)
                    throw new IllegalArgumentException(
                            "Failed generated number 5 times. Please check your regex again.");
            } finally {
                counter++;
            }
        }

        return generatedNum;
    }

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
                if (generatedNum == Long.MIN_VALUE && counter == 5)
                    throw new IllegalArgumentException(
                            "Failed generated number 5 times. Please check your regex again.");
            } finally {
                counter++;
            }
        }

        return generatedNum;
    }

    public static String randString() {
        return randString(0, 10);
    }

    public static String randString(int maxLength) {
        if (maxLength < 1)
            throw new IllegalArgumentException("Length supposed to be higher than 0.");

        return randString(0, maxLength);
    }

    public static String randString(int minLength, int maxLength) {
        if (maxLength < 1)
            throw new IllegalArgumentException("Invalid string length.");

        return new Generex(Regex.WORD_CHARS + "{" + minLength + "," + maxLength + "}").random();
    }

    public static int randInt() {
        return random().nextInt();
    }

    public static int randInt(int maxValue) {
        return random().nextInt(maxValue);
    }

    public static int randInt(int minValue, int maxValue) {
        return random().nextInt(minValue, maxValue);
    }

    public static double randDouble() {
        return random().nextDouble();
    }

    public static double randDouble(double maxValue) {
        return random().nextDouble(maxValue);
    }

    public static double randDouble(double minValue, double maxValue) {
        return random().nextDouble(minValue, maxValue);
    }

    public static boolean randBoolean() {
        return random().nextBoolean();
    }

    public static long randLong() {
        return random().nextLong();
    }

    public static long randLong(long maxValue) {
        return random().nextLong(maxValue);
    }

    public static long randLong(long minValue, long maxValue) {
        return random().nextLong(minValue, maxValue);
    }

    public static LocalDateTime randDateTime() {
        LocalDateTime startDate = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
        return DataFakerUtils.randDateTime(startDate.toLocalDate(), LocalDateTime.now().toLocalDate());
    }

    public static LocalDateTime randDateTime(LocalDate startDate) {
        return DataFakerUtils.randDateTime(startDate, LocalDateTime.now().toLocalDate());
    }

    public static LocalDateTime randDateTime(LocalDate startDate, LocalDate endDate) {
        long start = startDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        long end = endDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        return new Timestamp(random().nextLong(start, end)).toLocalDateTime();
    }

}