package io.fundrequest.core.request.infrastructure;

import io.fundrequest.core.request.domain.Request;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.criteria.JoinType.INNER;

public class RequestSpecification implements Specification<Request> {

    private final List<String> projects;
    private final List<String> technologies;
    private final Long lastUpdatedSinceDays;

    public RequestSpecification(final List<String> projects, final List<String> technologies, final Long lastUpdatedSinceDays) {
        this.projects = projects.stream().map(String::toLowerCase).collect(Collectors.toList());
        this.technologies = technologies.stream().map(String::toLowerCase).collect(Collectors.toList());
        this.lastUpdatedSinceDays = lastUpdatedSinceDays;
    }

    @Override
    public Predicate toPredicate(final Root<Request> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();
        predicates.add(createProjectPredicate(root, criteriaBuilder));
        predicates.addAll(createTechnologyPredicates(root, criteriaBuilder));
        if (lastUpdatedSinceDays > 0L) {
            predicates.add(criteriaBuilder.greaterThan(root.get("lastModifiedDate"), LocalDate.now().minusDays(lastUpdatedSinceDays).atStartOfDay()));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate createProjectPredicate(Root<Request> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lower(root.get("issueInformation").get("owner")).in(projects);
    }

    private List<Predicate> createTechnologyPredicates(final Root<Request> root, final CriteriaBuilder criteriaBuilder) {
        return technologies.stream()
                           .map(technology -> createTechnologyPredicate(criteriaBuilder, root, technology))
                           .collect(Collectors.toList());
    }

    private Predicate createTechnologyPredicate(final CriteriaBuilder criteriaBuilder, final Root<Request> root, final String technology) {
        final SetJoin<Object, Object> technologiesJoin = root.joinSet("technologies", INNER);
        return criteriaBuilder.equal(criteriaBuilder.lower(technologiesJoin.get("technology")), technology);
    }
}
