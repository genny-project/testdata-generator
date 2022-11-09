package life.genny.datagenerator.data.schemas;

public class BaseEntityAttributeKey implements MessageKey {

    public static final String BEA_KEY_DELIMITER = ":";

    private String baseEntityCode;
    private String attributeCode;
    private String realm;

    public BaseEntityAttributeKey() {
    }

    public BaseEntityAttributeKey(String baseEntityCode, String attributeCode, String realm) {
        this.baseEntityCode = baseEntityCode;
        this.attributeCode = attributeCode;
        this.realm = realm;
    }

    public String getBaseEntityCode() {
        return baseEntityCode;
    }

    public void setBaseEntityCode(String baseEntityCode) {
        this.baseEntityCode = baseEntityCode;
    }

    public String getAttributeCode() {
        return attributeCode;
    }

    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    @Override
    public String getKeyString() {
        return realm + getDelimiter() + baseEntityCode + getDelimiter() + attributeCode;
    }

    @Override
    public MessageKey fromKey(String key) {
        String[] args = key.split(getDelimiter());
        return new BaseEntityAttributeKey(args[0], args[1], args[2]);
    }

    @Override
    public String getDelimiter() {
        return BEA_KEY_DELIMITER;
    }

    @Override
    public String[] getComponents() {
        return MessageKey.super.getComponents();
    }

    @Override
    public String getEntityCode() {
        return baseEntityCode + getDelimiter() + attributeCode;
    }
}
