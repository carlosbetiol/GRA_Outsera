package com.outsera.goldenraspberryawards.domain.specification;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.StudioCriteria;
import com.outsera.goldenraspberryawards.domain.model.Movie;
import com.outsera.goldenraspberryawards.domain.model.MovieAward;
import com.outsera.goldenraspberryawards.domain.model.Studio;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StudioSpecification {

    public static Specification<Studio> byCriteria(StudioCriteria criteria) {

        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Studio, Movie> movieJoin = root.join("movies", JoinType.LEFT);
            Join<Movie, MovieAward> movieAwardJoin = movieJoin.join("movieAwards", JoinType.LEFT);

            if (criteria.getJustAwardedYears() != null && criteria.getJustAwardedYears() ) {
                predicates.add(builder.isNotNull(movieAwardJoin.get("id")));
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
                                searchPredicates.add(builder.like(root.get("name"), word ));
                            } else {
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
