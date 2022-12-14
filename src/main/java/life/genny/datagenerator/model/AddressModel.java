package life.genny.datagenerator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import life.genny.datagenerator.data.entity.Address;

public class AddressModel implements BaseModel<Address> {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("json_data")
    private String jsonData;

    public AddressModel() {
    }

    public AddressModel(String jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public Address toEntity() {
        return new Address(this.id, this.jsonData);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
