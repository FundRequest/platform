package io.fundrequest.core.user.dto;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.core.infrastructure.mapping.DefaultMappingConfig;
import io.fundrequest.core.user.domain.User;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMappingConfig.class)
public interface UserDtoMapper extends BaseMapper<User, UserDto> {
}
