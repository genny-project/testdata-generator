package life.genny.datagenerator.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import life.genny.datagenerator.Constants;
import life.genny.datagenerator.Regex;
import life.genny.qwandaq.utils.testsuite.BaseTestCase;
import life.genny.qwandaq.utils.testsuite.JUnitTester;

public class DataFakerCustomUtilsTest extends BaseTestCase {

    @Test
    void generateName() {
        new JUnitTester<String, Boolean>()
                .setTest((input) -> {
                    return Expected(!input.input.isEmpty());
                })
                .createTest("Generate Random Name Check")
                .setInput(DataFakerCustomUtils.generateName())
                .setExpected(true)
                .build()

                .assertAll();
    }

    @Test
    void generateEmailFromRegex() {
        new JUnitTester<String, String>()
                .setTest((input) -> {
                    return Expected(input.input);
                })

                .setVerification((result, expected) -> {
                    Pattern pattern = Pattern.compile(expected);
                    Matcher matcher = pattern.matcher(result);
                    assertTrue(matcher.matches());
                })
                .createTest("Generate Email From Regex Check 1")
                .setInput(DataFakerCustomUtils.generateEmailFromRegex(Constants.EMAIL_REGEX))
                .setExpected(Constants.EMAIL_REGEX)
                .build()

                .setTest((input) -> {
                    return Expected(input.input.split("@")[1]);
                })
                .createTest("Generate Email From Regex Check 2")
                .setInput(DataFakerCustomUtils.generateEmailFromRegex(Constants.EMAIL_REGEX, "gada.io"))
                .setExpected("gada.io")
                .build()

                .assertAll();
    }

    @Test
    void generateEmail() {
        String firstName = DataFakerCustomUtils.generateName();
        String lastName = DataFakerCustomUtils.generateName();
        String auDomain = "gmail.au";

        new JUnitTester<String, String>()
                .setTest((input) -> {
                    return Expected(input.input);
                })
                .setVerification((result, expected) -> {
                    Pattern pattern = Pattern.compile(expected);
                    Matcher matcher = pattern.matcher(result);
                    assertTrue(matcher.matches());
                })

                .createTest("Generate Random Email Check")
                .setInput(DataFakerCustomUtils.generateEmail())
                .setExpected(Regex.CUSTOM_EMAIL_REGEX)
                .build()

                .createTest("Generate Email Check 1")
                .setInput(DataFakerCustomUtils.generateEmail(firstName, lastName))
                .setExpected("^(" + firstName + ")\\.(" + lastName + ")\\+" + Regex.WORD_CHARS
                        + "*\\@[A-Za-z]+(.[A-Za-z]+)+")
                .build()

                .createTest("Generate Email Check 2")
                .setInput(DataFakerCustomUtils.generateEmail(firstName, lastName, auDomain))
                .setExpected("^(" + firstName + ")\\.(" + lastName + ")\\+" + Regex.WORD_CHARS + "*\\@(" + auDomain
                        + ")+")
                .build()

                .assertAll();
    }

    @Test
    void generateInitials() {
        String fullName = "Thomas Alva Edison";
        new JUnitTester<String, String>()
                .setTest((input) -> {
                    return Expected(input.input);
                })

                .createTest("Generate Initials Check")
                .setInput(DataFakerCustomUtils.generateInitials(fullName.split(" ")))
                .setExpected("TAE")
                .build()

                .assertAll();
    }

    @Test
    void generatePhoneNumber() {
        String areaCode = "61";
        new JUnitTester<String, String>()
                .setTest((input) -> {
                    return Expected(input.input);
                })
                .setVerification((result, expected) -> {
                    Pattern pattern = Pattern.compile(expected);
                    assertTrue(pattern.matcher(result).matches());
                })

                .createTest("Generate Random Phone Number Check 1")
                .setInput(DataFakerCustomUtils.generatePhoneNumber())
                .setExpected(Regex.PHONE_REGEX)
                .build()

                .createTest("Generate Random Phone Number Check 2")
                .setInput(DataFakerCustomUtils.generatePhoneNumber(areaCode))
                .setExpected(Regex.PHONE_REGEX)
                .build()

                .assertAll();
    }

}
