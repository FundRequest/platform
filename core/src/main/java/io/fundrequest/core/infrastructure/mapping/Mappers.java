package io.fundrequest.core.infrastructure.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class Mappers {

    private final Map<FromTo, BaseMapper> mappers;

    @Autowired
    public Mappers(List<BaseMapper> baseMappers) {
        mappers = baseMappers.stream()
                             .filter(m -> !m.getClass().getSimpleName().endsWith("_"))
                             .collect(Collectors.toMap(
                                     this::getClasses,
                                     Function.identity()
                                                      ));
    }

    private FromTo getClasses(final BaseMapper b) {
        Class<?>[] classes = ResolvableType.forClass(b.getClass()).as(BaseMapper.class).resolveGenerics();
        return new FromTo(classes[0], classes[1]);
    }

    public <IN, OUT> BaseMapper<IN, OUT> getMapper(Class<IN> clazzIn, Class<OUT> clazzOut) {
        BaseMapper baseMapper = mappers.get(new FromTo(clazzIn, clazzOut));
        if (baseMapper == null) {
            throw new RuntimeException("Mapper was not found");
        }
        return baseMapper;
    }

    public <IN, OUT> OUT map(final Class<? super IN> clazzIn, Class<OUT> clazzOut, IN in) {
        return getMapper(clazzIn, clazzOut).map(in);
    }

    public <IN, OUT> List<OUT> mapList(final Class<? super IN> clazzIn, Class<OUT> clazzOut, Collection<IN> in) {
        return getMapper(clazzIn, clazzOut).mapToList(in);
    }

    public <IN, OUT> Page<OUT> mapToPage(final Class<IN> clazzIn, Class<OUT> clazzOut,
                                         final Page<? extends IN> pageIn) {
        return getMapper(clazzIn, clazzOut).mapToPage(pageIn);
    }

    private class FromTo {
        private Class from;
        private Class to;

        FromTo(final Class from, final Class to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final FromTo fromTo = (FromTo) o;
            return Objects.equals(from, fromTo.from) &&
                   Objects.equals(to, fromTo.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }
}