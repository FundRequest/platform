package io.fundrequest.core.usermanagement.repository;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.usermanagement.domain.GithubUser;

public interface GithubUserRepository extends JpaRepository<GithubUser, Long> {
}