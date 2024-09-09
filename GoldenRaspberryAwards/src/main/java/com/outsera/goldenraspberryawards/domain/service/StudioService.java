package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.StudioCriteria;
import com.outsera.goldenraspberryawards.domain.model.Studio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudioService {

    Studio save(Studio studio);

    Studio saveLogLess(Studio studio);

    Studio findById(Long id);

    Optional<Studio> findByName(String name);

    Page<Studio> findAll(StudioCriteria criteria, Pageable pageable);

    List<Studio> findAll();

    void delete(Long id);

}
