package io.fundrequest.core.config.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.AuthenticationNameUserIdSource;

import javax.sql.DataSource;

//@Configuration
public class SocialConfig extends SocialConfigurerAdapter {


//    @Autowired
//    private DataSource dataSource;
//
//
//    @Override
//    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
//        return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
//    }
//
//    @Override
//    public UserIdSource getUserIdSource() {
//        return new AuthenticationNameUserIdSource();
//    }
//
//    @Bean
//    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository) {
//        return new ProviderSignInUtils(connectionFactoryLocator, usersConnectionRepository);
//    }


}
