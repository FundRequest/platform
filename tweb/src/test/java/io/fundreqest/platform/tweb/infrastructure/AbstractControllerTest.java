package io.fundreqest.platform.tweb.infrastructure;

import io.fundreqest.platform.tweb.infrastructure.mav.dto.AlertDto;
import lombok.Data;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

public abstract class AbstractControllerTest<T> {

    protected MockMvc mockMvc;

    protected abstract T setupController();

    @Before
    public void setUpMockMvc() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(setupController()).build();
    }

    protected ResultMatcher redirectAlert(String type, String msg) {
        return flash().attribute("alerts", new AlertMatcher(type, msg));
    }

    @Data
    private class AlertMatcher extends BaseMatcher<List<AlertDto>> {
        private final String type;
        private final String msg;


        @Override
        public boolean matches(Object o) {
            List<AlertDto> alerts = (List<AlertDto>) o;
            for (AlertDto alert : alerts) {
                if (alert.getType().equals(type) && alert.getMsg().equals(msg)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue("Alert with type '" + this.type + "' and msg '" + this.msg + "'");
        }
    }
}
