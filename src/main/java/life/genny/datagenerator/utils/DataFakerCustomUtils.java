package life.genny.datagenerator.utils;

import life.genny.datagenerator.RegexMode;

/**
 * A static utility class used for standard operations 
 * involving generate complex data especially using Regex.
 * 
 * @author Amrizal Fajar
 */
public class DataFakerCustomUtils {

    public static final String DEFAULT_DOMAIN = "gada.io";
    public static final String TEXT_PARAGRAPH_REGEX = "(\\w{5,20}[ ]{1}){80,120}";
    public static final String CUSTOM_EMAIL_REGEX = "[a-zA-Z0-9]+(\\.[a-zA-Z0-9]*)?(\\@gmail\\.(com|au|io))";
    public static final String PHONE_REGEX = "(^\\+[1-9]{1,3})([0-9]{9,11})$";

    /**
     * Generate random characters as a name
     * 
     * @return The generated value
     */
    public static String generateName() {
        return DataFakerUtils.randStringFromRegex(RegexMode.ALPHABET + "{4,10}");
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
     * @param regex The regex pattern
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
        return DataFakerUtils.randStringFromRegex(CUSTOM_EMAIL_REGEX);
    }

    /**
     * Generate random email using first name and last name
     * 
     * @param firstName The first name
     * @param lastName The last name
     * @return The generated value
     */
    public static String generateEmail(String firstName, String lastName) {
        return DataFakerCustomUtils.generateEmail(firstName, lastName, DEFAULT_DOMAIN);
    }

    /**
     * Generate random email using first name and last name
     * 
     * @param firstName The first name
     * @param lastName The last name
     * @param domain The domain
     * @return The generated value
     */
    public static String generateEmail(String firstName, String lastName, String domain) {
        return firstName + "." + lastName + "+" + DataFakerUtils.randString(5, 10) + "@" + domain;
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
        return DataFakerUtils.randStringFromRegex(PHONE_REGEX);
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
}
