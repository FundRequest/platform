package io.fundrequest.platform.profile.profile.dto;

import io.fundrequest.platform.profile.arkane.Wallet;
import io.fundrequest.platform.profile.arkane.WalletMother;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class UserProfileTest {

    @Test
    void userOwnsAddress() {
        Wallet wallet = WalletMother.aWallet();
        UserProfile userProfile = UserProfile.builder()
                                             .etherAddresses(Collections.singletonList(wallet.getAddress()))
                                             .linkedin(null)
                                             .github(null)
                                             .arkane(null)
                                             .google(null)
                                             .wallets(Collections.singletonList(wallet)).build();

        assertThat(userProfile.userOwnsAddress(wallet.getAddress())).isTrue();
    }

    @Test
    void getEtherAddresses() {
        Wallet wallet = WalletMother.aWallet();
        UserProfile userProfile = UserProfile.builder()
                                             .etherAddresses(Collections.singletonList(wallet.getAddress()))
                                             .wallets(Collections.singletonList(wallet)).build();

        assertThat(userProfile.getEtherAddresses()).containsExactly(wallet.getAddress());
    }
}