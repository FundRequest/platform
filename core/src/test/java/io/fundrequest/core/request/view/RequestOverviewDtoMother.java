package io.fundrequest.core.request.view;

import io.fundrequest.core.request.IssueInformationDtoMapperImpl;
import io.fundrequest.core.request.RequestDtoMapperImpl;
import io.fundrequest.core.request.RequestOverviewDtoMapper;
import io.fundrequest.core.request.RequestOverviewDtoMapperImpl;
import io.fundrequest.core.request.domain.RequestMother;
import org.mapstruct.factory.Mappers;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.UUID;

public final class RequestOverviewDtoMother {
    private static RequestOverviewDtoMapper mapper;

    static {
        mapper = new RequestOverviewDtoMapperImpl();
        Field issueInformationDtoMapper = ReflectionUtils.findField(RequestOverviewDtoMapperImpl.class, "issueInformationDtoMapper");
        ReflectionUtils.makeAccessible(issueInformationDtoMapper);
        ReflectionUtils.setField(issueInformationDtoMapper, mapper, new IssueInformationDtoMapperImpl());
    }

    public static RequestOverviewDto freeCodeCampNoUserStories() {
        return mapper.map(RequestMother.freeCodeCampNoUserStories()
                .but()
                .withId(Math.abs(UUID.randomUUID().getMostSignificantBits()))
                .build());
    }
}