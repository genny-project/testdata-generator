package life.genny.datagenerator.configs;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "data.generator")
public interface GeneratorConfig {
    String totalGeneration();

    int maxThread();

    int recordsPerThread();
}
