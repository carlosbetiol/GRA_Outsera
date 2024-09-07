package com.outsera.goldenraspberryawards.domain.specification;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.PersistenceLogCriteria;
import com.outsera.goldenraspberryawards.domain.model.PersistenceLog;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class PersistenceLogSpecification {

    public static Specification<PersistenceLog> byCriteria(PersistenceLogCriteria criteria) {

        criteria.resolveMandatoryData();

        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getStartDate() != null && criteria.getEndDate() == null) {
                predicates.add(builder.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        criteria.getStartDate().atStartOfDay().atZone(ZoneId.systemDefault()).toOffsetDateTime()
                ));
            }

            if (criteria.getStartDate() == null && criteria.getEndDate() != null) {
                predicates.add(builder.lessThanOrEqualTo(
                        root.get("createdAt"),
                        criteria.getEndDate().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toOffsetDateTime()
                ));
            }

            if (criteria.getStartDate() != null && criteria.getEndDate() != null) {
                predicates.add(builder.between(
                        root.get("createdAt"),
                        criteria.getStartDate().atStartOfDay().atZone(ZoneId.systemDefault()).toOffsetDateTime(),
                        criteria.getEndDate().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toOffsetDateTime()
                ));
            }

            if (criteria.getRequestUUID() != null && !criteria.getRequestUUID().isEmpty()) {
                predicates.add(root.get("requestUUID").in(criteria.getRequestUUID()));
            }

            if (criteria.getEntityName() != null && !criteria.getEntityName().isEmpty()) {
                predicates.add(root.get("entityName").in(criteria.getEntityName()));
            }

            if (criteria.getEntityId() != null && !criteria.getEntityId().isEmpty()) {
                predicates.add(root.get("entityId").in(criteria.getEntityId()));
            }

            if (criteria.getUserId() != null && !criteria.getUserId().isEmpty()) {
                predicates.add(root.get("user").get("id").in(criteria.getUserId()));
            }

            if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
                predicates.add(root.get("user").get("email").in(criteria.getEmail()));
            }

            if (criteria.getAction() != null && !criteria.getAction().isEmpty()) {
                predicates.add(root.get("action").in(criteria.getAction()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
