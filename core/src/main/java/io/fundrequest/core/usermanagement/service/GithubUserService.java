package io.fundrequest.core.usermanagement.service;

import io.fundrequest.core.usermanagement.domain.GithubUser;
import io.fundrequest.core.usermanagement.domain.Role;
import io.fundrequest.core.usermanagement.domain.RoleEnum;
import io.fundrequest.core.usermanagement.repository.GithubUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class GithubUserService {

    @Autowired
    private GithubUserRepository githubUserRepository;

    @Transactional
    public void upsert(final GithubUser githubUser) {
        Optional<GithubUser> one = githubUserRepository.findOne(githubUser.getId());
        if (one.isPresent()) {
            githubUserRepository.save(
                    one.get()
                            .setAvatarUrl(githubUser.getAvatarUrl())
                            .setUrl(githubUser.getUrl())
                            .setLogin(githubUser.getLogin())
                            .setEmail(githubUser.getEmail())
                            .setEmail(githubUser.getEmail())
                            .setName(githubUser.getName())
                            .setAuthorities(defaultRoles())
            );
        } else {
            githubUserRepository.save(githubUser
                    .setAuthorities(defaultRoles()));
        }
    }

    private Set<Role> defaultRoles() {
        return Collections.singleton(RoleEnum.USER_ROLE.toRole());
    }
}