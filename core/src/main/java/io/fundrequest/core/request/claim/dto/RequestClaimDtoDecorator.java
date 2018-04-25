package io.fundrequest.core.request.claim.dto;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.domain.RequestClaim;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;

public abstract class RequestClaimDtoDecorator implements RequestClaimDtoMapper {


    @Autowired
    @Qualifier("delegate")
    private RequestClaimDtoMapper delegate;

    @Autowired
    @Lazy
    private RequestService requestService;

    @Override
    public RequestClaimDto map(RequestClaim r) {
        RequestClaimDto dto = delegate.map(r);
        if (dto != null) {
            RequestDto request = requestService.findRequest(dto.getId());
            dto.setUrl(createLink(request.getIssueInformation()));
        }
        return dto;
    }

    private String createLink(IssueInformationDto issueInformation) {
        return "https://github.com/"
               + issueInformation.getOwner()
               + "/"
               + issueInformation.getRepo()
               + "/issues/"
               + issueInformation.getNumber();
    }

}
