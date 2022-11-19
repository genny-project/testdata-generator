package life.genny.datagenerator.configs;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "keycloak")
public interface KeycloakCatalogueConfig {

    String serverBaseUrl();

    String clientId();

    String credentialsSecret();

    String username();

    String password();

}
