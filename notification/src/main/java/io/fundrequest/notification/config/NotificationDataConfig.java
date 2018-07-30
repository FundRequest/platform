package io.fundrequest.notification.config;

import io.fundrequest.db.infrastructure.SpringSecurityAuditorAware;
import io.fundrequest.notification.FundRequestNotification;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EntityScan(
        basePackageClasses = {FundRequestNotification.class, Jsr310JpaConverters.class}
)
@Configuration
public class NotificationDataConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }
}
