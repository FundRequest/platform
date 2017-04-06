package io.fundrequest.core.request.view;

import io.fundrequest.core.infrastructure.repository.AbstractController;
import io.fundrequest.core.request.CreateRequestCommand;
import io.fundrequest.core.request.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    @PostMapping("/requests")
    public ResponseEntity<?> createRequest(@RequestBody @Valid CreateRequestCommand createRequestCommand) {
        RequestOverviewDto result = requestService.createRequest(createRequestCommand);
        return ResponseEntity
                .created(getLocationFromCurrentPath("/{id}", result.getId()))
                .build();
    }
}
