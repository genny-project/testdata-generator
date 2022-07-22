package life.genny.datagenerator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BaseModel<Entity> {
    public abstract Entity toEntity();
}
