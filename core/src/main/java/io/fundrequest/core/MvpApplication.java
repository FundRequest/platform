package io.fundrequest.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

@SpringBootApplication
public class MvpApplication {

    private static final Logger logger = LoggerFactory.getLogger(MvpApplication.class);


    public static void main(final String[] args) throws Exception {
        final ApplicationContext app = configureApplication(new SpringApplicationBuilder()).run(args);
        final Environment env = app.getEnvironment();
        logger.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "External: \thttp://{}:{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                env.getActiveProfiles());
    }

    private static SpringApplicationBuilder configureApplication(final SpringApplicationBuilder builder) {
        return builder.sources(MvpApplication.class);
    }


}
