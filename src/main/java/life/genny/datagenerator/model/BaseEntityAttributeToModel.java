package life.genny.datagenerator.model;

public interface BaseEntityAttributeToModel {
    String getAttributeCode();

    /**
     * @return the value of BaseEntityAttribute, it could be valueDate, valueBoolean, valueInteger and the others
     */
    Object getValue();

    void setValue(Object value) throws Exception;
}
