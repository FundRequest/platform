package io.fundrequest.common.infrastructure.mav;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EnumToCapitalizedStringMapperTest {

    private EnumToCapitalizedStringMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new EnumToCapitalizedStringMapper();
    }

    @Test
    void map() {
        assertThat(mapper.map(TestEnum.BLABLABLA)).isEqualTo("Blablabla");
        assertThat(mapper.map(TestEnum.BLIBLIBLI)).isEqualTo("Bliblibli");
        assertThat(mapper.map(SomeOtherTestEnum.BLOBLOBLO)).isEqualTo("Blobloblo");
    }

    @Test
    void map_null() {
        assertThat(mapper.map(null)).isNull();
    }

    private enum TestEnum {
        BLABLABLA,
        BLIBLIBLI
    }

    private enum SomeOtherTestEnum {
        BLOBLOBLO
    }
}