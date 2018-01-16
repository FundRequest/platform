package io.fundrequest.core.request.erc67;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ERC67 {
    private String network = "ethereum";
    private String address;
    private Map<String, String> parameters = new HashMap<>();

    public String visualize() {
        require(address);

        final StringBuilder builder = new StringBuilder(network);
        builder.append(":").append(address);
        if (!parameters.isEmpty()) {
            builder.append("?");
            parameters
                    .forEach((key, value) -> builder.append(key).append("=").append(value).append("&"));
            builder.deleteCharAt(builder.lastIndexOf("&"));
        }

        return builder.toString();
    }

    private void require(final String address) {
        if (StringUtils.isEmpty(address)) {
            throw new IllegalArgumentException("Address is mandatory");
        }
    }

    public void addParameter(final String key, final String value) {
        this.parameters.put(key, value);
    }

    public static class Builder {

        private ERC67 result;

        public Builder() {
            this.result = new ERC67();
        }

        public Builder withNetwork(final String network) {
            result.network = network;
            return this;
        }

        public Builder withAddress(final String address) {
            result.address = address;
            return this;
        }

        public Builder withParameter(final String key, final String value) {
            result.addParameter(key, value);
            return this;
        }

        public ERC67 build() {
            return result;
        }
    }
}
