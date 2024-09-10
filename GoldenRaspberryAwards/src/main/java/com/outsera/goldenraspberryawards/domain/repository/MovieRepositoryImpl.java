package com.outsera.goldenraspberryawards.domain.repository;

import com.outsera.goldenraspberryawards.domain.model.Movie;
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
public class MovieRepositoryImpl implements MovieRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<Movie> findAll(Specification<Movie> spec, Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);

        criteriaQuery.distinct(true);

        Predicate predicate = spec.toPredicate(root, criteriaQuery, criteriaBuilder);
        if (predicate != null) {
            criteriaQuery.where(predicate);
        }

        if (pageable.isPaged() && pageable.getSort().isSorted()) {
            criteriaQuery.orderBy(toOrders(pageable.getSort(), root, criteriaBuilder));
        }

        TypedQuery<Movie> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((int) pageable.getOffset() );
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Movie> content = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Movie> countRoot = countQuery.from(Movie.class);
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
    public void detach(Movie movie) {
        entityManager.detach(movie);
    }

    private Order[] toOrders(Sort sort, Root<Movie> root, CriteriaBuilder criteriaBuilder) {
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




