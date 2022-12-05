package life.genny.datagenerator.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import life.genny.qwandaq.utils.testsuite.BaseTestCase;
import life.genny.qwandaq.utils.testsuite.Expected;
import life.genny.qwandaq.utils.testsuite.JUnitTester;

public class DataFakerUtilsTest extends BaseTestCase {

    private static final String IGNORE = "YOU CAN IGNORE THIS!!";
    private static final String EMAIL_REGEX = "^(^[a-zA-Z0-9._-]*)([+]?)([a-zA-Z0-9._-]*)([@])([a-zA-Z0-9._-]*)([.])([a-zA-Z.]{2,6})$";
    private static final String LINKEDIN_URL_REGEX = "(http://|https://)?[a-z]{2,3}\\.linkedin\\.com\\/.*$";
    private static final String HOURS_REGEX = "^((2[0-3]|[1][0-9]|[1-9])([.][0-9]{1})*)$";
    private static final String NUMBER_REGEX = "^-?\\d{1,10}$";
    private static final String DECIMAL_REGEX = "^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$";

    @Test
    void randStringFromRegex() {
        new JUnitTester<String, Object>()
                .setTest((input) -> {
                    return new Expected<>(input.input);
                })
                .setVerification((result, expected) -> {
                    String randString = DataFakerUtils.randStringFromRegex((String) result);
                    Pattern pattern = Pattern.compile((String) result);
                    Matcher matcher = pattern.matcher(randString);
                    assertEquals(randString.getClass(), expected);
                    assertTrue(matcher.matches());
                })

                .createTest("Email Regex Check")
                .setInput(EMAIL_REGEX)
                .setExpected(String.class)
                .build()

                .createTest("LinkedIn URL Regex Check")
                .setInput(LINKEDIN_URL_REGEX)
                .setExpected(String.class)
                .build()

                .createTest("Hours Regex Check")
                .setInput(HOURS_REGEX)
                .setExpected(String.class)
                .build()

                .assertAll();
    }

    @Test
    void randIntFromRegex() {
        new JUnitTester<Integer, Object>()
                .setTest((input) -> {
                    return new Expected<>(input.input);
                })
                .setVerification((result, expected) -> {
                    Pattern pattern = Pattern.compile(NUMBER_REGEX);
                    Matcher matcher = pattern.matcher(result.toString());
                    assertEquals(result.getClass(), expected);
                    assertTrue(matcher.matches());
                })

                .createTest("Generate Random Integer From Regex 1")
                .setInput(DataFakerUtils.randIntFromRegex(NUMBER_REGEX))
                .setExpected(Integer.class)
                .build()

                .createTest("Generate Random Integer From Regex 2")
                .setInput(DataFakerUtils.randIntFromRegex(NUMBER_REGEX))
                .setExpected(Integer.class)
                .build()

                .createTest("Generate Random Integer From Regex 3")
                .setInput(DataFakerUtils.randIntFromRegex(NUMBER_REGEX))
                .setExpected(Integer.class)
                .build()

                .assertAll();
    }

    @Test
    void randDoubleFromRegex() {
        new JUnitTester<Double, Object>()
                .setTest((input) -> {
                    return new Expected<>(input.input);
                })
                .setVerification((result, expected) -> {
                    Pattern pattern = Pattern.compile(DECIMAL_REGEX);
                    Matcher matcher = pattern.matcher(result.toString());
                    assertTrue(matcher.matches());
                    assertEquals(result.getClass(), expected);
                })

                .createTest("Generate Random Double From Regex 1")
                .setInput(DataFakerUtils.randDoubleFromRegex(DECIMAL_REGEX))
                .setExpected(Double.class)
                .build()

                .createTest("Generate Random Double From Regex 2")
                .setInput(DataFakerUtils.randDoubleFromRegex(DECIMAL_REGEX))
                .setExpected(Double.class)
                .build()

                .createTest("Generate Random Double From Regex 3")
                .setInput(DataFakerUtils.randDoubleFromRegex(DECIMAL_REGEX))
                .setExpected(Double.class)
                .build()

                .assertAll();
    }

    @Test
    void randLongFromRegex() {
        new JUnitTester<Long, Object>()
                .setTest((input) -> {
                    return new Expected<>(input.input);
                })
                .setVerification((result, expected) -> {
                    Pattern pattern = Pattern.compile(NUMBER_REGEX);
                    Matcher matcher = pattern.matcher(result.toString());
                    assertEquals(result.getClass(), expected);
                    assertTrue(matcher.matches());
                })

                .createTest("Generate Random Long From Regex 1")
                .setInput(DataFakerUtils.randLongFromRegex(NUMBER_REGEX))
                .setExpected(Long.class)
                .build()

                .createTest("Generate Random Long From Regex 2")
                .setInput(DataFakerUtils.randLongFromRegex(NUMBER_REGEX))
                .setExpected(Long.class)
                .build()

                .createTest("Generate Random Long From Regex 3")
                .setInput(DataFakerUtils.randLongFromRegex(NUMBER_REGEX))
                .setExpected(Long.class)
                .build()

                .assertAll();
    }

    @Test
    void randString() {
        int maxLength = 100;

        new JUnitTester<String, String>()
                .setTest((input) -> {
                    return new Expected<>(input.input);
                })
                .setVerification((result, expexted) -> {
                    Pattern pattern = Pattern.compile(Regex.WORD_CHARS + Regex.PLUS);
                    Matcher matcher = pattern.matcher(result);
                    assertTrue(matcher.matches());
                    assertTrue(result instanceof String);
                })

                .createTest("Create Random String 1")
                .setInput(DataFakerUtils.randString())
                .setExpected(IGNORE)
                .build()

                .createTest("Create Random String 2")
                .setInput(DataFakerUtils.randString())
                .setExpected(IGNORE)
                .build()

                .setVerification((result, expected) -> {
                    Pattern pattern = Pattern.compile(Regex.WORD_CHARS + Regex.ASTERISK);
                    Matcher matcher = pattern.matcher(result);
                    assertTrue(matcher.matches());
                    assertTrue(result.length() < maxLength);
                    assertTrue(result instanceof String);
                })
                .setTest((input) -> {
                    return new Expected<>(input.input);
                })

                .createTest("Create Random String 3")
                .setInput(DataFakerUtils.randString(maxLength))
                .setExpected(IGNORE)
                .build()

                .createTest("Create Random String 4")
                .setInput(DataFakerUtils.randString(10, maxLength))
                .setExpected(IGNORE)
                .build()

                .assertAll();
    }

    @Test
    void randInt() {
        int minValue = -100;
        int maxValue = 100;

        new JUnitTester<Integer, Object>()
                .setTest((input) -> {
                    return new Expected<>(input.input);
                })

                .setVerification((result, expected) -> {
                    assertEquals(result.getClass(), expected);
                })
                .createTest("Generate Random Integer 1")
                .setInput(DataFakerUtils.randInt())
                .setExpected(Integer.class)
                .build()

                .setVerification((result, expected) -> {
                    assertEquals(result.getClass(), expected);
                    assertTrue((Integer) result < maxValue);
                })
                .createTest("Generate Random Integer 2")
                .setInput(DataFakerUtils.randInt(maxValue))
                .setExpected(Integer.class)
                .build()

                .setVerification((result, expected) -> {
                    assertEquals(result.getClass(), expected);
                    assertTrue((Integer) result > minValue);
                    assertTrue((Integer) result < maxValue);
                })
                .createTest("Generate Random Integer 3")
                .setInput(DataFakerUtils.randInt(minValue, maxValue))
                .setExpected(Integer.class)
                .build()

                .assertAll();
    }

    @Test
    void randDouble() {
        double minValue = -100.99;
        double maxValue = 99.99;

        new JUnitTester<Double, Object>()
                .setTest((input) -> {
                    return new Expected<>(input.input);
                })

                .setVerification((result, expected) -> {
                    assertEquals(result.getClass(), expected);
                })
                .createTest("Generate Random Integer 1")
                .setInput(DataFakerUtils.randDouble())
                .setExpected(Double.class)
                .build()

                .setVerification((result, expected) -> {
                    assertEquals(result.getClass(), expected);
                    assertTrue((Double) result < maxValue);
                })
                .createTest("Generate Random Integer 2")
                .setInput(DataFakerUtils.randDouble(maxValue))
                .setExpected(Double.class)
                .build()

                .setVerification((result, expected) -> {
                    assertEquals(result.getClass(), expected);
                    assertTrue((Double) result > minValue);
                    assertTrue((Double) result < maxValue);
                })
                .createTest("Generate Random Integer 3")
                .setInput(DataFakerUtils.randDouble(minValue, maxValue))
                .setExpected(Double.class)
                .build()

                .assertAll();
    }

    @Test
    void randBoolean() {
        new JUnitTester<Boolean, Class<?>>()
                .setTest((input) -> {
                    return new Expected<>(input.input.getClass());
                })
                .createTest("Generate Random Boolean 1")
                .setInput(DataFakerUtils.randBoolean())
                .setExpected(Boolean.class)
                .build()

                .assertAll();
    }

    @Test
    void randLong() {
        long minValue = 100;
        long maxValue = 1000;

        new JUnitTester<Long, Object>()
                .setTest((input) -> {
                    return new Expected<>(input.input);
                })
                .setVerification((result, expected) -> {
                    assertEquals(result.getClass(), expected);
                })
                .createTest("Generate Random Long 1")
                .setInput(DataFakerUtils.randLong())
                .setExpected(Long.class)
                .build()

                .setVerification((result, expected) -> {
                    assertEquals(result.getClass(), expected);
                    assertTrue((Long) result < maxValue);
                })
                .createTest("Generate Random Long 2")
                .setInput(DataFakerUtils.randLong(maxValue))
                .setExpected(Long.class)
                .build()

                .setVerification((result, expected) -> {
                    assertEquals(result.getClass(), expected);
                    assertTrue((Long) result < maxValue);
                    assertTrue((Long) result > minValue);
                })
                .createTest("Generate Random Long 3")
                .setInput(DataFakerUtils.randLong(minValue, maxValue))
                .setExpected(Long.class)
                .build()

                .assertAll();
    }

    @Test
    void randDateTime() {
        LocalDate startDate = LocalDate.of(2020, 8, 17);
        LocalDate endDate = LocalDate.of(2021, 4, 5);

        new JUnitTester<LocalDateTime, Object>()
                .setTest((input) -> {
                    return new Expected<>(input.input);
                })

                .setVerification((result, expected) -> {
                    assertEquals(result.getClass(), expected);
                })
                .createTest("Generate Random Date Time 1")
                .setInput(DataFakerUtils.randDateTime())
                .setExpected(LocalDateTime.class)
                .build()

                .setVerification((result, expected) -> {
                    long now = ((LocalDateTime) result).toInstant(ZoneOffset.UTC).toEpochMilli();
                    assertEquals(result.getClass(), expected);
                    assertTrue(now > startDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
                })
                .createTest("Generate Random Date Time 2")
                .setInput(DataFakerUtils.randDateTime(startDate))
                .setExpected(LocalDateTime.class)
                .build()

                .setVerification((result, expected) -> {
                    long now = ((LocalDateTime) result).toInstant(ZoneOffset.UTC).toEpochMilli();
                    assertEquals(result.getClass(), expected);
                    assertTrue(now > startDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
                    assertTrue(now < endDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
                })
                .createTest("Generate Random Date Time 3")
                .setInput(DataFakerUtils.randDateTime(startDate, endDate))
                .setExpected(LocalDateTime.class)
                .build()

                .assertAll();
    }

}
