package com.outsera.goldenraspberryawards.domain.repository;

import com.outsera.goldenraspberryawards.domain.model.Movie;
import com.outsera.goldenraspberryawards.domain.model.MovieAward;
import com.outsera.goldenraspberryawards.domain.model.Producer;
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

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ProducerRepositoryImpl implements ProducerRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<Producer> findAll(Specification<Producer> spec, Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Producer> criteriaQuery = criteriaBuilder.createQuery(Producer.class);
        Root<Producer> root = criteriaQuery.from(Producer.class);

        criteriaQuery.distinct(true);

        Predicate predicate = spec.toPredicate(root, criteriaQuery, criteriaBuilder);
        if (predicate != null) {
            criteriaQuery.where(predicate);
        }

        if (pageable.isPaged() && pageable.getSort().isSorted()) {
            criteriaQuery.orderBy(toOrders(pageable.getSort(), root, criteriaBuilder));
        }

        TypedQuery<Producer> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((int) pageable.getOffset() );
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Producer> content = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Producer> countRoot = countQuery.from(Producer.class);
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

    private jakarta.persistence.criteria.Order[] toOrders(Sort sort, Root<Producer> root, CriteriaBuilder criteriaBuilder) {
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

    @Override
    public Map<String, List<Integer>> findProducersAwardedYears() {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Producer> criteriaQuery = criteriaBuilder.createQuery(Producer.class);
        Root<Producer> root = criteriaQuery.from(Producer.class);
        Fetch<Producer, Movie> fetchMovie = root.fetch("movies", JoinType.INNER);
        fetchMovie.fetch("movieAwards", JoinType.INNER);

        criteriaQuery.distinct(true);

        TypedQuery<Producer> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Producer> content = typedQuery.getResultList();

        Map<String, Set<Integer>> producerYearsMap = new HashMap<>();

        content.forEach(producer -> {
            producerYearsMap.compute(producer.getName(), (k, v) -> {
                Set<Integer> years = producer.getMovies().stream()
                        .map(Movie::getMovieAwards)
                        .flatMap(Set::stream)
                        .map(MovieAward::getAwardYear)
                        .collect(Collectors.toSet());

                if (v == null) {
                    return years;
                } else {
                    v.addAll(years);
                    return v;
                }
            });
        });

        return producerYearsMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    List<Integer> years = new ArrayList<>(e.getValue());
                    years.sort(Comparator.naturalOrder());
                    return years;
                }));

    }


}




