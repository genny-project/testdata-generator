package life.genny.datagenerator.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import life.genny.datagenerator.Constants;
import life.genny.qwandaq.utils.testsuite.BaseTestCase;
import life.genny.qwandaq.utils.testsuite.Expected;
import life.genny.qwandaq.utils.testsuite.JUnitTester;

public class DataFakerGeneralUtilsTest extends BaseTestCase {

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
                .setInput(Constants.EMAIL_REGEX)
                .setExpected(String.class)
                .build()

                .createTest("LinkedIn URL Regex Check")
                .setInput(Constants.LINKEDIN_URL_REGEX)
                .setExpected(String.class)
                .build()

                .createTest("Hours Regex Check")
                .setInput(Constants.HOURS_REGEX)
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
                    Pattern pattern = Pattern.compile(Constants.NUMBER_REGEX);
                    Matcher matcher = pattern.matcher(result.toString());
                    assertEquals(result.getClass(), expected);
                    assertTrue(matcher.matches());
                })

                .createTest("Generate Random Integer From Regex Check 1")
                .setInput(DataFakerUtils.randIntFromRegex(Constants.NUMBER_REGEX))
                .setExpected(Integer.class)
                .build()

                .createTest("Generate Random Integer From Regex Check 2")
                .setInput(DataFakerUtils.randIntFromRegex(Constants.NUMBER_REGEX))
                .setExpected(Integer.class)
                .build()

                .createTest("Generate Random Integer From Regex Check 3")
                .setInput(DataFakerUtils.randIntFromRegex(Constants.NUMBER_REGEX))
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
                    Pattern pattern = Pattern.compile(Constants.DECIMAL_REGEX);
                    Matcher matcher = pattern.matcher(result.toString());
                    assertTrue(matcher.matches());
                    assertEquals(result.getClass(), expected);
                })

                .createTest("Generate Random Double From Regex Check 1")
                .setInput(DataFakerUtils.randDoubleFromRegex(Constants.DECIMAL_REGEX))
                .setExpected(Double.class)
                .build()

                .createTest("Generate Random Double From Regex Check 2")
                .setInput(DataFakerUtils.randDoubleFromRegex(Constants.DECIMAL_REGEX))
                .setExpected(Double.class)
                .build()

                .createTest("Generate Random Double From Regex Check 3")
                .setInput(DataFakerUtils.randDoubleFromRegex(Constants.DECIMAL_REGEX))
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
                    Pattern pattern = Pattern.compile(Constants.NUMBER_REGEX);
                    Matcher matcher = pattern.matcher(result.toString());
                    assertEquals(result.getClass(), expected);
                    assertTrue(matcher.matches());
                })

                .createTest("Generate Random Long From Regex Check 1")
                .setInput(DataFakerUtils.randLongFromRegex(Constants.NUMBER_REGEX))
                .setExpected(Long.class)
                .build()

                .createTest("Generate Random Long From Regex Check 2")
                .setInput(DataFakerUtils.randLongFromRegex(Constants.NUMBER_REGEX))
                .setExpected(Long.class)
                .build()

                .createTest("Generate Random Long From Regex Check 3")
                .setInput(DataFakerUtils.randLongFromRegex(Constants.NUMBER_REGEX))
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
                    Pattern pattern = Pattern.compile(RegexMode.WORD_CHARS + RegexMode.PLUS);
                    Matcher matcher = pattern.matcher(result);
                    assertTrue(matcher.matches());
                    assertTrue(result instanceof String);
                })

                .createTest("Create Random String Check 1")
                .setInput(DataFakerUtils.randString())
                .setExpected(Constants.IGNORE)
                .build()

                .createTest("Create Random String Check 2")
                .setInput(DataFakerUtils.randString())
                .setExpected(Constants.IGNORE)
                .build()

                .setVerification((result, expected) -> {
                    Pattern pattern = Pattern.compile(RegexMode.WORD_CHARS + RegexMode.ASTERISK);
                    Matcher matcher = pattern.matcher(result);
                    assertTrue(matcher.matches());
                    assertTrue(result.length() < maxLength);
                    assertTrue(result instanceof String);
                })
                .setTest((input) -> {
                    return new Expected<>(input.input);
                })

                .createTest("Create Random String Check 3")
                .setInput(DataFakerUtils.randString(maxLength))
                .setExpected(Constants.IGNORE)
                .build()

                .createTest("Create Random String Check 4")
                .setInput(DataFakerUtils.randString(10, maxLength))
                .setExpected(Constants.IGNORE)
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
                .createTest("Generate Random Integer Check 1")
                .setInput(DataFakerUtils.randInt())
                .setExpected(Integer.class)
                .build()

                .setVerification((result, expected) -> {
                    assertEquals(result.getClass(), expected);
                    assertTrue((Integer) result < maxValue);
                })
                .createTest("Generate Random Integer Check 2")
                .setInput(DataFakerUtils.randInt(maxValue))
                .setExpected(Integer.class)
                .build()

                .setVerification((result, expected) -> {
                    assertEquals(result.getClass(), expected);
                    assertTrue((Integer) result > minValue);
                    assertTrue((Integer) result < maxValue);
                })
                .createTest("Generate Random Integer Check 3")
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
                .createTest("Generate Random Integer Check 1")
                .setInput(DataFakerUtils.randDouble())
                .setExpected(Double.class)
                .build()

                .setVerification((result, expected) -> {
                    assertEquals(result.getClass(), expected);
                    assertTrue((Double) result < maxValue);
                })
                .createTest("Generate Random Integer Check 2")
                .setInput(DataFakerUtils.randDouble(maxValue))
                .setExpected(Double.class)
                .build()

                .setVerification((result, expected) -> {
                    assertEquals(result.getClass(), expected);
                    assertTrue((Double) result > minValue);
                    assertTrue((Double) result < maxValue);
                })
                .createTest("Generate Random Integer Check 3")
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
                .createTest("Generate Random Boolean Check 1")
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
                .createTest("Generate Random Long Check 1")
                .setInput(DataFakerUtils.randLong())
                .setExpected(Long.class)
                .build()

                .setVerification((result, expected) -> {
                    assertEquals(result.getClass(), expected);
                    assertTrue((Long) result < maxValue);
                })
                .createTest("Generate Random Long Check 2")
                .setInput(DataFakerUtils.randLong(maxValue))
                .setExpected(Long.class)
                .build()

                .setVerification((result, expected) -> {
                    assertEquals(result.getClass(), expected);
                    assertTrue((Long) result < maxValue);
                    assertTrue((Long) result > minValue);
                })
                .createTest("Generate Random Long Check 3")
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
                .createTest("Generate Random Date Time Check 1")
                .setInput(DataFakerUtils.randDateTime())
                .setExpected(LocalDateTime.class)
                .build()

                .setVerification((result, expected) -> {
                    long now = ((LocalDateTime) result).toInstant(ZoneOffset.UTC).toEpochMilli();
                    assertEquals(result.getClass(), expected);
                    assertTrue(now > startDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
                })
                .createTest("Generate Random Date Time Check 2")
                .setInput(DataFakerUtils.randDateTime(startDate))
                .setExpected(LocalDateTime.class)
                .build()

                .setVerification((result, expected) -> {
                    long now = ((LocalDateTime) result).toInstant(ZoneOffset.UTC).toEpochMilli();
                    assertEquals(result.getClass(), expected);
                    assertTrue(now > startDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
                    assertTrue(now < endDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
                })
                .createTest("Generate Random Date Time Check 3")
                .setInput(DataFakerUtils.randDateTime(startDate, endDate))
                .setExpected(LocalDateTime.class)
                .build()

                .assertAll();
    }

    @Test
    void randItemFromList() {
        List<String> genders = new ArrayList<>(
                Arrays.asList(new String[] { "Male", "Female", "Other" }));

        new JUnitTester<String, List<String>>()
                .setTest((input) -> {
                    return Expected(Arrays.asList(new String[] { input.input }));
                })
                .setVerification((result, expected) -> {
                    assertTrue(expected.containsAll(result));
                })
                .createTest("Take Random Item From List Check")
                .setInput(DataFakerUtils.randItemFromList(genders))
                .setExpected(genders)
                .build()
                .assertAll();

        Set<Integer> nums = new HashSet<>();
        for (int i = 0; i < 20; i++)
            nums.add(DataFakerUtils.randInt(-100, 100));

        new JUnitTester<Integer, Set<Integer>>()
                .setTest((input) -> {
                    return Expected(new HashSet<>(Arrays.asList(new Integer[] { input.input })));
                })
                .setVerification((result, expected) -> {
                    assertTrue(expected.containsAll(result));
                })
                .createTest("Take Random Item From Set Check")
                .setInput(DataFakerUtils.randItemFromList(nums))
                .setExpected(nums)
                .build()
                .assertAll();
    }

}
