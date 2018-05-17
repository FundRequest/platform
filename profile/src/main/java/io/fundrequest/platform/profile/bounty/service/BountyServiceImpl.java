package io.fundrequest.platform.profile.bounty.service;

import io.fundrequest.db.infrastructure.AbstractEntity;
import io.fundrequest.platform.profile.bounty.domain.Bounty;
import io.fundrequest.platform.profile.bounty.domain.BountyType;
import io.fundrequest.platform.profile.bounty.dto.BountyDTO;
import io.fundrequest.platform.profile.bounty.dto.PaidBountyDto;
import io.fundrequest.platform.profile.bounty.event.CreateBountyCommand;
import io.fundrequest.platform.profile.bounty.infrastructure.BountyRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
class BountyServiceImpl implements BountyService {

    private BountyRepository bountyRepository;

    public BountyServiceImpl(BountyRepository bountyRepository) {
        this.bountyRepository = bountyRepository;
    }

    @Transactional
    @Override
    public void createBounty(CreateBountyCommand createBountyCommand) {
        final Bounty bounty = Bounty.builder()
                                    .userId(createBountyCommand.getUserId())
                                    .type(createBountyCommand.getType())
                                    .build();

        bountyRepository.save(bounty);
    }

    @Override
    public BountyDTO getBounties(final Principal principal) {
        final List<Bounty> byUser = bountyRepository.findByUserId(principal.getName());
        Map<BountyType, List<Bounty>> byType = byUser.stream()
                                                     .collect(Collectors.groupingBy(Bounty::getType));

        int referralRewards = byType.getOrDefault(BountyType.REFERRAL, new ArrayList<>()).size() * BountyType.REFERRAL.getReward();
        int twitterRewards = byType.getOrDefault(BountyType.TWITTER_TWEET_FOLLOW, new ArrayList<>()).size() * BountyType.TWITTER_TWEET_FOLLOW.getReward();
        int linkedInRewards = byType.getOrDefault(BountyType.POST_LINKEDIN_UPDATE, new ArrayList<>()).size() * BountyType.POST_LINKEDIN_UPDATE.getReward();
        int telegramRewards = byType.getOrDefault(BountyType.LINK_TELEGRAM, new ArrayList<>()).size() * BountyType.LINK_TELEGRAM.getReward();
        int surveyRewards = byType.getOrDefault(BountyType.SURVEY, new ArrayList<>()).size() * BountyType.SURVEY.getReward();
        int otherRewards =
                byType.getOrDefault(BountyType.LINK_GITHUB, new ArrayList<>()).size() * BountyType.LINK_GITHUB.getReward()
                + byType.getOrDefault(BountyType.LINK_STACK_OVERFLOW, new ArrayList<>()).size() * BountyType.LINK_STACK_OVERFLOW.getReward()
                + linkedInRewards
                + twitterRewards
                + telegramRewards
                + surveyRewards;

        return BountyDTO.builder()
                        .referralRewards(referralRewards)
                        .otherRewards(otherRewards)
                        .totalRewards(referralRewards + otherRewards)
                        .twitterRewards(twitterRewards)
                        .linkedInRewards(linkedInRewards)
                        .telegramRewards(telegramRewards)
                        .surveyRewards(surveyRewards)
                        .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaidBountyDto> getPaidBounties(Principal principal) {
        return bountyRepository.findByUserId(principal.getName())
                               .stream()
                               .filter(f -> StringUtils.isNotBlank(f.getTransactionHash()))
                               .sorted(Comparator.comparing(AbstractEntity::getCreationDate).reversed())
                               .map(b -> PaidBountyDto.builder()
                                                      .type(b.getType())
                                                      .amount(b.getType().getReward())
                                                      .transactionHash(b.getTransactionHash())
                                                      .build()
                                   )
                               .collect(Collectors.toList());
    }


}
