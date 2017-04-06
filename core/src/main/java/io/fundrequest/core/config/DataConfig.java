package io.fundrequest.core.config;

import io.fundrequest.core.MvpApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EntityScan(
        basePackageClasses = {MvpApplication.class, Jsr310JpaConverters.class}
)
@Configuration
public class DataConfig {
}
