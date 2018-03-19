package io.fundrequest.core.request.claim.dto;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.core.infrastructure.mapping.DefaultMappingConfig;
import io.fundrequest.core.request.claim.domain.RequestClaim;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = DefaultMappingConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(RequestClaimDtoDecorator.class)
public interface RequestClaimDtoMapper extends BaseMapper<RequestClaim, RequestClaimDto> {
}