package io.fundrequest.common.infrastructure.mav;

import io.fundrequest.common.infrastructure.mapping.BaseMapper;
import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Component;

@Component
public class EnumToCapitalizedStringMapper implements BaseMapper<Enum, String> {

    @Override
    public String map(final Enum anEnum) {
        if (anEnum == null) {
            return null;
        }
        return WordUtils.capitalizeFully(anEnum.name().replace('_', ' '));
    }
}
