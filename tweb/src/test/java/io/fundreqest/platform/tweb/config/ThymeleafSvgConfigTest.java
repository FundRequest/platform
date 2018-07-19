package io.fundreqest.platform.tweb.config;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ThymeleafSvgConfigTest {

    private ThymeleafSvgConfig config = new ThymeleafSvgConfig();

    @Test
    public void svgTemplateResolver() {

        final ApplicationContext applicationContext = mock(ApplicationContext.class);
        config.setApplicationContext(applicationContext);
        final SpringResourceTemplateResolver templateResolver = (SpringResourceTemplateResolver) config.svgTemplateResolver();

        assertThat(templateResolver.getPrefix()).isEqualTo("classpath:/templates/svg/");
        assertThat(templateResolver.getTemplateMode()).isEqualTo(TemplateMode.XML);
        assertThat(templateResolver.getCharacterEncoding()).isEqualTo("UTF-8");
    }

    @Test
    public void svgViewResolver() {
        final SpringTemplateEngine templateEngine = mock(SpringTemplateEngine.class);

        final ThymeleafViewResolver viewResolver = config.svgViewResolver(templateEngine);

        assertThat(viewResolver.getTemplateEngine()).isEqualTo(templateEngine);
        assertThat(viewResolver.getOrder()).isEqualTo(0);
        assertThat(viewResolver.getCharacterEncoding()).isEqualTo("UTF-8");
        assertThat(viewResolver.getContentType()).isEqualTo("image/svg+xml");
        assertThat(viewResolver.getViewNames()).isEqualTo(new String[] {"*.svg"});
    }
}
