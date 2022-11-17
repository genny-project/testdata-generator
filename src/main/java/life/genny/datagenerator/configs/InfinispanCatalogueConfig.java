package life.genny.datagenerator.configs;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "data.infinispan")
public interface InfinispanCatalogueConfig {

    String connectionUrl();

    String username();

    String password();

    String dialect();

    String driver();

    @WithDefault("genny")
    String packageName();

    @WithDefault("baseentites")
    String baseEntityPath();

    @WithDefault("attributes")
    String baseEntityAttributePath();

}
