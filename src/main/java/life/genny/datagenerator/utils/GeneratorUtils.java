package life.genny.datagenerator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import life.genny.datagenerator.model.json.AddressComponent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GeneratorUtils {

    private GeneratorUtils() {
        throw new IllegalArgumentException("Utility class " + GeneratorUtils.class.getSimpleName() + " can't be instantiate");
    }

    public static final boolean DEFAULT_INFERRED = false;
    public static final boolean DEFAULT_PRIVACY_FLAG = false;
    public static final boolean DEFAULT_READ_ONLY = false;
    public static final String DEFAULT_REALM = "Genny";

    protected static final String[] GENDER = {"MALE", "FEMALE", "OTHER"};
    public static final String COMPLETED = "Completed";
    public static final String AVAILABLE = "AVAILABLE";
    public static final String ACTIVE = "ACTIVE";

    private static final Faker faker = new Faker();

    private static final Random random = new Random();

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static int generateRandomNum(int size) {
        return random.nextInt(size);
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    public static String generateFirstName() {
        return faker.name().firstName();
    }

    public static String generateLastName() {
        return faker.address().lastName();
    }


    public static Email generateEmail(String firstName, String lastName) {
        String[] emailHost = new String[]{"gmail.com", "hotmail.com", "outlook.com", "yahoo.com"};
        String host = emailHost[random.nextInt(emailHost.length)];
        String[] separators = new String[]{".", "_", "-"};
        String separator = separators[random.nextInt(separators.length)];
        long timestamps = (new Date()).getTime();
        return new Email(firstName.toLowerCase() + separator + lastName.toLowerCase() + separator + timestamps, host);
    }

    public static Date generateDOB() throws ParseException {
        GregorianCalendar calendar = new GregorianCalendar();
        DateUtil dtUtil = new DateUtil();
        int year = dtUtil.pickRandom(1970, 2000);
        calendar.set(Calendar.YEAR, year);
        int day = dtUtil.pickRandom(1, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        calendar.set(Calendar.DAY_OF_YEAR, day);
        String generatedDate = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(generatedDate);
    }

    public static <E> E pickRandomData(List<E> data) {
        int ranInt = generateRandomNum(data.size());
        return data.get(ranInt);
    }

    public static Map<String, String> convertToMap(List<AddressComponent> components) {
        HashMap<String, String> addressMap = new HashMap<>();
        for (AddressComponent component : components) {
            for (String type : component.getTypes()) {
                if ("street_number".equals(type)) {
                    addressMap.put("street_map", component.getLongName());
                } else {
                    addressMap.put(type, component.getLongName());
                }
            }
        }
        return addressMap;
    }

    public static String generateGender() {
        int i = random.nextInt(3);
        return GENDER[i];
    }

    public static String generateLinkedInURL(String firstName, String lastName) {
        String linkedInBaseUrl = "https://www.linkedin.com/in/";
        return linkedInBaseUrl + firstName + lastName;
    }

    public static String generatePhoneNumber() {
        int part1 = random.nextInt(600) + 100;
        int part2 = random.nextInt(641) + 100;
        int part3 = random.nextInt(8999) + 1000;
        return part1 + "" + part2 + "" + part3;
    }

    public static String generateImageUrl(List<String> images) {
        return images.get(generateRandomNum(images.size()));
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        GeneratorUtils.objectMapper = objectMapper;
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public static String generateUTCTimeZone(int utcOffset) {
        if (utcOffset >= 0)
            return "UTC +" + (utcOffset / 60);
        else
            return "UTC " + (utcOffset / 60);
    }

}
