package io.fundrequest.core.request;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.core.infrastructure.mapping.DefaultMappingConfig;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.mapper.RequestOverviewDtoMapperDecorator;
import io.fundrequest.core.request.view.RequestOverviewDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = DefaultMappingConfig.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {IssueInformationDtoMapper.class})
@DecoratedWith(RequestOverviewDtoMapperDecorator.class)
public interface RequestOverviewDtoMapper extends BaseMapper<Request, RequestOverviewDto> {
}
