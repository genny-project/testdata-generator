package life.genny.datagenerator.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import life.genny.datagenerator.Constants;
import life.genny.qwandaq.utils.testsuite.BaseTestCase;
import life.genny.qwandaq.utils.testsuite.JUnitTester;

public class DataFakerSpecialUtilsTest extends BaseTestCase {
    
    @Test
    void generateName() {
        new JUnitTester<String, Boolean>()
            .setTest((input) -> {
                return Expected(!input.input.isEmpty());
            })
            .createTest("Generate Random Name Check")
            .setInput(DataFakerSpecialUtils.generateName())
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
            .setInput(DataFakerSpecialUtils.generateEmailFromRegex(Constants.EMAIL_REGEX))
            .setExpected(Constants.EMAIL_REGEX)
            .build()

            .setTest((input) -> {
                return Expected(input.input.split("@")[1]);
            })
            .createTest("Generate Email From Regex Check 2")
            .setInput(DataFakerSpecialUtils.generateEmailFromRegex(Constants.EMAIL_REGEX, "gada.io"))
            .setExpected("gada.io")
            .build()

            .assertAll();
    }

    @Test
    void generateEmail() {
        String firstName = DataFakerSpecialUtils.generateName();
        String lastName = DataFakerSpecialUtils.generateName();
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
            .createTest("Generate Email Check 1")
            .setInput(DataFakerSpecialUtils.generateEmail(firstName, lastName))
            .setExpected("^(" + firstName + ")\\.(" + lastName + ")\\+" + RegexMode.WORD_CHARS + "*\\@[A-Za-z]+(.[A-Za-z]+)+")
            .build()

            .setVerification((result, expected) -> {
                Pattern pattern = Pattern.compile(expected);
                Matcher matcher = pattern.matcher(result);
                assertTrue(matcher.matches());
            })
            .createTest("Generate Email Check 2")
            .setInput(DataFakerSpecialUtils.generateEmail(firstName, lastName, auDomain))
            .setExpected("^(" + firstName + ")\\.(" + lastName + ")\\+" + RegexMode.WORD_CHARS + "*\\@(" + auDomain + ")+")
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
            .setInput(DataFakerSpecialUtils.generateInitials(fullName.split(" ")))
            .setExpected("TAE")
            .build()

            .assertAll();
    }

}
