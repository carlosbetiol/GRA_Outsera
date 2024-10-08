package com.outsera.goldenraspberryawards.domain.repository;

import com.outsera.goldenraspberryawards.domain.model.PersistenceLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersistenceLogRepositoryImpl implements PersistenceLogRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<PersistenceLog> findAll(Specification<PersistenceLog> spec, Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PersistenceLog> criteriaQuery = criteriaBuilder.createQuery(PersistenceLog.class);
        Root<PersistenceLog> root = criteriaQuery.from(PersistenceLog.class);

        root.fetch("user", JoinType.LEFT);

        criteriaQuery.distinct(true);

        Predicate predicate = spec.toPredicate(root, criteriaQuery, criteriaBuilder);
        if (predicate != null) {
            criteriaQuery.where(predicate);
        }

        if (pageable.isPaged() && pageable.getSort().isSorted()) {
            criteriaQuery.orderBy(toOrders(pageable.getSort(), root, criteriaBuilder));
        }

        TypedQuery<PersistenceLog> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((int) pageable.getOffset() );
        typedQuery.setMaxResults(pageable.getPageSize());

        List<PersistenceLog> content = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<PersistenceLog> countRoot = countQuery.from(PersistenceLog.class);
        countQuery.select(criteriaBuilder.countDistinct(countRoot.get("id")));
        if (predicate != null) {
            Predicate countPredicate = spec.toPredicate(countRoot, countQuery, criteriaBuilder);
            if (countPredicate != null) {
                countQuery.where(countPredicate);
            }
        }

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(content, pageable, total);

    }

    private Order[] toOrders(Sort sort, Root<PersistenceLog> root, CriteriaBuilder criteriaBuilder) {
        return sort.stream()
                .map(order -> {
                    if (order.getDirection() == Sort.Direction.ASC) {
                        return criteriaBuilder.asc(root.get(order.getProperty()));
                    } else {
                        return criteriaBuilder.desc(root.get(order.getProperty()));
                    }
                })
                .toArray(Order[]::new);
    }

}




