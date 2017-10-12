package io.fundrequest.core.user;

import io.fundrequest.core.user.domain.User;
import io.fundrequest.core.user.infrastructure.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable("users")
    @Transactional(readOnly = true)
    public UserDto getUser(String userId) {
        return null;
    }

    @Override
    @Cacheable("userlogins")
    @Transactional
    public UserAuthentication login(UserLoginCommand loginCommand) {
        User user = userRepository.findOne(loginCommand.getUserId())
                .map(u -> updateUser(loginCommand, u))
                .orElseGet(() -> createNewUser(loginCommand));

        userRepository.save(user);
        return new UserAuthentication(user.getUserId());
    }

    private User updateUser(UserLoginCommand loginCommand, User user) {
        user.setEmail(loginCommand.getEmail());
        user.setPhoneNumber(loginCommand.getPhoneNumber());
        return user;
    }

    private User createNewUser(UserLoginCommand loginCommand) {
        return new User(loginCommand.getUserId(), loginCommand.getPhoneNumber(), loginCommand.getEmail());
    }

}
