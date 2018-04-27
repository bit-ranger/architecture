package com.rainyalley.architecture.config;

import com.didispace.swagger.EnableSwagger2Doc;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableSwagger2Doc
@Configuration
public class SwaggerConfig extends WebMvcConfigurerAdapter {


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/doc/v2/api-docs", "/v2/api-docs");
        registry.addRedirectViewController("/doc/configuration/ui", "/configuration/ui");
        registry.addRedirectViewController("/doc/configuration/security", "/configuration/security");
        registry.addRedirectViewController("/doc/swagger-resources", "/swagger-resources");
        registry.addRedirectViewController("/doc", "/swagger-ui.html");
        registry.addRedirectViewController("/doc/", "/swagger-ui.html");
    }

}
