package io.fundreqest.platform.tweb.request.dto;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.core.request.view.RequestDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(RequestDetailsViewMapperDecorator.class)
public interface RequestDetailsViewMapper extends BaseMapper<RequestDto, RequestDetailsView> {
}
