package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.ProducerCriteria;
import com.outsera.goldenraspberryawards.domain.model.Producer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProducerService {

    Producer save(Producer producer);

    Producer findById(Long id);

    Page<Producer> findAll(ProducerCriteria criteria, Pageable pageable);

    List<Producer> findAll();

    void delete(Long id);

}
