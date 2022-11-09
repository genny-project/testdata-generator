package life.genny.datagenerator.model;

import life.genny.datagenerator.data.schemas.MessageKey;

public interface BaseModel<E> {
    E toEntity();

    MessageKey getMessageKey();
}
