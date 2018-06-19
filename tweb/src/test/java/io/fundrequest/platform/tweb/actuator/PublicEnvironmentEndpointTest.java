package io.fundrequest.platform.tweb.actuator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.endpoint.EnvironmentEndpoint;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicEnvironmentEndpointTest {

    private PublicEnvironmentEndpoint endpoint;

    private EnvironmentEndpoint environmentEndpoint;

    @BeforeEach
    void setUp() {
        environmentEndpoint = mock(EnvironmentEndpoint.class);
        endpoint = new PublicEnvironmentEndpoint(environmentEndpoint, true, false);
    }

    @Test
    void invoke() {
        final Map<String, Object> envProperties = MapBuilder.<String, Object>builder()
                .put("notAGroup", "hgfjk")
                .put("notAGroup2", "hxfgcj")
                .put("notAGroup2.public", "false")
                .put("notAGroup3", "aefsd")
                .put("notAGroup3.public", "true")
                .put("group1", MapBuilder.<String, Object>builder()
                        .put("sfdfsd.dfsfd.sdfsd", "aefg")
                        .put("aszgf.dsads", "sdf")
                        .put("aszgf.dsads.public", "true")
                        .build())
                .put("group1.public", "false")
                .put("group2", MapBuilder.<String, Object>builder()
                        .put("vxvdf.dscvds.ad", "wqwe")
                        .put("wdoij.oijda.acs", "efsef")
                        .put("wdoij.oijda.acs.public", "true")
                        .build())
                .put("group2.public", "true")
                .put("group3", MapBuilder.<String, Object>builder()
                        .put("ghfcv.jhgk", "khgj")
                        .put("cgv.fgchv.dwf", "kjhg")
                        .put("cgv.fgchv.dwf.public", "true")
                        .build())
                .build();

        when(environmentEndpoint.invoke()).thenReturn(envProperties);

        final Map<String, Object> result = endpoint.invoke();

        assertThat(result).isEqualTo(MapBuilder.<String, Object>builder()
                                             .put("notAGroup3", "aefsd")
                                             .put("aszgf.dsads", "sdf")
                                             .put("vxvdf.dscvds.ad", "wqwe")
                                             .put("wdoij.oijda.acs", "efsef")
                                             .put("cgv.fgchv.dwf", "kjhg")
                                             .build());
    }

    private static class MapBuilder<T, E> {

        private Map<T, E> map = new HashMap<>();

        public static <T, E> MapBuilder<T, E> builder() {
            return new MapBuilder<>();
        }

        public MapBuilder<T, E> put(final T key, final E value) {
            map.put(key, value);
            return this;
        }

        public Map<T, E> build() {
            return map;
        }
    }
}