package io.fundreqest.platform.tweb.infrastructure.thymeleaf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.context.IProcessingContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ProfilesDialectTest {

    private ProfilesDialect profilesDialect;

    private ProfilesExpressionObject profilesExpressionObject;

    @BeforeEach
    void setUp() {
        profilesExpressionObject = mock(ProfilesExpressionObject.class);
        profilesDialect = new ProfilesDialect(profilesExpressionObject);
    }

    @Test
    void getPrefix() {
        assertThat(profilesDialect.getPrefix()).isEqualTo("profiles");
    }

    @Test
    void getAdditionalExpressionObjects() {
        assertThat(profilesDialect.getAdditionalExpressionObjects(mock(IProcessingContext.class)).get("profiles")).isEqualTo(profilesExpressionObject);
    }
}