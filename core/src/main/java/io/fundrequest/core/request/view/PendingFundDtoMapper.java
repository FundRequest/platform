package io.fundrequest.core.request.view;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.core.infrastructure.mapping.DefaultMappingConfig;
import io.fundrequest.core.request.fund.domain.PendingFund;
import io.fundrequest.core.request.fund.dto.PendingFundDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = DefaultMappingConfig.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {IssueInformationDtoMapper.class})
@DecoratedWith(PendingFundDtoMapperDecorator.class)
public interface PendingFundDtoMapper extends BaseMapper<PendingFund, PendingFundDto> {
}