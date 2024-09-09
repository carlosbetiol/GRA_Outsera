package com.outsera.goldenraspberryawards.domain.specification;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.CriteriaStatusEnum;
import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.RoleCriteria;
import com.outsera.goldenraspberryawards.domain.model.Permission;
import com.outsera.goldenraspberryawards.domain.model.Role;
import com.outsera.goldenraspberryawards.domain.model.RolePermission;
import com.outsera.goldenraspberryawards.domain.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RoleSpecification {

    public static Specification<Role> byCriteria(RoleCriteria criteria) {

        criteria.resolveMandatoryData();

        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Role, RolePermission> rolePermissionsJoin = root.join("rolePermissions", JoinType.LEFT);
            Join<RolePermission, Permission> permissionJoin = rolePermissionsJoin.join("permission", JoinType.LEFT);
            Join<Role, User> userJoin = root.join("users", JoinType.LEFT);

            if (criteria.getStatus() != null && !criteria.getStatus().equals(CriteriaStatusEnum.ALL)) {
                predicates.add(builder.equal(root.get("isActive"), criteria.getStatus().equals(CriteriaStatusEnum.ACTIVE)));
            }

            if (criteria.getPermissionIdentifier() != null && !criteria.getPermissionIdentifier().isEmpty() ) {
                predicates.add(permissionJoin.get("identifier").in(criteria.getPermissionIdentifier()));
            }

            if (criteria.getUserId() != null && !criteria.getUserId().isEmpty() ) {
                predicates.add(userJoin.get("id").in(criteria.getUserId()));
            }

            if (criteria.getUserEmail() != null && !criteria.getUserEmail().isEmpty() ) {
                predicates.add(userJoin.get("email").in(criteria.getUserEmail()));
            }

            if (criteria.getSearch() != null && !criteria.getSearch().isEmpty()) {
                Predicate searchPredicate = builder.conjunction();
                for (String term : criteria.getSearch()) {
                    Predicate termPredicate = builder.disjunction();
                    String[] terms = term.split("\\s+");
                    for (String word : terms) {

                        Predicate idPredicate;

                        Boolean isNumericValue = false;

                        try {
                            Integer intValue = Integer.valueOf(word);
                            idPredicate = builder.equal(root.get("id"), intValue);
                            isNumericValue = true;

                        } catch (NumberFormatException e) {
                            idPredicate = builder.equal(builder.literal(0), 1);
                        }

                        List<Predicate> searchPredicates = new ArrayList<>();
                        searchPredicates.add(idPredicate);

                        if (!isNumericValue || terms.length > 1) {

                            word = word.charAt(0) == '=' ? word.substring(1) : "%" + word + "%";

                            if( word.charAt(0) == '%') {
                                searchPredicates.add(builder.like(root.get("identifier"), word ));
                                searchPredicates.add(builder.like(root.get("name"), word ));
                            } else {
                                searchPredicates.add(builder.equal(root.get("identifier"), word ));
                                searchPredicates.add(builder.equal(root.get("name"), word ));
                            }
                        }

                        Predicate or = builder.or(
                                searchPredicates.toArray(new Predicate[0])
                        );

                        termPredicate = builder.or(termPredicate, or);
                    }
                    searchPredicate = builder.and(searchPredicate, termPredicate);
                }
                predicates.add(searchPredicate);
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
