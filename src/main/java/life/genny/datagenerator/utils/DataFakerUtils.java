package life.genny.datagenerator.utils;

import com.mifmif.common.regex.Generex;

public class DataFakerUtils {

    public static final boolean DEFAULT_INFERRED = false;
    public static final boolean DEFAULT_PRIVACY_FLAG = false;
    public static final boolean DEFAULT_READ_ONLY = false;
    public static final String DEFAULT_REALM = "Genny";

    protected static final String[] GENDER = { "MALE", "FEMALE", "OTHER" };
    public static final String COMPLETED = "Completed";
    public static final String AVAILABLE = "AVAILABLE";
    public static final String ACTIVE = "ACTIVE";

    public static final String DEFAULT_REGEX = "[A-Za-z0-9]";
    public static final int DEFAULT_MIN_LENGTH = 5;
    public static final int DEFAULT_MAX_LENGTH = 10;
    public static final String DEFAULT_MIN_MAX_REGEX = "{" + DEFAULT_MIN_LENGTH + "," + DEFAULT_MAX_LENGTH + "}";

    public static String generateStringFromRegex(String regex) {
        regex = regex.replace(".*", DEFAULT_REGEX + "{0,10}");
        regex = regex.replace(".+", DEFAULT_REGEX + "{1,10}");
        Generex generator = new Generex(regex);
        String randString = generator.random();
        randString = randString.replaceAll("[$^]*", "");
        return randString;
    }
}