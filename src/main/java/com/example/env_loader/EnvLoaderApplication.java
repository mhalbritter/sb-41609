package com.example.env_loader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;

@SpringBootApplication
public class EnvLoaderApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(EnvLoaderApplication.class, args);
        String property = context.getEnvironment().getProperty("app.value1");
        System.out.println("app.value1 = " + property);

        for (PropertySource<?> propertySource : context.getEnvironment().getPropertySources()) {
            System.out.println(propertySource.getName());
        }
    }

}
