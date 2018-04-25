package io.fundrequest.core.request.view;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.core.infrastructure.mapping.DefaultMappingConfig;
import io.fundrequest.core.request.domain.IssueInformation;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMappingConfig.class)
public interface IssueInformationDtoMapper extends BaseMapper<IssueInformation, IssueInformationDto> {
}
