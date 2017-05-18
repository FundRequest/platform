package io.fundrequest.core.request.view;

import io.fundrequest.core.infrastructure.repository.AbstractController;
import io.fundrequest.core.request.CreateRequestCommand;
import io.fundrequest.core.request.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class RequestController extends AbstractController {

    private RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/requests")
    public List<RequestOverviewDto> findAll() {
        return requestService.findAll();
    }

    @GetMapping("/user/requests")
    public List<RequestOverviewDto> findRequestsForUser(Principal principal) {
        return requestService.findRequestsForUser(principal);
    }

    @GetMapping("/requests/{id}")
    public RequestDto findOne(@PathVariable("id") Long id) {
        return requestService.findRequest(id);
    }

    @PostMapping("/requests")
    public ResponseEntity<?> createRequest(
            Principal principal,
            @RequestBody @Valid CreateRequestCommand createRequestCommand) {

        RequestOverviewDto result = requestService.createRequest(
                principal.getName(),
                createRequestCommand
        );
        return ResponseEntity
                .created(getLocationFromCurrentPath("/{id}", result.getId()))
                .build();
    }
}
