package io.fundreqest.platform.tweb.request;

import io.fundreqest.platform.tweb.request.dto.ClaimView;
import io.fundreqest.platform.tweb.request.dto.RequestView;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.dto.ClaimableResultDto;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.view.RequestDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/requests")
public class RequestRestController {

    private final RequestService requestService;
    private final Mappers mappers;

    public RequestRestController(final RequestService requestService, final Mappers mappers) {
        this.requestService = requestService;
        this.mappers = mappers;
    }

    @GetMapping(value = "/github/{owner}/{repo}/{number}/claimable")
    public ClaimView claimDetails(@PathVariable("owner") final String repoOwner, @PathVariable("repo") final String repo, @PathVariable("number") final String issueNumber) {
        final RequestDto request = requestService.findRequest(Platform.GITHUB, String.format("%s|FR|%s|FR|%s", repoOwner, repo, issueNumber));
        final ClaimableResultDto claimableResult = requestService.getClaimableResult(request.getId());
        return new ClaimView(claimableResult.isClaimable(), claimableResult.getClaimableByPlatformUserName());
    }

    @GetMapping(value = "/github/{owner}/{repo}/{number}")
    public RequestView requestDetails(@PathVariable("owner") final String repoOwner, @PathVariable("repo") final String repo, @PathVariable("number") final String issueNumber) {
        final RequestDto request = requestService.findRequest(Platform.GITHUB, String.format("%s|FR|%s|FR|%s", repoOwner, repo, issueNumber));
        return mappers.map(RequestDto.class, RequestView.class, request);
    }

}
