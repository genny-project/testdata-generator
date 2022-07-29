package life.genny.datagenerator.utils;

import com.github.javafaker.Faker;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GeneratorUtils {

    public static final boolean DEFAULT_INFERRED = false;
    public static final boolean DEFAULT_PRIVACY_FLAG = false;
    public static final boolean DEFAULT_READ_ONLY = false;
    public static final String DEFAULT_REALM = "Genny";

    public static final String[] GENDER = {"MALE", "FEMALE", "OTHER"};
    public static final String COMPLETED = "Completed";
    public static final String AVAILABLE = "AVAILABLE";
    public static final String ACTIVE = "ACTIVE";

    private static final Faker faker = new Faker();

    private static int generateRandomNum(int size) {
        Random random = new Random();
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


    public static String generateEmail(String firstName, String lastName) {
        int index = (int) Math.floor(Math.random() * 2);
        String email = "";
        EmailOptions value = EmailOptions.values()[index];

        switch (value) {
            case OPTION1:
                email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com";
                break;
            case OPTION2:
                int randomNum = (int) Math.floor(Math.random() * 10);
                email = lastName.toLowerCase() + randomNum + "@gmail.com";
                break;
            default:
                email = firstName.toLowerCase() + lastName.toLowerCase() + "@gmail.com";
                break;
        }

        return email;
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

    public static HashMap<String, String> generateFullAddress() {
        HashMap<String, String> address = new HashMap<>();
        address.put("street", faker.address().streetAddress());
        address.put("country", faker.address().country());
        address.put("city", faker.address().city());
        address.put("province", faker.address().state());
        address.put("full", faker.address().fullAddress());
        address.put("second", faker.address().secondaryAddress());
        String zipCode = faker.address().zipCode();
        if (zipCode.length() < 6) address.put("zipCode", zipCode);
        else address.put("zipCode", zipCode.substring(0, 5));
        return address;
    }


    public static String generateGender() {
        Random random = new Random();
        int i = random.nextInt(3);
        return GENDER[i];
    }

    public static String generateLinkedInURL(String firstName, String lastName) {
        String linkedInBaseUrl = "https://www.linkedin.com/in/";
        return linkedInBaseUrl + firstName + lastName;
    }

    public static String generatePhoneNumber() {
        Random random = new Random();
        int part1 = random.nextInt(600) + 100;
        int part2 = random.nextInt(641) + 100;
        int part3 = random.nextInt(8999) + 1000;
        return part1 + "" + part2 + "" + part3;
    }

    public static String generateImageUrl(List<String> images) {
        return images.get(generateRandomNum(images.size()));
    }

//    public static File generateImage() throws IOException {
//        Random random = new Random();
//        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//        Graphics2D g2d = bufferedImage.createGraphics();
//        g2d.setColor(Color.red);
//        File file = new File("image.jpg");
//        ImageIO.write(bufferedImage, "jpg", file);
//        return file;
//    }

    public static HashMap<String, String> generateLngLat() {
        HashMap<String, String> geoLocation = new HashMap<>();
        double minLat = 0.00;
        double maxLat = 30.00;
        double minLng = 40.00;
        double maxLng = 55.00;
        double latitude = minLat + (Math.random() * ((maxLat - minLat) + 1));
        double longitude = minLng + (Math.random() * ((maxLng - minLng) + 1));
        DecimalFormat dFormat = new DecimalFormat("#.#######");
        geoLocation.put("latitude", dFormat.format(latitude));
        geoLocation.put("longitude", dFormat.format(longitude));
        return geoLocation;
    }

}
