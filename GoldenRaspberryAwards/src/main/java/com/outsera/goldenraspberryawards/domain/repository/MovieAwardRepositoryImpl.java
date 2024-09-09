package com.outsera.goldenraspberryawards.domain.repository;

import com.outsera.goldenraspberryawards.domain.model.MovieAward;
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
public class MovieAwardRepositoryImpl implements MovieAwardRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<MovieAward> findAll(Specification<MovieAward> spec, Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MovieAward> criteriaQuery = criteriaBuilder.createQuery(MovieAward.class);
        Root<MovieAward> root = criteriaQuery.from(MovieAward.class);

        criteriaQuery.distinct(true);

        Predicate predicate = spec.toPredicate(root, criteriaQuery, criteriaBuilder);
        if (predicate != null) {
            criteriaQuery.where(predicate);
        }

        if (pageable.isPaged() && pageable.getSort().isSorted()) {
            criteriaQuery.orderBy(toOrders(pageable.getSort(), root, criteriaBuilder));
        }

        TypedQuery<MovieAward> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((int) pageable.getOffset() );
        typedQuery.setMaxResults(pageable.getPageSize());

        List<MovieAward> content = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<MovieAward> countRoot = countQuery.from(MovieAward.class);
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

    @Override
    public void detach(MovieAward movieAward) {
        entityManager.detach(movieAward);
    }

    private Order[] toOrders(Sort sort, Root<MovieAward> root, CriteriaBuilder criteriaBuilder) {
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




