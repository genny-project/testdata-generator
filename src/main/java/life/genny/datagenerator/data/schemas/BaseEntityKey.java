package life.genny.datagenerator.data.schemas;

public class BaseEntityKey implements MessageKey {

    public static final String BE_KEY_DELIMITER = ":";

    private String realm;
    private String code;

    public BaseEntityKey() {
    }

    public BaseEntityKey(String realm, String code) {
        this.realm = realm;
        this.code = code;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return getKeyString();
    }

    @Override
    public String getKeyString() {
        return this.realm + getDelimiter() + code;
    }

    @Override
    public MessageKey fromKey(String key) {
        String[] args = key.split(getDelimiter());
        return new BaseEntityKey(args[0], args[1]);
    }

    @Override
    public String getDelimiter() {
        return BE_KEY_DELIMITER;
    }

    @Override
    public String[] getComponents() {
        return MessageKey.super.getComponents();
    }

    @Override
    public String getEntityCode() {
        return getComponents()[1];
    }
}
