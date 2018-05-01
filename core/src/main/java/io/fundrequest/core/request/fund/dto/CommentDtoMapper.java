package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.core.infrastructure.mapping.DefaultMappingConfig;
import io.fundrequest.platform.github.parser.GithubIssueCommentsResult;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = DefaultMappingConfig.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(CommentDtoMapperDecorator.class)
public interface CommentDtoMapper extends BaseMapper<GithubIssueCommentsResult, CommentDto> {

}
