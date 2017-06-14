package io.fundrequest.core.request.view;

import io.fundrequest.core.request.IssueInformationDtoMapperImpl;
import io.fundrequest.core.request.RequestDtoMapper;
import io.fundrequest.core.request.RequestDtoMapperImpl;
import io.fundrequest.core.request.domain.RequestMother;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.UUID;

public final class RequestDtoMother {

    private final static RequestDtoMapper mapper;

    static {
        mapper = new RequestDtoMapperImpl();
        Field issueInformationDtoMapper = ReflectionUtils.findField(RequestDtoMapperImpl.class, "issueInformationDtoMapper");
        ReflectionUtils.makeAccessible(issueInformationDtoMapper);
        ReflectionUtils.setField(issueInformationDtoMapper, mapper, new IssueInformationDtoMapperImpl());
    }

    public static RequestDto freeCodeCampNoUserStories() {
        return mapper.map(RequestMother.freeCodeCampNoUserStories()
                .but()
                .withId(Math.abs(UUID.randomUUID().getMostSignificantBits()))
                .build());
    }

}