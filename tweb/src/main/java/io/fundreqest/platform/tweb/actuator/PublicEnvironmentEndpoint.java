package io.fundreqest.platform.tweb.actuator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.actuate.endpoint.EnvironmentEndpoint;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PublicEnvironmentEndpoint extends AbstractEndpoint<Map<String, Object>> {

    private static final String PUB_ENV_ID = "pubenv";
    private final EnvironmentEndpoint environmentEndpoint;
    private Environment env;

    public PublicEnvironmentEndpoint(final EnvironmentEndpoint environmentEndpoint,
                                     @Value("${endpoints.pubenv.enabled:false}") final Boolean enabled,
                                     @Value("${endpoints.pubenv.sensitive:true}") final Boolean sensitive,
                                     Environment env) {
        super(PUB_ENV_ID, sensitive, enabled);
        this.environmentEndpoint = environmentEndpoint;
        this.env = env;
    }

    @Override
    public Map<String, Object> invoke() {
        return filterOutNonPublicProperties(environmentEndpoint.invoke());
    }

    private Map<String, Object> filterOutNonPublicProperties(final Map<String, Object> sourceMap) {
        return sourceMap.entrySet()
                        .stream()
                        .flatMap(this::flattenEntries)
                        .map(Map.Entry::getKey)
                        .filter(x -> x.endsWith(".public") && "true".equals(env.getProperty(x)))
                        .map(x -> x.replaceAll(".public", ""))
                        .collect(Collectors.toMap(x -> x, x -> env.getProperty(x)));
    }

    private Stream<Map.Entry<String, Object>> flattenEntries(final Map.Entry<String, Object> entry) {
        if (isMap(entry.getValue())) {
            return flattenMap((Map<String, Object>) entry.getValue());
        }
        return Stream.of(entry);
    }

    private Stream<Map.Entry<String, Object>> flattenMap(Map<String, Object> map) {
        return map.entrySet().stream().flatMap(this::flattenEntries);
    }

    private boolean isMap(final Object value) {
        return value instanceof Map<?, ?>;
    }
}
