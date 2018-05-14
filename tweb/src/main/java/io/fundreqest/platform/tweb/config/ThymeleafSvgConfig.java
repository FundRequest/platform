package io.fundreqest.platform.tweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeleafSvgConfig {

    @Bean
    public ITemplateResolver svgTemplateResolver() {
        final SpringResourceTemplateResolver svgTemplateResolver = new SpringResourceTemplateResolver();
        svgTemplateResolver.setPrefix("classpath:/templates/svg/");
        svgTemplateResolver.setTemplateMode("XML");
        svgTemplateResolver.setCharacterEncoding("UTF-8");
        return svgTemplateResolver;
    }

    @Bean
    public ThymeleafViewResolver svgViewResolver(final SpringTemplateEngine templateEngine) {
        final ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine);
        thymeleafViewResolver.setOrder(0);
        thymeleafViewResolver.setCharacterEncoding("UTF-8");
        thymeleafViewResolver.setContentType("image/svg+xml");
        thymeleafViewResolver.setViewNames(new String[] {"*.svg"});
        return thymeleafViewResolver;
    }
}
