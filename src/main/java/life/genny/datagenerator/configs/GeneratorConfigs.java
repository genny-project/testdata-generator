package life.genny.datagenerator.configs;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "data.generator")
public interface GeneratorConfigs {
    String totalGeneration();

    String maxThread();

    String recordsPerThread();
}
