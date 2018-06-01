package io.fundreqest.platform.tweb.actuator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.actuate.endpoint.EnvironmentEndpoint;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PublicEnvironmentEndpoint extends AbstractEndpoint<Map<String, Object>> {

    private static final String PUB_ENV_ID = "pubenv";
    private final EnvironmentEndpoint environmentEndpoint;

    public PublicEnvironmentEndpoint(final EnvironmentEndpoint environmentEndpoint,
                                     @Value("${endpoints.pubenv.enabled:false}") final Boolean enabled,
                                     @Value("${endpoints.pubenv.sensitive:true}") final Boolean sensitive) {
        super(PUB_ENV_ID, sensitive, enabled);
        this.environmentEndpoint = environmentEndpoint;
    }

    @Override
    public Map<String, Object> invoke() {
        return filterOutNonPublicProperties(environmentEndpoint.invoke());
    }

    private Map<String, Object> filterOutNonPublicProperties(final Map<String, Object> sourceMap) {
        final HashMap<String, Object> resultMap = new HashMap<>();

        sourceMap.entrySet()
                 .forEach(entry -> {
                     if (isPublicProperty(sourceMap, entry.getKey())) {
                         processPublicProperty(resultMap, entry);
                     } else if (isMap(entry.getValue())) {
                         final Map<String, Object> filtered = filterOutNonPublicProperties((Map<String, Object>) entry.getValue());
                         if (!filtered.isEmpty()) {
                             resultMap.putAll(flattenMap(filtered).collect(getEntriesToMapCollector()));
                         }
                     }
                 });
        return resultMap;
    }

    private void processPublicProperty(final HashMap<String, Object> resultMap, final Map.Entry<String, Object> entry) {
        if (isMap(entry.getValue())) {
            resultMap.putAll(flattenEntries(entry).filter(getFilterOutPublicKeys()).collect(getEntriesToMapCollector()));
        } else {
            resultMap.put(entry.getKey(), entry.getValue());
        }
    }

    private Stream<Map.Entry<String, Object>> flattenMap(Map<String, Object> map) {
        return map.entrySet().stream().flatMap(this::flattenEntries);
    }

    public Stream<Map.Entry<String, Object>> flattenEntries(final Map.Entry<String, Object> entry) {
        if (isMap(entry.getValue())) {
            return flattenMap((Map<String, Object>) entry.getValue());
        }
        return Stream.of(entry);
    }

    private boolean isPublicProperty(final Map<String, Object> allProperties, final String key) {
        return "true".equals(allProperties.get(key + ".public"));
    }

    private Predicate<Map.Entry<String, Object>> getFilterOutPublicKeys() {
        return subEntry -> !subEntry.getKey().endsWith(".public");
    }

    private Collector<Map.Entry<String, Object>, ?, Map<String, Object>> getEntriesToMapCollector() {
        return Collectors.toMap((Map.Entry::getKey), (Map.Entry::getValue));
    }

    private boolean isMap(final Object value) {
        return value instanceof Map<?, ?>;
    }
}
