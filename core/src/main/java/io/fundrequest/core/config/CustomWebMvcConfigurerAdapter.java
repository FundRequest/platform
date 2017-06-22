package io.fundrequest.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CustomWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/docs").setViewName("redirect:/docs/");
        registry.addViewController("/docs/").setViewName("forward:/docs/index.html");
        registry.addViewController("/login").setViewName("redirect:/login/");
        registry.addViewController("/login/").setViewName("forward:/login/index.html");
        registry.addViewController("/activity").setViewName("redirect:/activity/");
        registry.addViewController("/activity/").setViewName("forward:/activity/index.html");
        super.addViewControllers(registry);
    }
}