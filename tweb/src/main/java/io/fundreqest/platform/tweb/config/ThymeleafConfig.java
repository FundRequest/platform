package io.fundreqest.platform.tweb.config;

import com.google.common.collect.Sets;
import io.fundreqest.platform.tweb.infrastructure.thymeleaf.ProfilesDialect;
import io.fundreqest.platform.tweb.infrastructure.thymeleaf.ProfilesExpressionObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.TemplateResolver;

@Configuration
public class ThymeleafConfig {

    @Bean
    public IDialect profileDialect(final ProfilesExpressionObject profilesExpressionObject) {
        return new ProfilesDialect(profilesExpressionObject);
    }

    @Bean
    public TemplateEngine templateEngine(final TemplateResolver templateResolver, final IDialect profileDialect) {
        final SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        springTemplateEngine.setTemplateResolver(templateResolver);
        springTemplateEngine.setAdditionalDialects(Sets.newHashSet(profileDialect));
        return springTemplateEngine;
    }
}
