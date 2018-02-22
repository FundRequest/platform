package io.fundrequest.core.request.claim.dto;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.core.infrastructure.mapping.DefaultMappingConfig;
import io.fundrequest.core.request.claim.domain.RequestClaim;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = DefaultMappingConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequestClaimDtoMapper extends BaseMapper<RequestClaim, RequestClaimDto> {
}