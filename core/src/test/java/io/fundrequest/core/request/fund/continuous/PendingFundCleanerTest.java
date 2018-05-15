package io.fundrequest.core.request.fund.continuous;


import com.google.common.collect.ImmutableList;
import io.fundrequest.core.request.fund.PendingFundService;
import io.fundrequest.core.request.fund.domain.PendingFund;
import io.fundrequest.core.request.fund.domain.PendingFundMother;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.transactions.TransactionStatus;
import io.fundrequest.core.util.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static com.google.common.collect.ImmutableList.of;
import static io.fundrequest.core.web3j.AddressUtils.prettify;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PendingFundCleanerTest {

    private PendingFundCleaner pendingFundCleaner;
    private PendingFundService pendingFundService;
    private AzraelClient azraelClient;

    @Before
    public void setUp() {
        azraelClient = mock(AzraelClient.class);
        pendingFundService = mock(PendingFundService.class);

        pendingFundCleaner = new PendingFundCleaner(pendingFundService, azraelClient);
    }

    @Test
    public void shouldRemoveFailedPendingFunds() {
        final PendingFund pendingFund = PendingFundMother.aPendingFund();
        when(pendingFundService.findAll())
                .thenReturn(of(pendingFund));
        when(azraelClient.getTransactionStatus(prettify(pendingFund.getTransactionHash()))).thenReturn(TransactionStatus.FAILED);


        pendingFundCleaner.cleanupPendingFunds();

        verify(pendingFundService).removePendingFund(pendingFund);
    }

    @Test
    public void shouldRemoveAgedPendingFunds() {
        final PendingFund pendingFund = PendingFundMother.aPendingFund();
        when(pendingFundService.findAll())
                .thenReturn(of(pendingFund));
        when(azraelClient.getTransactionStatus(prettify(pendingFund.getTransactionHash()))).thenReturn(TransactionStatus.SUCCEEDED);

        ReflectionUtils.set(pendingFund, "creationDate", LocalDateTime.now().minusDays(2));

        pendingFundCleaner.cleanupPendingFunds();

        verify(pendingFundService).removePendingFund(pendingFund);
    }

    @Test
    public void shouldntRemoveWhenThrownExceptionFromAzrael() {
        final PendingFund pendingFund = PendingFundMother.aPendingFund();
        when(pendingFundService.findAll())
                .thenReturn(of(pendingFund));
        when(azraelClient.getTransactionStatus(prettify(pendingFund.getTransactionHash()))).thenThrow(new IllegalArgumentException("problemos"));

        pendingFundCleaner.cleanupPendingFunds();

        verify(pendingFundService, times(0)).removePendingFund(any(PendingFund.class));
    }
}