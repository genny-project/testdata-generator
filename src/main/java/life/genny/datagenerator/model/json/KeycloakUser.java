package life.genny.datagenerator.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeycloakUser {
    @JsonProperty("id")
    private String id;
    @JsonProperty("createdTimestamp")
    private long createdTimestamp;
    @JsonProperty("username")
    private String username;
    @JsonProperty("enabled")
    private boolean enabled;
    @JsonProperty("totp")
    private boolean totp;
    @JsonProperty("emailVerified")
    private boolean emailVerified;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("disableableCredentialTypes")
    private List<Object> disableableCredentialTypes = null;
    @JsonProperty("requiredActions")
    private List<Object> requiredActions = null;
    @JsonProperty("notBefore")
    private int notBefore;
    @JsonProperty("access")
    private KeycloakUserAccess access;

    public KeycloakUser() {
    }

    public KeycloakUser(String username, String firstName, String lastName, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isTotp() {
        return totp;
    }

    public void setTotp(boolean totp) {
        this.totp = totp;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Object> getDisableableCredentialTypes() {
        return disableableCredentialTypes;
    }

    public void setDisableableCredentialTypes(List<Object> disableableCredentialTypes) {
        this.disableableCredentialTypes = disableableCredentialTypes;
    }

    public List<Object> getRequiredActions() {
        return requiredActions;
    }

    public void setRequiredActions(List<Object> requiredActions) {
        this.requiredActions = requiredActions;
    }

    public int getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(int notBefore) {
        this.notBefore = notBefore;
    }

    public KeycloakUserAccess getAccess() {
        return access;
    }

    public void setAccess(KeycloakUserAccess access) {
        this.access = access;
    }
}
