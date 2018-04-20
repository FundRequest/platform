package io.fundrequest.platform.profile.linkedin;

import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.keycloak.Provider;
import io.fundrequest.platform.profile.bounty.domain.BountyType;
import io.fundrequest.platform.profile.bounty.event.CreateBountyCommand;
import io.fundrequest.platform.profile.bounty.service.BountyService;
import io.fundrequest.platform.profile.linkedin.domain.LinkedInPost;
import io.fundrequest.platform.profile.linkedin.domain.LinkedInVerification;
import io.fundrequest.platform.profile.linkedin.dto.LinkedInPostDto;
import io.fundrequest.platform.profile.linkedin.dto.LinkedInShare;
import io.fundrequest.platform.profile.linkedin.dto.LinkedInShareContent;
import io.fundrequest.platform.profile.linkedin.dto.LinkedInUpdateResult;
import io.fundrequest.platform.profile.linkedin.dto.LinkedInVerificationDto;
import io.fundrequest.platform.profile.linkedin.infrastructure.LinkedInClient;
import io.fundrequest.platform.profile.linkedin.infrastructure.LinkedInPostRepository;
import io.fundrequest.platform.profile.linkedin.infrastructure.LinkedInUser;
import io.fundrequest.platform.profile.linkedin.infrastructure.LinkedInVerificationRepository;
import io.fundrequest.platform.profile.profile.dto.UserLinkedProviderEvent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
class LinkedInServiceImpl implements LinkedInService {

    private KeycloakRepository keycloakRepository;
    private LinkedInClient client;
    private LinkedInVerificationRepository repository;
    private BountyService bountyService;
    private LinkedInPostRepository postRepository;

    public LinkedInServiceImpl(KeycloakRepository keycloakRepository,
                               LinkedInClient client,
                               LinkedInVerificationRepository repository,
                               BountyService bountyService,
                               LinkedInPostRepository postRepository) {
        this.keycloakRepository = keycloakRepository;
        this.client = client;
        this.repository = repository;
        this.bountyService = bountyService;
        this.postRepository = postRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public LinkedInVerificationDto getVerification(Principal principal) {
        return repository.findByUserId(principal.getName()).map(
                l -> LinkedInVerificationDto.builder()
                                            .verified(true)
                                            .postUrl(l.getPostUrl())
                                            .build()
                                                               ).orElseGet(() -> null);
    }

    @EventListener
    @Transactional
    public void onProviderLinked(UserLinkedProviderEvent event) {
        if (event.getProvider() == Provider.LINKEDIN && event.getPrincipal() != null) {
            try {
                updateUserHeadline(event);
            } catch (Exception e) {
                log.error("An error occurred when getting users headline from LinkedIn");
            }
        }
    }

    private void updateUserHeadline(UserLinkedProviderEvent event) {
        LinkedInUser linkedInUser = client.getUserInfo(keycloakRepository.getAccessToken((KeycloakAuthenticationToken) event.getPrincipal(), Provider.LINKEDIN));
        if (linkedInUser != null && StringUtils.isNotBlank(linkedInUser.getHeadline())) {
            keycloakRepository.updateHeadline(event.getPrincipal().getName(), linkedInUser.getHeadline());
        }
    }

    @Override
    @Transactional
    public void postLinkedInShare(Principal principal, @NonNull Long postId) {
        if (!repository.findByUserId(principal.getName()).isPresent() && keycloakRepository.isVerifiedDeveloper(principal.getName())) {
            KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) principal;
            LinkedInShare linkedInShare = getLinkedInShare(postId);
            LinkedInUpdateResult linkedInUpdateResult = client.postNetworkUpdate(
                    keycloakRepository.getAccessToken(token, Provider.LINKEDIN),
                    linkedInShare
                                                                                );

            LinkedInVerification verification = LinkedInVerification.builder().userId(principal.getName()).postUrl(linkedInUpdateResult.getUpdateUrl()).build();
            repository.save(verification);
            bountyService.createBounty(CreateBountyCommand.builder()
                                                          .userId(principal.getName())
                                                          .type(BountyType.POST_LINKEDIN_UPDATE)
                                                          .build());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public LinkedInPostDto getRandomPost() {
        List<LinkedInPost> posts = postRepository.findAll();
        Collections.shuffle(posts);
        LinkedInPost post = posts.get(0);
        return LinkedInPostDto.builder()
                              .id(post.getId())
                              .comment(post.getComment())
                              .title(post.getTitle())
                              .description(post.getDescription())
                              .submittedUrl(post.getSubmittedUrl())
                              .submittedImageUrl(post.getSubmittedImageUrl())
                              .build();
    }

    private LinkedInShare getLinkedInShare(Long postId) {
        LinkedInPost post = postRepository.findOne(postId);
        return LinkedInShare.builder()
                            .comment(post.getComment())
                            .content(
                                    LinkedInShareContent.builder()
                                                        .title(post.getTitle())
                                                        .description(post.getDescription())
                                                        .submittedUrl(post.getSubmittedUrl())
                                                        .submittedImageUrl(post.getSubmittedImageUrl())
                                                        .build()
                                    )
                            .build();
    }
}
