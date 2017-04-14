package io.fundrequest.core.request;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.core.infrastructure.mapping.DefaultMappingConfig;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.view.RequestOverviewDto;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMappingConfig.class,
        uses = {IssueInformationDtoMapper.class})
public interface RequestMapper extends BaseMapper<Request, RequestOverviewDto> {
}
