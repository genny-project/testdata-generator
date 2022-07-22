package life.genny.datagenerator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BaseModel<Entity> {
    @JsonIgnore
    protected Entity entity;

    public BaseModel() {
    }

    public BaseModel(Entity entity) {
        this.entity = entity;
    }

    public abstract Entity toEntity();
}
