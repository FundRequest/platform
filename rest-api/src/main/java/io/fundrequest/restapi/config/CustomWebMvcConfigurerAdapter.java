package io.fundrequest.restapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CustomWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Value("#{'${cors.allowed-origins}'.split(',')}")
    private String[] allowedOrigins;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/index.html");
        registry.addViewController("/api").setViewName("redirect:/api/");
        registry.addViewController("/api/").setViewName("redirect:/api/index.html");
        registry.addViewController("/api/docs").setViewName("redirect:/api/docs/");
        registry.addViewController("/api/docs/").setViewName("forward:/api/docs/index.html");
        registry.addViewController("/api/login").setViewName("redirect:/api/login/");
        registry.addViewController("/api/login/").setViewName("forward:/api/login/index.html");
        registry.addViewController("/api/activity").setViewName("redirect:/api/activity/");
        registry.addViewController("/api/activity/").setViewName("forward:/api/activity/index.html");
        super.addViewControllers(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .maxAge(3600);
    }

}