package life.genny.datagenerator.utils;

import life.genny.datagenerator.model.json.KeycloakUser;

import java.util.List;

public class Utils {
    private Utils() {
        throw new IllegalArgumentException("Class %s can't be initiate".formatted(this.getClass().getName()));
    }

    public static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static KeycloakUser findBuEmail(List<KeycloakUser> users, String email) {
        for (KeycloakUser us : users) {
            if (us.getEmail().equals(email)) {
                return us;
            }
        }
        return null;
    }
}
