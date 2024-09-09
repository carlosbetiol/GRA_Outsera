package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.MovieAwardCriteria;
import com.outsera.goldenraspberryawards.domain.model.MovieAward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MovieAwardService {

    MovieAward save(MovieAward movieAward);

    MovieAward update(MovieAward movieAward);

    MovieAward saveLogLess(MovieAward movieAward);

    MovieAward findById(Long id);

    Page<MovieAward> findAll(MovieAwardCriteria criteria, Pageable pageable);

    List<MovieAward> findAll();

    void delete(Long id);

}
