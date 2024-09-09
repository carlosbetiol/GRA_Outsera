package com.outsera.goldenraspberryawards.domain.repository;

import com.outsera.goldenraspberryawards.domain.model.Studio;
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
public class StudioRepositoryImpl implements StudioRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<Studio> findAll(Specification<Studio> spec, Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Studio> criteriaQuery = criteriaBuilder.createQuery(Studio.class);
        Root<Studio> root = criteriaQuery.from(Studio.class);

        criteriaQuery.distinct(true);

        Predicate predicate = spec.toPredicate(root, criteriaQuery, criteriaBuilder);
        if (predicate != null) {
            criteriaQuery.where(predicate);
        }

        if (pageable.isPaged() && pageable.getSort().isSorted()) {
            criteriaQuery.orderBy(toOrders(pageable.getSort(), root, criteriaBuilder));
        }

        TypedQuery<Studio> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((int) pageable.getOffset() );
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Studio> content = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Studio> countRoot = countQuery.from(Studio.class);
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

    private Order[] toOrders(Sort sort, Root<Studio> root, CriteriaBuilder criteriaBuilder) {
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




