package life.genny.datagenerator.service;

import life.genny.datagenerator.data.proxy.KeycloakAuthProxy;
import life.genny.datagenerator.model.json.KeycloakAuthResponse;
import life.genny.datagenerator.model.json.KeycloakUser;
import life.genny.datagenerator.utils.Utils;
import life.genny.datagenerator.utils.exception.GeneratorException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.WebApplicationException;
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

    private KeycloakAuthResponse auth = null;

    private KeycloakAuthResponse signIn() {
        MultivaluedMap<String, Object> form = new MultivaluedHashMap<>();
        form.putSingle("username", getKeycloakAdminUsername());
        form.putSingle("password", getKeycloakAdminPassword());
        form.putSingle("grant_type", "password");
        form.putSingle("client_id", getClientId());
        form.putSingle("client_secret", getCredentialSecret());

        try {
            return getKeycloakAuthProxy().signIn(
                    getRealmName(),
                    form
            );
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    private KeycloakAuthResponse refreshToken() {
        MultivaluedMap<String, Object> form = new MultivaluedHashMap<>();
        form.putSingle("refresh_token", auth.getRefreshToken());
        form.putSingle("grant_type", "refresh_token");
        form.putSingle("client_id", getClientId());
        form.putSingle("client_secret", getCredentialSecret());

        try {
            return getKeycloakAuthProxy().refreshToken(
                    getRealmName(),
                    form
            );
        } catch (WebApplicationException wa) {
            if (wa.getResponse().getStatus() >= 400 && wa.getResponse().getStatus() <= 499) {
                return signIn();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    private <E, F> E executeAuthenticatedRequest(OnRequestListener<E, F> listener) {
        if (auth == null) {
            auth = signIn();
        }
        if (auth == null) return null;
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
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    public KeycloakUser registerUserToKeycloak(String firstName, String lastName, String email, String username) {
        final KeycloakUser user = new KeycloakUser(username, firstName, lastName, email);
        user.setEnabled(true);
        user.setEmailVerified(true);

        boolean result = Boolean.TRUE.equals(executeAuthenticatedRequest(new OnRequestListener<>(user) {
            @Override
            public Boolean onRequest(String bearerToken, KeycloakUser user) {
                try {
                    getKeycloakAuthProxy().createUser(getRealmName(), bearerToken, user);
                    return true;
                } catch (WebApplicationException webApplicationException) {
                    if (webApplicationException.getResponse().getStatus() == 409) {
                        return false;
                    } else {
                        throw webApplicationException;
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    return false;
                }
            }
        }));

        if (result) {
            return getRegisteredUserFromKeycloak(user.getEmail());
        } else return null;
    }

    public void deleteUserKeycloak(String userId) {
        executeAuthenticatedRequest(new OnRequestListener<Void, String>(userId) {
            @Override
            Void onRequest(String bearerToken, String s) {
                getKeycloakAuthProxy().deleteUser(getRealmName(), bearerToken, s);
                return null;
            }
        });
    }

    public KeycloakUser getRegisteredUserFromKeycloak(String email) {
        return executeAuthenticatedRequest(new OnRequestListener<>(email) {
            @Override
            KeycloakUser onRequest(String bearerToken, String email) {
                List<KeycloakUser> users = getKeycloakAuthProxy().getUser(getRealmName(), bearerToken, email, 1);
                return Utils.findByEmail(users, email);
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
