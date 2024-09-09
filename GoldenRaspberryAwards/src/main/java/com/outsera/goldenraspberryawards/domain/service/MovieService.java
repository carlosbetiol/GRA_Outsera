package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.MovieCriteria;
import com.outsera.goldenraspberryawards.domain.model.Movie;
import com.outsera.goldenraspberryawards.domain.model.MovieAward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    Movie save(Movie movie);

    Movie update(Movie movie);

    Movie saveLogLess(Movie movie);

    Movie findById(Long id);

    Optional<Movie> findByName(String name);

    Page<Movie> findAll(MovieCriteria criteria, Pageable pageable);

    List<Movie> findAll();

    void delete(Long id);

}
