package life.genny.datagenerator.utils;

public class Email {
    private String username;
    private String host;
    private String address;

    public Email(String username, String host) {
        this.username = username;
        this.host = host;
        this.address = username + "@" + host;
    }

    public String toString() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        this.address = username + "@" + host;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
        this.address = username + "@" + host;
    }
}
