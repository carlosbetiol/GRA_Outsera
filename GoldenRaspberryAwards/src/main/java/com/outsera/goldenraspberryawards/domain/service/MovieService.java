package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.MovieCriteria;
import com.outsera.goldenraspberryawards.domain.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {

    Movie save(Movie movie);

    Movie findById(Long id);

    Page<Movie> findAll(MovieCriteria criteria, Pageable pageable);

    List<Movie> findAll();

    void delete(Long id);

}
