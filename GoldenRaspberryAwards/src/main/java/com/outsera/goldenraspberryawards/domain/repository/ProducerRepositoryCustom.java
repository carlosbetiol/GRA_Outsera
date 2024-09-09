package com.outsera.goldenraspberryawards.domain.repository;

import com.outsera.goldenraspberryawards.domain.model.Producer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

public interface ProducerRepositoryCustom {

    Page<Producer> findAll(Specification<Producer> spec, Pageable pageable);

    Map<String, List<Integer>> findProducersAwardedYears();

}
