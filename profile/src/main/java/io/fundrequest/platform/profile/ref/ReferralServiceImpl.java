package io.fundrequest.platform.profile.ref;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.profile.bounty.event.CreateBountyCommand;
import io.fundrequest.platform.profile.bounty.service.BountyService;
import io.fundrequest.platform.profile.profile.dto.UserLinkedProviderEvent;
import io.fundrequest.platform.profile.ref.domain.Referral;
import io.fundrequest.platform.profile.ref.domain.ReferralStatus;
import io.fundrequest.platform.profile.ref.dto.ReferralDto;
import io.fundrequest.platform.profile.ref.dto.ReferralOverviewDto;
import io.fundrequest.platform.profile.ref.infrastructure.ReferralRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.fundrequest.platform.profile.bounty.domain.BountyType.REFERRAL;

@Service
@Slf4j
class ReferralServiceImpl implements ReferralService {

    private final ObjectMapper objectMapper;
    private ReferralRepository repository;
    private KeycloakRepository keycloakRepository;
    private BountyService bountyService;

    public ReferralServiceImpl(ReferralRepository repository,
                               KeycloakRepository keycloakRepository,
                               BountyService bountyService) {
        this.repository = repository;
        this.keycloakRepository = keycloakRepository;
        this.bountyService = bountyService;
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @EventListener
    @Transactional
    public void onProviderLinked(UserLinkedProviderEvent event) {
        if (event.getPrincipal() != null) {
            repository.findByReferee(event.getPrincipal().getName())
                      .ifPresent(this::sendBountyIfPossible);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ReferralOverviewDto getOverview(Principal principal) {
        Map<ReferralStatus, List<Referral>> byStatus = repository.findByReferrer(principal.getName()).stream().collect(Collectors.groupingBy(Referral::getStatus));
        return ReferralOverviewDto.builder()
                                  .totalVerified(byStatus.get(ReferralStatus.VERIFIED).size())
                                  .totalUnverified(byStatus.get(ReferralStatus.PENDING).size())
                                  .build();
    }

    @Override
    @Cacheable("ref_links")
    public String generateRefLink(String userId, String source) {
        String longUrl = "https://fundrequest.io?ref=" + userId + "&utm_source=referral&utm_medium=" + source + "&utm_campaign=early_signup";
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("https://tinyurl.com/api-create.php?url=" + longUrl);
        try {
            HttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity());
            } else {
                return longUrl;
            }
        } catch (IOException e) {
            return longUrl;
        }

    }

    @Transactional(readOnly = true)
    @Override
    public List<ReferralDto> getReferrals(Principal principal) {
        return repository.findByReferrer(principal.getName(), new Sort(Sort.Direction.DESC, "creationDate"))
                         .stream()
                         .parallel()
                         .map(this::createReferralDto)
                         .collect(Collectors.toList());
    }

    private ReferralDto createReferralDto(Referral r) {
        UserRepresentation ur = keycloakRepository.getUser(r.getReferee());
        return ReferralDto.builder().status(r.getStatus())
                          .name(ur.getFirstName() + " " + ur.getLastName())
                          .email(ur.getEmail())
                          .picture(keycloakRepository.getAttribute(ur, "picture"))
                          .createdAt(r.getCreationDate()).build();
    }

    @Override
    @Transactional
    public void createNewRef(CreateRefCommand command) {
        String referrer = command.getRef();
        String referee = command.getPrincipal().getName();
        validReferral(referrer, referee);
        if (!repository.existsByReferee(referee)) {
            Referral referral = Referral.builder()
                                        .referrer(referrer)
                                        .referee(referee)
                                        .status(ReferralStatus.PENDING)
                                        .build();
            repository.save(referral);
            sendBountyIfPossible(referral);
        } else {
            throw new RuntimeException("This ref already exists!");
        }
    }


    private void sendBountyIfPossible(Referral referral) {
        if (referral != null && isVerifiedPrincipal(referral.getReferee()) && referral.getStatus() == ReferralStatus.PENDING) {
            referral.setStatus(ReferralStatus.VERIFIED);
            bountyService.createBounty(CreateBountyCommand.builder()
                                                          .userId(referral.getReferrer())
                                                          .type(REFERRAL)
                                                          .build());
            repository.save(referral);
        }
    }

    private boolean isVerifiedPrincipal(String userId) {
        return keycloakRepository.isVerifiedDeveloper(userId);
    }

    private void validReferral(String referrer, String referee) {
        if (isInvalidReferee(referrer, referee)) {
            throw new RuntimeException("This is not a valid referee");
        }
        if (!keycloakRepository.userExists(referrer)) {
            throw new RuntimeException("This is not a valid referrer");
        }
    }

    private boolean isInvalidReferee(String referrer, String referee) {
        return referrer.equalsIgnoreCase(referee) || !keycloakRepository.userExists(referee);
    }
}
