package io.fundrequest.core.user.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.user.domain.User;

public interface UserRepository extends JpaRepository<User, String> {
}
