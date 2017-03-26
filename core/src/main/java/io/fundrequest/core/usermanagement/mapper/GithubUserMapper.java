package io.fundrequest.core.usermanagement.mapper;

import io.fundrequest.core.usermanagement.domain.GithubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Optional;

@Component
public class GithubUserMapper {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    public Optional<GithubUser> mapToGithubUser(Authentication authentication) {
        try {
            if (authentication instanceof OAuth2Authentication) {
                Authentication userAuthentication = ((OAuth2Authentication) authentication).getUserAuthentication();
                Object details = userAuthentication.getDetails();
                if (details instanceof LinkedHashMap) {
                    String login = ((LinkedHashMap) details).getOrDefault("login", "").toString();
                    String id = ((LinkedHashMap) details).getOrDefault("id", "").toString();
                    String avatarUrl = ((LinkedHashMap) details).getOrDefault("avatar_url", "").toString();
                    String url = ((LinkedHashMap) details).get("url").toString();
                    String name = ((LinkedHashMap) details).get("name").toString();
                    String email = ((LinkedHashMap) details).get("email").toString();
                    return Optional.ofNullable(
                            new GithubUser()
                                    .setAvatarUrl(avatarUrl)
                                    .setLogin(login)
                                    .setUrl(url)
                                    .setName(name)
                                    .setEmail(email)
                                    .setId(Long.valueOf(id))
                                    .setName(name)
                    );
                }
            }
        } catch (Exception ex) {
            LOG.info("unable to map user", ex);
        }
        return Optional.empty();
    }

}