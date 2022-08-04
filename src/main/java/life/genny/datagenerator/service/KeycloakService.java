package life.genny.datagenerator.service;

import io.vertx.ext.web.handler.HttpException;
import life.genny.datagenerator.data.proxy.KeycloakAuthProxy;
import life.genny.datagenerator.model.json.KeycloakAuthResponse;
import life.genny.datagenerator.model.json.KeycloakUser;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

@ApplicationScoped
public class KeycloakService {
    private static final Logger LOGGER = Logger.getLogger(KeycloakService.class);

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

    private KeycloakAuthResponse auth;

    private KeycloakAuthResponse signIn() {
        MultivaluedMap<String, Object> form = new MultivaluedHashMap<>();
        form.putSingle("username", keycloakAdminUsername);
        form.putSingle("password", keycloakAdminPassword);
        form.putSingle("grant_type", "password");
        form.putSingle("client_id", clientId);
        form.putSingle("client_secret", credentialSecret);

        try {
            return keycloakAuthProxy.signIn(
                    realmName,
                    form
            );
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return null;
    }

    private KeycloakAuthResponse refreshToken() {
        MultivaluedMap<String, Object> form = new MultivaluedHashMap<>();
        form.putSingle("refresh_token", auth.getRefreshToken());
        form.putSingle("grant_type", "refresh_token");
        form.putSingle("client_id", clientId);
        form.putSingle("client_secret", credentialSecret);

        try {
            return keycloakAuthProxy.refreshToken(
                    realmName,
                    form
            );
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return null;
    }

    private <E> E executeAuthenticatedRequest(OnRequestListener<E> listener) {
        if (auth == null) {
            auth = signIn();
        }
        if (auth == null) return null;
        try {
            return listener.onRequest("Bearer " + auth.getAccessToken());
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
            auth = refreshToken();
            if (e instanceof HttpException) {
                return executeAuthenticatedRequest(listener);
            } else return null;
        }
    }

    public KeycloakUser registerUserToKeycloak(String firstName, String lastName, String email, String username) {
        final KeycloakUser user = new KeycloakUser(username, firstName, lastName, email);
        user.setEnabled(true);
        user.setEmailVerified(true);
        return executeAuthenticatedRequest(bearerToken -> {
            keycloakAuthProxy.createUser(realmName, bearerToken, user);
            List<KeycloakUser> users = keycloakAuthProxy.getUser(realmName, bearerToken, user.getEmail());
            return users.get(0);
        });
    }

    interface OnRequestListener<E> {
        E onRequest(String bearerToken);
    }
}
