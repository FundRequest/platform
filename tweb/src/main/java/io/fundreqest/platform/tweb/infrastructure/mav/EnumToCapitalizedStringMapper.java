package io.fundreqest.platform.tweb.infrastructure.mav;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import org.apache.commons.lang.WordUtils;
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
