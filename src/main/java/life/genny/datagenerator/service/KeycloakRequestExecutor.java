package life.genny.datagenerator.service;

import life.genny.datagenerator.model.json.KeycloakAuthResponse;
import life.genny.datagenerator.model.json.KeycloakUser;
import org.jboss.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public class KeycloakRequestExecutor {
    private static final Logger LOGGER = Logger.getLogger(KeycloakRequestExecutor.class);
    private final KeycloakService keycloakService;
    private KeycloakAuthResponse auth;

    public KeycloakRequestExecutor(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    private KeycloakAuthResponse signIn() {
        MultivaluedMap<String, Object> form = new MultivaluedHashMap<>();
        form.putSingle("username", keycloakService.getKeycloakAdminUsername());
        form.putSingle("password", keycloakService.getKeycloakAdminPassword());
        form.putSingle("grant_type", "password");
        form.putSingle("client_id", keycloakService.getClientId());
        form.putSingle("client_secret", keycloakService.getCredentialSecret());

        try {
            return keycloakService.getKeycloakAuthProxy().signIn(
                    keycloakService.getRealmName(),
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
        form.putSingle("client_id", keycloakService.getClientId());
        form.putSingle("client_secret", keycloakService.getCredentialSecret());

        try {
            return keycloakService.getKeycloakAuthProxy().refreshToken(
                    keycloakService.getRealmName(),
                    form
            );
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return null;
    }

    private <E> E executeAuthenticatedRequest(OnRequestListener<E> listener) throws Exception {
        if (auth == null) {
            auth = signIn();
        }
        if (auth == null) return null;
        try {
            return listener.onRequest("Bearer " + auth.getAccessToken());
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
            if (e instanceof javax.ws.rs.WebApplicationException) {
                javax.ws.rs.WebApplicationException wa = (WebApplicationException) e;
                if (wa.getResponse().getStatus() == 401) {
                    auth = refreshToken();
                    return executeAuthenticatedRequest(listener);
                }
            }
            return null;
        }
    }

    public KeycloakUser registerUserToKeycloak(String firstName, String lastName, String email, String username) throws Exception {
        final KeycloakUser user = new KeycloakUser(username, firstName, lastName, email);
        user.setEnabled(true);
        user.setEmailVerified(true);
        return executeAuthenticatedRequest(bearerToken -> {
            keycloakService.getKeycloakAuthProxy().createUser(keycloakService.getRealmName(), bearerToken, user);
            List<KeycloakUser> users = keycloakService.getKeycloakAuthProxy().getUser(keycloakService.getRealmName(), bearerToken, user.getEmail());
            return users.get(0);
        });
    }

    interface OnRequestListener<E> {
        E onRequest(String bearerToken);
    }
}
