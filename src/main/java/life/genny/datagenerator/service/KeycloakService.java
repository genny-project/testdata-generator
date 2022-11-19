package life.genny.datagenerator.service;

import life.genny.datagenerator.data.proxy.KeycloakAuthProxy;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KeycloakService {

    @ConfigProperty(name = "keycloak.client-id")
    String clientId;
    @ConfigProperty(name = "keycloak.credentials.secret")
    String credentialSecret;
    @ConfigProperty(name = "keycloak.realm-name")
    String realmName;
    @ConfigProperty(name = "keycloak.user.username")
    String keycloakAdminUsername;
    @ConfigProperty(name = "keycloak.user.password")
    String keycloakAdminPassword;

    @RestClient
    KeycloakAuthProxy keycloakAuthProxy;

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
}
