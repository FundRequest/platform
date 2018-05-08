package io.fundreqest.platform.tweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeleafSvgConfig {

    @Bean
    public ITemplateResolver svgTemplateResolver() {
        SpringResourceTemplateResolver svgTemplateResolver = new SpringResourceTemplateResolver();
        svgTemplateResolver.setPrefix("classpath:/templates/svg/");
        svgTemplateResolver.setSuffix(".svg");
        svgTemplateResolver.setTemplateMode("XML");
        svgTemplateResolver.setCharacterEncoding("UTF-8");

        return svgTemplateResolver;
    }
}
