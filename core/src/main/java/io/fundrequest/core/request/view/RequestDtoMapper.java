package io.fundrequest.core.request.view;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.core.infrastructure.mapping.DefaultMappingConfig;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.mapper.RequestDtoMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = DefaultMappingConfig.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {IssueInformationDtoMapper.class})
@DecoratedWith(RequestDtoMapperDecorator.class)
public interface RequestDtoMapper extends BaseMapper<Request, RequestDto> {
}