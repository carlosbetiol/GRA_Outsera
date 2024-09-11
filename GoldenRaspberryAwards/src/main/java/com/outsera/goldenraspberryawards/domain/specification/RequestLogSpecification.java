package com.outsera.goldenraspberryawards.domain.specification;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.RequestLogCriteria;
import com.outsera.goldenraspberryawards.domain.model.RequestLog;
import com.outsera.goldenraspberryawards.domain.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RequestLogSpecification {

    public static Specification<RequestLog> byCriteria(RequestLogCriteria criteria) {

        criteria.resolveMandatoryData();

        return (root, query, builder) -> {

            Join<RequestLog, User> userJoin = root.join("user", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getStartAt() != null && criteria.getEndAt() == null) {
                predicates.add(builder.greaterThanOrEqualTo(
                        root.get("requestTime"),
                        criteria.getStartAt()
                ));
            }

            if (criteria.getStartAt() == null && criteria.getEndAt() != null) {
                predicates.add(builder.lessThanOrEqualTo(
                        root.get("requestTime"),
                        criteria.getEndAt()
                ));
            }

            if (criteria.getStartAt() != null && criteria.getEndAt() != null) {
                predicates.add(builder.between(
                        root.get("requestTime"),
                        criteria.getStartAt(),
                        criteria.getEndAt()
                ));
            }

            if (criteria.getUserId() != null && !criteria.getUserId().isEmpty()) {
                predicates.add(userJoin.get("id").in(criteria.getUserId()));
            }

            if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
                predicates.add(userJoin.get("email").in(criteria.getEmail()));
            }

            if (criteria.getRequestUUID() != null && !criteria.getRequestUUID().isEmpty()) {
                predicates.add(root.get("requestUUID").in(criteria.getRequestUUID()));
            }

            if (criteria.getApiUrl() != null && !criteria.getApiUrl().isEmpty()) {
                predicates.add(builder.like(root.get("apiUrl"), "%" + criteria.getApiUrl() + "%"));
            }

            if (criteria.getMethod() != null && !criteria.getMethod().isEmpty()) {
                predicates.add(root.get("method").in(criteria.getMethod()));
            }

            if (criteria.getRequestDTO() != null && !criteria.getRequestDTO().isEmpty()) {
                predicates.add(root.get("requestDto").in(criteria.getRequestDTO()));
            }

            if (criteria.getResponseDTO() != null && !criteria.getResponseDTO().isEmpty()) {
                predicates.add(root.get("responseDto").in(criteria.getResponseDTO()));
            }

            if (criteria.getResponseStatusCode() != null && !criteria.getResponseStatusCode().isEmpty()) {
                predicates.add(root.get("responseStatusCode").in(criteria.getResponseStatusCode()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
