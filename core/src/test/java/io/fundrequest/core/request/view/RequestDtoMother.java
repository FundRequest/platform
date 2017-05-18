package io.fundrequest.core.request.view;

import io.fundrequest.core.request.RequestDtoMapper;
import io.fundrequest.core.request.domain.RequestMother;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

public final class RequestDtoMother {

    private static RequestDtoMapper mapper = Mappers.getMapper(RequestDtoMapper.class);

    public static RequestDto freeCodeCampNoUserStories() {
        return mapper.map(RequestMother.freeCodeCampNoUserStories()
                .but()
                .withId(Math.abs(UUID.randomUUID().getMostSignificantBits()))
                .build());
    }

}