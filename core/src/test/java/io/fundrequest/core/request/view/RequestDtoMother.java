package io.fundrequest.core.request.view;

import io.fundrequest.core.request.domain.RequestMother;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.UUID;

public final class RequestDtoMother {

    private final static RequestDtoMapper mapper;

    static {
        mapper = new RequestDtoMapperImpl_();
        Field issueInformationDtoMapper = ReflectionUtils.findField(RequestDtoMapperImpl_.class, "issueInformationDtoMapper");
        ReflectionUtils.makeAccessible(issueInformationDtoMapper);
        ReflectionUtils.setField(issueInformationDtoMapper, mapper, new IssueInformationDtoMapperImpl());
    }

    public static RequestDto freeCodeCampNoUserStories() {
        return mapper.map(RequestMother.freeCodeCampNoUserStories()
                .but()
                .withId(Math.abs(UUID.randomUUID().getMostSignificantBits()))
                .build());
    }

    public static RequestDto fundRequestArea51() {
        return mapper.map(RequestMother.fundRequestArea51()
                                       .but()
                                       .withId(Math.abs(UUID.randomUUID().getMostSignificantBits()))
                                       .build());
    }

}
