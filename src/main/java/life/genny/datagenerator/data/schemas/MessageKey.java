package life.genny.datagenerator.data.schemas;

import java.io.Serializable;

public interface MessageKey extends Serializable {
    String getKeyString();

    MessageKey fromKey(String key);

    String getDelimiter();

    default String[] getComponents() {
        return getKeyString().split(getDelimiter());
    }

    String getEntityCode();
}
