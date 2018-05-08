package io.fundreqest.platform.tweb.infrastructure.mav;

import io.fundrequest.core.request.domain.RequestFase;
import io.fundrequest.core.request.domain.RequestStatus;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnumToCapitalizedStringMapperTest {

    private EnumToCapitalizedStringMapper mapper;

    @Before
    public void setUp() {
        mapper = new EnumToCapitalizedStringMapper();
    }

    @Test
    public void map() {
        assertThat(mapper.map(RequestStatus.FUNDED)).isEqualTo("Funded");
        assertThat(mapper.map(RequestStatus.CLAIM_REQUESTED)).isEqualTo("Claim Requested");
        assertThat(mapper.map(RequestFase.RESOLVED)).isEqualTo("Resolved");
    }

    @Test
    public void map_null() {
        assertThat(mapper.map(null)).isNull();
    }
}
