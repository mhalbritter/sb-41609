
package com.example.env_loader;

enum Format {
    PROPERTIES(".properties"), JSON(".json"), YAML(".yaml");

    private final String extension;

    Format(String extension) {
        this.extension = extension;
    }

    String getExtension() {
        return this.extension;
    }

    static Format getDefault() {
        return PROPERTIES;
    }

    static Format fromExtension(String extension) {
        for (Format format : values()) {
            if (format.getExtension().equals(extension)) {
                return format;
            }
        }
        return null;
    }
}
