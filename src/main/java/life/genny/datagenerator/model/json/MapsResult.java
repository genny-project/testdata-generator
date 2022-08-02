package life.genny.datagenerator.model.json;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MapsResult {

    @JsonProperty("html_attributions")
    private List<Object> htmlAttributions = null;
    @JsonProperty("next_page_token")
    private String nextPageToken;
    @JsonProperty("results")
    private List<Place> results = null;
    @JsonProperty("result")
    private PlaceDetail result = null;
    @JsonProperty("status")
    private String status;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public MapsResult() {
    }

    @JsonIgnore
    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    @JsonIgnore
    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    @JsonIgnore
    public List<Place> getResults() {
        return results;
    }

    public void setResults(List<Place> results) {
        this.results = results;
    }

    public PlaceDetail getResult() {
        return result;
    }

    public void setResult(PlaceDetail result) {
        this.result = result;
    }

    @JsonIgnore
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonIgnore
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}