package com.outsera.goldenraspberryawards.domain.specification;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.MovieAwardCriteria;
import com.outsera.goldenraspberryawards.domain.model.Movie;
import com.outsera.goldenraspberryawards.domain.model.MovieAward;
import com.outsera.goldenraspberryawards.domain.model.Producer;
import com.outsera.goldenraspberryawards.domain.model.Studio;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MovieAwardSpecification {

    public static Specification<MovieAward> byCriteria(MovieAwardCriteria criteria) {

        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<MovieAward, Movie> movieJoin = root.join("movie", JoinType.INNER);
            Join<Movie, Studio> studioJoin = movieJoin.join("studios", JoinType.INNER);
            Join<Movie, Producer> producerJoin = movieJoin.join("producers", JoinType.INNER);

            if (criteria.getAwardYear() != null && !criteria.getAwardYear().isEmpty()) {
                predicates.add(root.get("awardYear").in(criteria.getAwardYear()));
            }

            if (criteria.getMovieId() != null && !criteria.getMovieId().isEmpty()) {
                predicates.add(movieJoin.get("id").in(criteria.getMovieId()));
            }

            if (criteria.getStudioId() != null && !criteria.getStudioId().isEmpty()) {
                predicates.add(studioJoin.get("id").in(criteria.getStudioId()));
            }

            if (criteria.getProducerId() != null && !criteria.getProducerId().isEmpty()) {
                predicates.add(producerJoin.get("id").in(criteria.getProducerId()));
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
                                searchPredicates.add(builder.like(movieJoin.get("name"), word ));
                                searchPredicates.add(builder.like(studioJoin.get("name"), word ));
                                searchPredicates.add(builder.like(producerJoin.get("name"), word ));
                            } else {
                                searchPredicates.add(builder.equal(movieJoin.get("name"), word ));
                                searchPredicates.add(builder.equal(studioJoin.get("name"), word ));
                                searchPredicates.add(builder.equal(producerJoin.get("name"), word ));
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
