package com.rainyalley.architecture.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Configuration
@ConfigurationProperties(prefix = "mail")
@Validated
public class MailProperties {

    public static class Smtp {
        private boolean auth;
        private boolean starttlsEnable;
        // ... getters and setters
    }

    @NotBlank private String host;
    private int port;
    private String from;
    private String username;
    private String password;

    @NotNull
    private Smtp smtp;
    // ... getters and setters

}
