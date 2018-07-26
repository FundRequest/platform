package io.fundrequest.core.request.claim.dto;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.domain.RequestClaim;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

public abstract class RequestClaimDtoDecorator implements RequestClaimDtoMapper {


    @Autowired
    @Qualifier("delegate")
    private RequestClaimDtoMapper delegate;

    @Autowired
    @Lazy
    private RequestService requestService;

    @Value("${io.fundrequest.platform.base-path}")
    private String platformBasePath;

    @Override
    public RequestClaimDto map(RequestClaim r) {
        RequestClaimDto dto = delegate.map(r);
        if (dto != null) {
            RequestDto request = requestService.findRequest(r.getRequestId());
            dto.setUrl(createLink(request.getIssueInformation()));
            dto.setTitle(request.getIssueInformation().getTitle());
            dto.setFundRequestUrl(createFundRequestLink(request.getId()));
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

    private String createFundRequestLink(Long id) {
        return platformBasePath + "/requests/" + id;
    }

}
