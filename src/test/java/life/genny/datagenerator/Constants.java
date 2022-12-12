package life.genny.datagenerator;

public class Constants {
    public static final String IGNORE = "YOU CAN IGNORE THIS!!";
    public static final String EMAIL_REGEX = "^(^[a-zA-Z0-9._-]*)([+]?)([a-zA-Z0-9._-]*)([@])([a-zA-Z0-9._-]*)([.])([a-zA-Z.]{2,6})$";
    public static final String LINKEDIN_URL_REGEX = "(http://|https://)?[a-z]{2,3}\\.linkedin\\.com\\/.*$";
    public static final String HOURS_REGEX = "^((2[0-3]|[1][0-9]|[1-9])([.][0-9]{1})*)$";
    public static final String NUMBER_REGEX = "^-?\\d{1,10}$";
    public static final String DECIMAL_REGEX = "^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$";
}
