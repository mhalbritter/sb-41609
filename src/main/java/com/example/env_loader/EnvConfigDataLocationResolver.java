package com.example.env_loader;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.context.config.ConfigDataLocation;
import org.springframework.boot.context.config.ConfigDataLocationNotFoundException;
import org.springframework.boot.context.config.ConfigDataLocationResolver;
import org.springframework.boot.context.config.ConfigDataLocationResolverContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.io.support.SpringFactoriesLoader;

/**
 * @author Moritz Halbritter
 */
class EnvConfigDataLocationResolver implements ConfigDataLocationResolver<EnvConfigDataResource> {

    private static final String PREFIX = "env:";

    private static final Pattern EXTENSION_HINT_PATTERN = Pattern.compile("^(.*)\\[(\\.\\w+)](?!\\[)$");

    private static final String DEFAULT_EXTENSION = ".properties";

    private final List<PropertySourceLoader> loaders;

    EnvConfigDataLocationResolver() {
        this.loaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class,
                getClass().getClassLoader());
    }

    @Override
    public boolean isResolvable(ConfigDataLocationResolverContext context, ConfigDataLocation location) {
        return location.hasPrefix(PREFIX);
    }

    @Override
    public List<EnvConfigDataResource> resolve(ConfigDataLocationResolverContext context, ConfigDataLocation location) throws ConfigDataLocationNotFoundException, ConfigDataResourceNotFoundException {
        String value = location.getNonPrefixedValue(PREFIX);
        Matcher matcher = EXTENSION_HINT_PATTERN.matcher(value);
        String extension = getExtension(matcher);
        String variableName = getVariableName(matcher, value);
        PropertySourceLoader loader = getLoader(extension);
        if (hasEnvVariable(variableName)) {
            return List.of(new EnvConfigDataResource(location, variableName, extension, loader));
        }
        if (location.isOptional()) {
            return Collections.emptyList();
        }
        throw new ConfigDataLocationNotFoundException(location, "Environment variable '%s' is not set".formatted(variableName), null);
    }

    private PropertySourceLoader getLoader(String extension) {
        if (extension == null) {
            extension = DEFAULT_EXTENSION;
        }
        if (extension.startsWith(".")) {
            extension = extension.substring(1);
        }
        for (PropertySourceLoader loader : this.loaders) {
            for (String supportedExtension : loader.getFileExtensions()) {
                if (supportedExtension.equalsIgnoreCase(extension)) {
                    return loader;
                }
            }
        }
        throw new IllegalStateException("File extension '%s' is not known to any PropertySourceLoader".formatted(extension));
    }

    private boolean hasEnvVariable(String variableName) {
        return System.getenv(variableName) != null;
    }

    private String getVariableName(Matcher matcher, String value) {
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return value;
    }

    private String getExtension(Matcher matcher) {
        if (matcher.matches()) {
            return matcher.group(2);
        }
        return null;
    }


}
