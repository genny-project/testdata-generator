package life.genny.datagenerator.utils;

import com.github.javafaker.Faker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GeneratorUtils {


    private static final Faker faker = new Faker();

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
        String zipCode = faker.address().zipCode();
        if (zipCode.length() < 6) address.put("zipCode", zipCode);
        else address.put("zipCode", zipCode.substring(0, 5));
        return address;
    }


    public static String generateGender() {
        return null;
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

    public static String generateImageUrl(String firstName, String lastName) {
        return "https://drive.google.com/" + UUID.randomUUID() + "/" + (firstName.toLowerCase()
                + lastName.toLowerCase()).replace(" ", "");
    }

}
