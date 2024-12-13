package com.example.env_loader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.boot.context.config.ConfigData;
import org.springframework.boot.context.config.ConfigDataLoader;
import org.springframework.boot.context.config.ConfigDataLoaderContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.core.io.ByteArrayResource;

/**
 * @author Moritz Halbritter
 */
class EnvConfigDataLoader implements ConfigDataLoader<EnvConfigDataResource> {
    @Override
    public ConfigData load(ConfigDataLoaderContext context, EnvConfigDataResource resource) throws IOException, ConfigDataResourceNotFoundException {
        String content = System.getenv(resource.getVariableName());
        String name = String.format("Environment variable '%s' via location '%s'", resource.getVariableName(),
                resource.getLocation());
        return new ConfigData(resource.getLoader().load(name, createResource(content)));
    }

    private ByteArrayResource createResource(String content) {
        return new ByteArrayResource(content.getBytes(StandardCharsets.UTF_8));
    }
}
