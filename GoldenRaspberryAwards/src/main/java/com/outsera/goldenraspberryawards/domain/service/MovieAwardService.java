package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.MovieAwardCriteria;
import com.outsera.goldenraspberryawards.domain.model.Movie;
import com.outsera.goldenraspberryawards.domain.model.MovieAward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieAwardService {

    MovieAward save(MovieAward movieAward);

    MovieAward saveLogLess(MovieAward movieAward);

    MovieAward findById(Long id);

    Page<MovieAward> findAll(MovieAwardCriteria criteria, Pageable pageable);

    void delete(Long id);

}
