package life.genny.datagenerator.model;

public class Address implements Entity {
    private Long id;
    private String jsonData;

    public Address() {
    }

    public Address(Long id, String jsonData) {
        this.id = id;
        this.jsonData = jsonData;
    }

    public Address(String jsonData) {
        this.jsonData = jsonData;
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
