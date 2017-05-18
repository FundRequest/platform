package io.fundrequest.core.request.view;

import io.fundrequest.core.request.RequestOverviewDtoMapper;
import io.fundrequest.core.request.domain.RequestMother;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

public final class RequestOverviewDtoMother {
    private static RequestOverviewDtoMapper mapper = Mappers.getMapper(RequestOverviewDtoMapper.class);

    public static RequestOverviewDto freeCodeCampNoUserStories() {
        return mapper.map(RequestMother.freeCodeCampNoUserStories()
                .but()
                .withId(Math.abs(UUID.randomUUID().getMostSignificantBits()))
                .build());
    }
}