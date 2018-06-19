package io.fundrequest.platform.tweb.request;

import io.fundrequest.core.infrastructure.exception.ResourceNotFoundException;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.dto.ClaimableResultDto;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.platform.tweb.request.dto.ClaimView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/requests")
public class RequestRestController {


    private final RequestService requestService;

    public RequestRestController(final RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping(value = "/github/{owner}/{repo}/{number}/claimable")
    public ClaimView claimDetails(@PathVariable("owner") final String repoOwner, @PathVariable("repo") final String repo, @PathVariable("number") final String issueNumber) {
        final RequestDto request = requestService.findRequest(Platform.GITHUB, String.format("%s|FR|%s|FR|%s", repoOwner, repo, issueNumber));
        final ClaimableResultDto claimableResult = requestService.getClaimableResult(request.getId());
        return new ClaimView(claimableResult.isClaimable(), claimableResult.getClaimableByPlatformUserName());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void onResourceNotFound() {
        // Intentionally blank
    }
}
