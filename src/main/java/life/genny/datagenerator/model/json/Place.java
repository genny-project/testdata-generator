package life.genny.datagenerator.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Place {

    @JsonProperty("geometry")
    private Geometry geometry;
    @JsonProperty("icon")
    private String icon;
    @JsonProperty("icon_background_color")
    private String iconBackgroundColor;
    @JsonProperty("icon_mask_base_uri")
    private String iconMaskBaseUri;
    @JsonProperty("name")
    private String name;
    @JsonProperty("place_id")
    private String placeId;
    @JsonProperty("reference")
    private String reference;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("types")
    private List<String> types = null;
    @JsonProperty("vicinity")
    private String vicinity;
    @JsonProperty("business_status")
    private String businessStatus;
    @JsonProperty("rating")
    private Integer rating;
    @JsonProperty("user_ratings_total")
    private Integer userRatingsTotal;
    @JsonProperty("price_level")
    private Integer priceLevel;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonIgnore
    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @JsonIgnore
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @JsonIgnore
    public String getIconBackgroundColor() {
        return iconBackgroundColor;
    }

    public void setIconBackgroundColor(String iconBackgroundColor) {
        this.iconBackgroundColor = iconBackgroundColor;
    }

    @JsonIgnore
    public String getIconMaskBaseUri() {
        return iconMaskBaseUri;
    }

    public void setIconMaskBaseUri(String iconMaskBaseUri) {
        this.iconMaskBaseUri = iconMaskBaseUri;
    }

    @JsonIgnore
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @JsonIgnore
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @JsonIgnore
    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @JsonIgnore
    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @JsonIgnore
    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    @JsonIgnore
    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    @JsonIgnore
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @JsonIgnore
    public Integer getUserRatingsTotal() {
        return userRatingsTotal;
    }

    public void setUserRatingsTotal(Integer userRatingsTotal) {
        this.userRatingsTotal = userRatingsTotal;
    }

    @JsonIgnore
    public Integer getPriceLevel() {
        return priceLevel;
    }


    public void setPriceLevel(Integer priceLevel) {
        this.priceLevel = priceLevel;
    }

    @JsonIgnore
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }


    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
