package io.fundrequest.core.config;

import io.fundrequest.core.FundRequestCore;
import io.fundrequest.db.infrastructure.SpringSecurityAuditorAware;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EntityScan(
        basePackageClasses = {FundRequestCore.class, Jsr310JpaConverters.class}
)
@Configuration
public class DataConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }
}
