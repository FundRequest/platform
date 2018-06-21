package io.fundreqest.platform.tweb.actuator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.endpoint.EnvironmentEndpoint;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicEnvironmentEndpointTest {

    private PublicEnvironmentEndpoint endpoint;

    private EnvironmentEndpoint environmentEndpoint;
    private Environment env;

    @BeforeEach
    void setUp() {
        environmentEndpoint = mock(EnvironmentEndpoint.class);
        env = mock(Environment.class);
        endpoint = new PublicEnvironmentEndpoint(environmentEndpoint, true, false, env);
    }

    @Test
    void invoke() {
        when(env.getProperty("notAGroup2.public")).thenReturn("false");
        when(env.getProperty("notAGroup3")).thenReturn("aefsd");
        when(env.getProperty("notAGroup3.public")).thenReturn("true");
        when(env.getProperty("aszgf.dsads")).thenReturn("sdf");
        when(env.getProperty("aszgf.dsads.public")).thenReturn("true");
        when(env.getProperty("group1.public")).thenReturn("false");
        when(env.getProperty("vxvdf.dscvds.ad")).thenReturn("wqwe");
        when(env.getProperty("vxvdf.dscvds.ad.public")).thenReturn("true");
        when(env.getProperty("wdoij.oijda.acs")).thenReturn("efsef");
        when(env.getProperty("wdoij.oijda.acs.public")).thenReturn("true");
        when(env.getProperty("cgv.fgchv.dwf")).thenReturn("kjhg");
        when(env.getProperty("cgv.fgchv.dwf.public")).thenReturn("true");

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
                .put("group2", MapBuilder.<String, Object>builder()
                        .put("vxvdf.dscvds.ad", "wqwe")
                        .put("wdoij.oijda.acs", "efsef")
                        .put("aszgf.dsads", "blah")
                        .put("wdoij.oijda.acs.public", "true")
                        .build())
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
                                             .put("wdoij.oijda.acs", "efsef")
                                             .put("cgv.fgchv.dwf", "kjhg")
                                             .build());
    }

    @Test
    void overwritten() {
        when(env.getProperty("network")).thenReturn("kovan");
        when(env.getProperty("network.public")).thenReturn("true");

        final Map<String, Object> envProperties = MapBuilder.<String, Object>builder()
                .put("network", "kovan")
                .put("network", "main")
                .put("network.public", "true")
                .build();

        when(environmentEndpoint.invoke()).thenReturn(envProperties);

        final Map<String, Object> result = endpoint.invoke();

        assertThat(result).isEqualTo(MapBuilder.<String, Object>builder()
                                             .put("network", "kovan")
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