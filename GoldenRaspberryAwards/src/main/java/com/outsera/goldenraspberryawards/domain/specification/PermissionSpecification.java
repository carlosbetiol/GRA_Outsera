package com.outsera.goldenraspberryawards.domain.specification;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.PermissionCriteria;
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

public class PermissionSpecification {

    public static Specification<Permission> byCriteria(PermissionCriteria criteria) {

        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Permission, RolePermission> permissionRolesJoin = root.join("permissionRoles", JoinType.LEFT);
            Join<RolePermission, Role> roleJoin = permissionRolesJoin.join("role", JoinType.LEFT);
            Join<Role, User> userJoin = roleJoin.join("users", JoinType.LEFT);

            if (criteria.getPermissionIdentifier() != null && !criteria.getPermissionIdentifier().isEmpty() ) {
                predicates.add(root.get("identifier").in(criteria.getPermissionIdentifier()));
            }

            if (criteria.getRoleIdentifier() != null && !criteria.getRoleIdentifier().isEmpty() ) {
                predicates.add(roleJoin.get("identifier").in(criteria.getRoleIdentifier()));
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
                                searchPredicates.add(builder.like(root.get("description"), word ));
                            } else {
                                searchPredicates.add(builder.equal(root.get("identifier"), word ));
                                searchPredicates.add(builder.equal(root.get("description"), word ));
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
