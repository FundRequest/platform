package io.fundrequest.platform.profile.config;

import io.fundrequest.db.infrastructure.SpringSecurityAuditorAware;
import io.fundrequest.platform.profile.ProfileApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EntityScan(
        basePackageClasses = {ProfileApplication.class, Jsr310JpaConverters.class}
)
@Configuration
public class ProfileDataConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }
}