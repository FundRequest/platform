package io.fundrequest.platform.profile.profile.dto;

import io.fundrequest.platform.profile.arkane.Wallet;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
@Builder
public class UserProfile {
    private String id;
    private String name;
    private String email;
    private List<String> etherAddresses;
    private List<Wallet> wallets;
    private String telegramName;
    private String picture;
    private String headline;
    private Long createdAt;
    private boolean verifiedDeveloper;
    private UserProfileProvider github;
    private UserProfileProvider linkedin;
    private UserProfileProvider twitter;
    private UserProfileProvider stackoverflow;
    private UserProfileProvider arkane;
    private UserProfileProvider google;
    private String emailSignedVerification;

    public boolean userOwnsAddress(String address) {
        return getEtherAddresses().stream().anyMatch(x -> x.equalsIgnoreCase(address));
    }

    public boolean hasEtherAddress() {
        return !CollectionUtils.isEmpty(etherAddresses);
    }


}
