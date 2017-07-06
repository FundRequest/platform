package io.fundrequest.core.config;

import io.fundrequest.core.FundRequestCore;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = {FundRequestCore.class})
public class FeignConfig {
}
