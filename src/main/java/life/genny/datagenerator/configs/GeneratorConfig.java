package life.genny.datagenerator.configs;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "data.generator")
public interface GeneratorConfig {
    int totalGeneration();

    int maxThread();

    int recordsPerThread();
}
