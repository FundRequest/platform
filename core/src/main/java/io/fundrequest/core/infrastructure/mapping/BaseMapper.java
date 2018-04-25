package io.fundrequest.core.infrastructure.mapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface BaseMapper<IN, OUT> {

    OUT map(IN in);

    default Optional<OUT> mapToOptional(IN in) {
        return Optional.ofNullable(in).map(BaseMapper.this::map);
    }

    default List<OUT> mapToList(final Collection<? extends IN> collectionIn) {
        return collectionIn.stream().map(BaseMapper.this::map).collect(Collectors.toList());
    }

    default Set<OUT> mapToSet(final Collection<? extends IN> collectionIn) {
        return collectionIn.stream().map(BaseMapper.this::map).collect(Collectors.toSet());
    }

    default Page<OUT> mapToPage(final Page<? extends IN> pageIn) {
        final List<OUT> collect = pageIn.getContent()
                                        .stream()
                                        .map(this::map)
                                        .collect(Collectors.toList());

        return new PageImpl<>(collect, null, pageIn.getTotalElements());
    }

}