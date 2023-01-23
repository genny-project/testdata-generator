package life.genny.datagenerator;

public class Regex {
    /**
     * Regular-Expression construct
     */
    public static final String ALL = ".";
    public static final String DIGIT_ONLY = "[0-9]";
    public static final String NON_DIGIT = "[^0-9]";
    public static final String WHITESPACE = "[ \\t\\n\\x0B\\f\\r]";
    public static final String NON_WHITESPACE = "[^ \\t\\n\\x0B\\f\\r]";
    public static final String WORD_CHARS = "[A-Za-z_0-9]";
    public static final String NON_WORD_CHARS = "[^A-Za-z0-9]";
    public static final String ALPHABET = "[A-Za-z]";
    public static final String NON_ALPHABET = "[^A-Za-z]";
    public static final String ASTERISK = "*";
    public static final String PLUS = "+";

    /**
     * Custom Regular-Expression Pattern
     */
    public static final String TEXT_PARAGRAPH_REGEX = "(\\w{5,20}[ ]{1}){80,120}";
    public static final String TOP_LEVEL_DOMAIN_REGEX = "(com|au|io)";
    public static final String WEBSITE_URL_REGEX = "((http(s)?://)?www\\.)?([a-zA-Z0-9]+)(\\.[a-z]+)+";
    public static final String CUSTOM_EMAIL_REGEX = "[a-zA-Z0-9]+(\\.[a-zA-Z0-9]*)?(\\@gmail\\.(com|au|io))";
    public static final String PHONE_REGEX = "(^\\+[1-9]{1,3})([0-9]{9,11})$";
    public static final String GENDER_REGEX = "(MALE|FEMALE|OTHER|PREFER NOT TO SAY)";
    public static final String STUDENT_ID_REGEX = "\\d{6,10}";
    public static final String COMPLETE_STATUS_REGEX = "\\(Complete\\)|\\(Incomplete\\)";
    public static final String AGREE_REGEX = "(SEL_YES|SEL_NO)";
    public static final String YOUTUBE_URL_REGEX = "(http://|https://)?(www\\.)?(youtube\\.com|youtu\\.be)\\/(watch)?(\\?v=)?"
            + ALPHABET + "{6}";
    public static final String SELECTION_REGEX = "^(SEL)(\\_[A-Z]{4,8}){1,4}";
    public static final String HTML_TAG_REGEX = "label|h1|p|strong|em";
    public static final String LOCALE_REGEX = "en-(US|GB|AU|NZ|SG)";
}
