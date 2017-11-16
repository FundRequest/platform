package io.fundrequest.restapi.request;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.restapi.infrastructure.AbstractRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class RequestController extends AbstractRestController {

    private RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping(PUBLIC_PATH + "/requests")
    public List<RequestDto> findAll() {
        return requestService.findAll();
    }

    @GetMapping(PUBLIC_PATH + "/user/requests")
    public List<RequestDto> findRequestsForUser(Principal principal) {
        return requestService.findRequestsForUser(principal);
    }

    @GetMapping(PUBLIC_PATH + "/requests/{id}")
    public RequestDto findOne(@PathVariable("id") Long id) {
        return requestService.findRequest(id);
    }

    @GetMapping({PUBLIC_PATH + "/requests/{id}/watchers", "/requests/{id}/watchlink"})
    public RequestDto findWatchers(@PathVariable("id") Long id) {
        return requestService.findRequest(id);
    }

    @PutMapping(PRIVATE_PATH + "/requests/{id}/watchers")
    public ResponseEntity<?> addWatcher(@PathVariable("id") Long requestId, Principal principal) {
        requestService.addWatcherToRequest(principal, requestId);
        return ResponseEntity
                .created(getLocationFromCurrentPath(""))
                .build();
    }

    @DeleteMapping(PRIVATE_PATH + "/requests/{id}/watchers")
    public void removeWatcher(@PathVariable("id") Long requestId, Principal principal) {
        requestService.removeWatcherFromRequest(principal, requestId);
    }

    @PostMapping(PRIVATE_PATH + "/requests")
    public ResponseEntity<?> createRequest(
            Principal principal,
            @RequestBody @Valid CreateRequestCommand createRequestCommand) {

        RequestDto result = requestService.createRequest(
                principal,
                createRequestCommand
        );
        return ResponseEntity
                .created(getLocationFromCurrentPath("/{id}", result.getId()))
                .build();
    }
}
