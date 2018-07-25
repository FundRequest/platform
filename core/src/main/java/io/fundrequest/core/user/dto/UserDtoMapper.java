package io.fundrequest.core.user.dto;

import io.fundrequest.common.infrastructure.mapping.BaseMapper;
import io.fundrequest.common.infrastructure.mapping.DefaultMappingConfig;
import io.fundrequest.core.user.domain.User;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = DefaultMappingConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(UserDtoMapperDecorator.class)
public interface UserDtoMapper extends BaseMapper<User, UserDto> {
}

