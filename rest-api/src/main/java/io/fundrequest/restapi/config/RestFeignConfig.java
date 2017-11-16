package io.fundrequest.restapi.config;

import io.fundrequest.restapi.RestApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = {RestApplication.class})
public class RestFeignConfig {
}
