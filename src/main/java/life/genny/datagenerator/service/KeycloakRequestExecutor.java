package life.genny.datagenerator.service;

import life.genny.datagenerator.model.json.KeycloakAuthResponse;
import life.genny.datagenerator.model.json.KeycloakUser;
import life.genny.datagenerator.utils.Utils;
import life.genny.datagenerator.utils.exception.GeneratorException;
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

    private <E, F> E executeAuthenticatedRequest(OnRequestListener<E, F> listener) {
        if (auth == null) {
            auth = signIn();
        }
        if (auth == null) return null;
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        try {
            return listener.onRequest("Bearer " + auth.getAccessToken(), listener.getInput());
        } catch (WebApplicationException wa) {
            if (wa.getResponse().getStatus() == 401) {
                auth = refreshToken();
                return executeAuthenticatedRequest(listener);
            } else {
                LOGGER.error(wa.getMessage());
                return null;
            }
        } catch (GeneratorException ex) {
            LOGGER.error(ex.getMessage());
            return null;
        }
    }

    public KeycloakUser registerUserToKeycloak(String firstName, String lastName, String email, String username) {
        if (keycloakService.checkIsEmailAvailable(email)) {
            return null;
        }
        keycloakService.putEmail(email);
        final KeycloakUser user = new KeycloakUser(username, firstName, lastName, email);
        user.setEnabled(true);
        user.setEmailVerified(true);

        boolean result = Boolean.TRUE.equals(executeAuthenticatedRequest(new OnRequestListener<>(user) {
            @Override
            public Boolean onRequest(String bearerToken, KeycloakUser user) {
                try {
                    keycloakService.getKeycloakAuthProxy().createUser(keycloakService.getRealmName(), bearerToken, user);
                    return true;
                } catch (WebApplicationException webApplicationException) {
                    if (webApplicationException.getResponse().getStatus() == 409) {
                        keycloakService.putEmail(user.getEmail());
                        return false;
                    } else {
                        throw webApplicationException;
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    return false;
                }
            }
        }));

        if (result) {
            return executeAuthenticatedRequest(new OnRequestListener<>(user.getEmail()) {
                @Override
                KeycloakUser onRequest(String bearerToken, String email) {
                    List<KeycloakUser> users = keycloakService.getKeycloakAuthProxy().getUser(keycloakService.getRealmName(), bearerToken, email);
                    return Utils.findBuEmail(users, email);
                }
            });
        } else return null;
    }

    public void deleteUserKeycloak(String userId) {
        executeAuthenticatedRequest(new OnRequestListener<Void, String>(userId) {
            @Override
            Void onRequest(String bearerToken, String s) {
                keycloakService.getKeycloakAuthProxy().deleteUser(keycloakService.getRealmName(), bearerToken, s);
                return null;
            }
        });
    }

    public KeycloakUser getRegisteredUserFromKeycloak(String email) {
        return executeAuthenticatedRequest(new OnRequestListener<>(email) {
            @Override
            KeycloakUser onRequest(String bearerToken, String email) {
                List<KeycloakUser> users = keycloakService.getKeycloakAuthProxy().getUser(keycloakService.getRealmName(), bearerToken, email);
                return Utils.findBuEmail(users, email);
            }
        });
    }

    public abstract static class OnRequestListener<O, I> {
        private final I input;

        protected OnRequestListener(I input) {
            this.input = input;
        }

        public I getInput() {
            return input;
        }

        abstract O onRequest(String bearerToken, I input) throws GeneratorException;
    }
}
