package io.fundrequest.core.request;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.core.infrastructure.mapping.DefaultMappingConfig;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.view.RequestDto;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMappingConfig.class,
        uses = {IssueInformationDtoMapper.class})
public interface RequestDtoMapper extends BaseMapper<Request, RequestDto> {
}
