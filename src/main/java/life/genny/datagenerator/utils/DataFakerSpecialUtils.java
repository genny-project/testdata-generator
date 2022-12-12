package life.genny.datagenerator.utils;

public class DataFakerSpecialUtils {

    public static final String DEFAULT_DOMAIN = "gada.io";

    public static String generateName() {
        return DataFakerGeneralUtils.randStringFromRegex(RegexMode.ALPHABET + "{4,10}");
    }

    public static String generateEmailFromRegex(String regex) {
        return DataFakerGeneralUtils.randStringFromRegex(regex);
    }

    public static String generateEmailFromRegex(String regex, String domain) {
        String email = DataFakerGeneralUtils.randStringFromRegex(regex);
        String username = email.split("@")[0];
        return username + "@" + domain;
    }

    public static String generateEmail(String firstName, String lastName) {
        return DataFakerSpecialUtils.generateEmail(firstName, lastName, DEFAULT_DOMAIN);
    }

    public static String generateEmail(String firstName, String lastName, String domain) {
        return firstName + "." + lastName + "+" + DataFakerGeneralUtils.randString(5, 10) + "@" + domain;
    }

    public static String generateInitials(String... args) {
        String initials = "";
        for (String arg: args) 
            initials += arg.split("")[0];
        return initials;
    }
}
