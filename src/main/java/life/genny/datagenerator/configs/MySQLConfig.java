package life.genny.datagenerator.configs;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "data.mysql")
public interface MySQLConfig {

    String host();

    String port();

    String database();

    String user();

    String password();

}
