package life.genny.datagenerator.utils;

public class DataFakerCustomUtils {

    public static final String DEFAULT_DOMAIN = "gada.io";
    public static final String TEXT_PARAGRAPH_REGEX = "(\\w{5,20}[ ]{1}){80,120}";
    public static final String CUSTOM_EMAIL_REGEX = "[a-zA-Z0-9]+(\\.[a-zA-Z0-9]*)?(\\@gmail\\.(com|au|io))";

    public static String generateName() {
        return DataFakerUtils.randStringFromRegex(RegexMode.ALPHABET + "{4,10}");
    }

    public static String generateEmailFromRegex(String regex) {
        return DataFakerUtils.randStringFromRegex(regex);
    }

    public static String generateEmailFromRegex(String regex, String domain) {
        String email = DataFakerUtils.randStringFromRegex(regex);
        String username = email.split("@")[0];
        return username + "@" + domain;
    }

    public static String generateEmail() {
        return DataFakerUtils.randStringFromRegex(CUSTOM_EMAIL_REGEX);
    }

    public static String generateEmail(String firstName, String lastName) {
        return DataFakerCustomUtils.generateEmail(firstName, lastName, DEFAULT_DOMAIN);
    }

    public static String generateEmail(String firstName, String lastName, String domain) {
        return firstName + "." + lastName + "+" + DataFakerUtils.randString(5, 10) + "@" + domain;
    }

    public static String generateInitials(String... args) {
        String initials = "";
        for (String arg : args)
            initials += arg.split("")[0];
        return initials;
    }
}
