package life.genny.datagenerator.service;

import life.genny.datagenerator.data.proxy.KeycloakAuthProxy;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class KeycloakService {

    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;
    @ConfigProperty(name = "quarkus.oidc.credentials.secret")
    String credentialSecret;
    @ConfigProperty(name = "keycloak.realm-name")
    String realmName;
    @ConfigProperty(name = "keycloak.user.username")
    String keycloakAdminUsername;
    @ConfigProperty(name = "keycloak.user.password")
    String keycloakAdminPassword;

    @RestClient
    KeycloakAuthProxy keycloakAuthProxy;

    private final Set<String> createdEmails = new HashSet<>();

    public String getClientId() {
        return clientId;
    }

    public String getCredentialSecret() {
        return credentialSecret;
    }

    public String getRealmName() {
        return realmName;
    }

    public String getKeycloakAdminUsername() {
        return keycloakAdminUsername;
    }

    public String getKeycloakAdminPassword() {
        return keycloakAdminPassword;
    }

    public KeycloakAuthProxy getKeycloakAuthProxy() {
        return keycloakAuthProxy;
    }

    public void putEmail(String email) {
        this.createdEmails.add(email);
    }

    public boolean checkIsEmailAvailable(String email) {
        return createdEmails.contains(email);
    }
}
