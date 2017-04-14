package io.fundrequest.core.config;

import io.fundrequest.core.MvpApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = {MvpApplication.class})
public class FeignConfig {
}
