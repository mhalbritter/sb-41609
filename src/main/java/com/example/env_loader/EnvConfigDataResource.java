package com.example.env_loader;

import java.util.Objects;

import org.springframework.boot.context.config.ConfigDataLocation;
import org.springframework.boot.context.config.ConfigDataResource;
import org.springframework.boot.env.PropertySourceLoader;

/**
 * @author Moritz Halbritter
 */
class EnvConfigDataResource extends ConfigDataResource {
    private final ConfigDataLocation location;
    private final String variableName;
    private final String extension;
    private final PropertySourceLoader loader;

    EnvConfigDataResource(ConfigDataLocation location, String variableName, String extension, PropertySourceLoader loader) {
        super(location.isOptional());
        this.location = location;
        this.variableName = variableName;
        this.extension = extension;
        this.loader = loader;
    }

    ConfigDataLocation getLocation() {
        return this.location;
    }

    String getVariableName() {
        return this.variableName;
    }

    String getExtension() {
        return this.extension;
    }

    PropertySourceLoader getLoader() {
        return this.loader;
    }

    @Override
    public String toString() {
        return "MyConfigDataResource[%s]".formatted(this.variableName);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnvConfigDataResource that = (EnvConfigDataResource) o;
        return Objects.equals(this.location, that.location) && Objects.equals(this.variableName, that.variableName) && Objects.equals(this.extension, that.extension) && Objects.equals(this.loader, that.loader);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.location, this.variableName, this.extension, this.loader);
    }
}
