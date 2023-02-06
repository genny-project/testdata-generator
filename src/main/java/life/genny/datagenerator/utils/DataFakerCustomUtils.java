package life.genny.datagenerator.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import life.genny.datagenerator.Regex;

/**
 * A static utility class used for standard operations
 * involving generate complex data especially using Regex.
 * 
 * @author Amrizal Fajar
 */
public class DataFakerCustomUtils {

    public static final String DEFAULT_DOMAIN = "gada.io";

    /**
     * Generate random characters as a name
     * 
     * @return The generated value
     */
    public static String generateName() {
        return DataFakerUtils.randStringFromRegex(Regex.ALPHABET + "{4,10}");
    }

    /**
     * Generate random email from regex pattern
     * 
     * @param regex The regex pattern
     * @return The generated value
     */
    public static String generateEmailFromRegex(String regex) {
        return DataFakerUtils.randStringFromRegex(regex);
    }

    /**
     * Generate random email from regex pattern
     * 
     * @param regex  The regex pattern
     * @param domain The domain
     * @return The generated value
     */
    public static String generateEmailFromRegex(String regex, String domain) {
        String email = DataFakerUtils.randStringFromRegex(regex);
        String username = email.split("@")[0];
        return username + "@" + domain;
    }

    /**
     * Generate random characters as an email
     * 
     * @return The generated value
     */
    public static String generateEmail() {
        return DataFakerUtils.randStringFromRegex(Regex.CUSTOM_EMAIL_REGEX);
    }

    public static String generateEmail(String name) {
        return name + "+" + UUID.randomUUID() + "@gmail."
                + DataFakerUtils.randStringFromRegex(Regex.TOP_LEVEL_DOMAIN_REGEX);
    }

    /**
     * Generate random email using first name and last name
     * 
     * @param firstName The first name
     * @param lastName  The last name
     * @return The generated value
     */
    public static String generateEmail(String firstName, String lastName) {
        return generateEmail(firstName, lastName,
                "gmail." + DataFakerUtils.randStringFromRegex(Regex.TOP_LEVEL_DOMAIN_REGEX));
    }

    /**
     * Generate random email using first name and last name
     * 
     * @param firstName The first name
     * @param lastName  The last name
     * @param domain    The domain
     * @return The generated value
     */
    public static String generateEmail(String firstName, String lastName, String domain) {
        return firstName + "." + lastName + "+" + UUID.randomUUID() + "@" + domain;
    }

    /**
     * Generate initials from {@link String} word or more
     * 
     * @param args The needed parameters
     * @return The generated value
     */
    public static String generateInitials(String... args) {
        String initials = "";
        for (String arg : args)
            initials += arg.split("")[0];
        return initials;
    }

    /**
     * Generate random phone number
     * 
     * @return The generated value
     */
    public static String generatePhoneNumber() {
        return DataFakerUtils.randStringFromRegex(Regex.PHONE_REGEX);
    }

    /**
     * Generate random phone number
     * 
     * @param areaCode The area code
     * @return The generated value
     */
    public static String generatePhoneNumber(String areaCode) {
        if (!areaCode.contains("+"))
            areaCode = "+" + areaCode;
        String phoneRegex = "([0-9]{9,11})";
        return areaCode + DataFakerUtils.randStringFromRegex(phoneRegex);
    }

    /**
     * Generate random selection
     * 
     * @return The generated value
     */
    public static String generateSelection() {
        return DataFakerUtils.randStringFromRegex(Regex.SELECTION_REGEX);
    }

    /**
     * Generate random html code
     * 
     * @return The generated value
     */
    public static String generateHTMLString() {
        return generateHTMLString("");
    }

    /**
     * Generate random html code
     * 
     * @param content The content inside html body
     * @return The generated value
     */
    public static String generateHTMLString(String content) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                <title>This is a generated html code</title>
                </head>
                <body>
                %s
                </body>
                </html>
                    """.formatted(content);
    }

    /**
     * Generate random html tag
     * 
     * @param content The content inside the html tag
     * @return The generated value
     */
    public static String generateHTMLTag(String content) {
        String tag = DataFakerUtils.randStringFromRegex(Regex.HTML_TAG_REGEX);
        return generateHTMLTag(content, tag);
    }

    /**
     * Generate random html tag
     * 
     * @param content The content inside the html tag
     * @param tag     The html tag
     * @return The generated value
     */
    public static String generateHTMLTag(String content, String tag) {
        return "<" + tag + ">" + content + "</" + tag + ">";
    }

    /**
     * Generate random html tag
     * 
     * @param content The content inside the html tag
     * @param tags    The html tags
     * @return The generated value
     */
    public static String generateHTMLTag(String content, String... tags) {
        List<String> listOfTags = new ArrayList<>(tags.length);
        String separator = ":SEPARATOR:";
        for (int i = 0; i < tags.length; i++) {
            String tag = tags[i];
            String temp = null;
            if (i == tags.length - 1)
                temp = tag + separator + content + separator + tag;
            else
                temp = tag + separator + tag;

            if (temp != null) {
                String[] items = temp.split(separator);
                items[0] = "<" + items[0] + ">";
                items[items.length - 1] = "</" + items[items.length - 1] + ">";
                listOfTags.add(String.join("", items));
            }
        }

        String result = null;
        String changeThis = ">:CHANGE THIS:<";
        for (String tag : listOfTags) {
            if (result == null) {
                result = tag.replace("><", changeThis);
            } else {
                if (tag.contains("><"))
                    tag = tag.replace("><", changeThis);
                result = result.replace(changeThis, ">" + tag + "<");
            }
        }
        return result;
    }

    /**
     * Generate random descriptive html tag
     * 
     * @return The generated value
     */
    public static String generateDescriptiveHTMLTag() {
        return generateDescriptiveHTMLTag("p");
    }

    /**
     * Generate random descriptive html tag
     * 
     * @param tag The html tag
     * @return The generated value
     */
    public static String generateDescriptiveHTMLTag(String tag) {
        return generateDescriptiveHTMLTag(tag, 1);
    }

    /**
     * Generate random descriptive html tag
     * 
     * @param tag    The html tag
     * @param length The repetition
     * @return The generated value
     */
    public static String generateDescriptiveHTMLTag(String tag, int length) {
        String objHtml = "";
        for (int i = 0; i < length; i++)
            objHtml += generateHTMLTag(
                    DataFakerUtils.randStringFromRegex(Regex.DESCRIPTION_REGEX),
                    tag);
        return objHtml;
    }

    /**
     * Generate random website url
     * 
     * @return The generated value
     */
    public static String generateWebsiteURL() {
        return generateWebsiteURL(generateName());
    }

    /**
     * Generate random website url
     * 
     * @param domain The website root domain
     * @return The generated value
     */
    public static String generateWebsiteURL(String domain) {
        return generateWebsiteURL(domain, DataFakerUtils
                .randStringFromRegex(Regex.TOP_LEVEL_DOMAIN_REGEX));
    }

    /**
     * Generate random website url
     * 
     * @param domain The website root domain
     * @param tld    The top level domain
     * @return The generated value
     */
    public static String generateWebsiteURL(String domain, String tld) {
        if (!tld.startsWith("."))
            tld = "." + tld;
        return DataFakerUtils.randStringFromRegex("((http(s)?\\:\\/\\/)?www\\.)?") +
                domain + tld;
    }
}
