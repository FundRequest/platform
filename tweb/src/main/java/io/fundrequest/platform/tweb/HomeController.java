package io.fundrequest.platform.tweb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.common.infrastructure.mapping.Mappers;
import io.fundrequest.common.infrastructure.mav.AbstractController;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.ref.RefSignupEvent;
import io.fundrequest.platform.tweb.request.dto.RequestView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;
import static java.util.stream.Collectors.toList;

@Controller
@Slf4j
public class HomeController extends AbstractController {

    private ProfileService profileService;
    private ApplicationEventPublisher eventPublisher;
    private final RequestService requestService;
    private final ObjectMapper objectMapper;
    private final Mappers mappers;

    public HomeController(ProfileService profileService,
                          ApplicationEventPublisher eventPublisher,
                          final RequestService requestService,
                          final ObjectMapper objectMapper,
                          final Mappers mappers) {
        this.profileService = profileService;
        this.eventPublisher = eventPublisher;
        this.requestService = requestService;
        this.objectMapper = objectMapper;
        this.mappers = mappers;
    }

    @RequestMapping("/")
    public ModelAndView home(@RequestParam(value = "ref", required = false) String ref, RedirectAttributes redirectAttributes, Principal principal) {
        final List<RequestView> requests = mappers.mapList(RequestDto.class, RequestView.class, requestService.findAll())
                .stream()
                .filter(request -> request.getPhase().toLowerCase().equals("open") && (hasFunds(request.getFunds().getFndFunds()) || hasFunds(request.getFunds().getOtherFunds())))
                .collect(toList());

        if (principal != null && StringUtils.isNotBlank(ref)) {
            eventPublisher.publishEvent(RefSignupEvent.builder().principal(principal).ref(ref).build());
            return redirectView(redirectAttributes).url("/").build();
        }
        return modelAndView()
                .withObject("openRequests", getAsJson(requests))
                .withView("index")
                .build();
    }

    private boolean hasFunds(final TokenValueDto funds) {
        return funds != null && funds.getTotalAmount().compareTo(ZERO) > 0;
    }

    @GetMapping(value = {"/requestsActiveCount"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getRequestsActiveCount(Principal principal) {
        final List<RequestView> requests = mappers.mapList(RequestDto.class, RequestView.class, requestService.findAll());
        final Map<String, Long> requestsActiveCount = requests.stream()
                .filter(request -> !request.getPhase().equals("Open"))
                .collect(Collectors.groupingBy(RequestView::getOwner, Collectors.counting()));

        return getAsJson(requestsActiveCount);
    }


    @RequestMapping("/user/login")
    public ModelAndView login(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        return redirectView(redirectAttributes)
                .url(request.getHeader("referer"))
                .build();
    }

    @GetMapping(path = "/logout")
    public String logout(Principal principal, HttpServletRequest request) throws ServletException {
        if (principal != null) {
            profileService.logout(principal);
        }
        request.logout();
        return "redirect:/";
    }

    private String getAsJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Error creating json", e);
            return "";
        }
    }
}
