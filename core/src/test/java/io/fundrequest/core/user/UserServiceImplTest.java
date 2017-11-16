package io.fundrequest.core.user;

import io.fundrequest.core.user.domain.User;
import io.fundrequest.core.user.domain.UserMother;
import io.fundrequest.core.user.dto.UserDto;
import io.fundrequest.core.user.dto.UserDtoMapper;
import io.fundrequest.core.user.infrastructure.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private UserServiceImpl userService;
    private UserRepository userRepository;
    private UserDtoMapper userDtoMapper;

    @Before
    public void setUp() throws Exception {
        userRepository = mock(UserRepository.class);
        userDtoMapper = mock(UserDtoMapper.class);
        userService = new UserServiceImpl(userRepository, userDtoMapper);
    }

    @Test
    public void getUser() throws Exception {
        User user = UserMother.davy();
        UserDto userDto = UserDtoMother.davy();
        when(userRepository.findOne(user.getEmail())).thenReturn(Optional.of(user));
        when(userDtoMapper.map(user)).thenReturn(userDto);

        UserDto result = userService.getUser(user.getEmail());

        assertThat(result).isEqualToComparingFieldByField(userDto);
    }


}