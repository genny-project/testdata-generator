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

    @Test
    void generateFullAddress() {
        new JUnitTester<String, Boolean>()
                .setTest((input) -> {
                    return Expected(input.input != null);
                })
                .createTest("Generate Random Full Address Check")
                .setInput(DataFakerCustomUtils.generateFullAddress())
                .setExpected(true)
                .build()
                .assertAll();
    }

    @Test
    void generateSelection() {
        new JUnitTester<String, String>()
                .setTest((input) -> {
                    return Expected(input.input);
                })
                .setVerification((result, expected) -> {
                    Pattern pattern = Pattern.compile(expected);
                    assertTrue(pattern.matcher(result).matches());
                })
                .createTest("Generate Random Selection Check")
                .setInput(DataFakerCustomUtils.generateSelection())
                .setExpected(Regex.SELECTION_REGEX)
                .build()
                .assertAll();
    }

    @Test
    void generateHTMLString() {
        new JUnitTester<String, Boolean>()
                .setTest((input) -> {
                    Pattern htmlPattern = Pattern.compile("(<!DOCTYPE html>)?(<html>).*(<\\/html>)");
                    Matcher htmlMatcher = htmlPattern.matcher(
                            input.input.replaceAll("[\t\n]+", ""));
                    return Expected(htmlMatcher.matches());
                })

                .createTest("Generate Random HTML Code Check 1")
                .setInput(DataFakerCustomUtils.generateHTMLString())
                .setExpected(true)
                .build()

                .createTest("Generate Random HTML Code Check 2")
                .setInput(DataFakerCustomUtils.generateHTMLString("<h1>This is an H1 tag.</h1>"))
                .setExpected(true)
                .build()

                .assertAll();
    }

    @Test
    void generateHTMLTag() {
        String tagContent = "This is a tag content";

        new JUnitTester<String, Boolean>()
                .setTest((input) -> {
                    Pattern tagPattern = Pattern.compile("(<[A-Za-z0-9]+>)+(" +
                            tagContent + ")(</[A-Za-z0-9]+>)+");
                    return Expected(tagPattern.matcher(input.input).matches());
                })

                .createTest("Generate Random HTML Tag Check 1")
                .setInput(DataFakerCustomUtils.generateHTMLTag(tagContent))
                .setExpected(true)
                .build()

                .createTest("Generate Random HTML Tag Check 2")
                .setInput(DataFakerCustomUtils.generateHTMLTag(tagContent, "h1"))
                .setExpected(true)
                .build()
                
                .createTest("Generate Random HTML Tag Check 3")
                .setInput(DataFakerCustomUtils.generateHTMLTag(tagContent, "ul", "li"))
                .setExpected(true)
                .build()

                .createTest("Generate Random HTML Tag Check 4")
                .setInput(DataFakerCustomUtils.generateHTMLTag(tagContent, "ul", "li", "strong"))
                .setExpected(true)
                .build()

                .assertAll();
    }
}
