package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.PersistenceLogCriteria;
import com.outsera.goldenraspberryawards.domain.model.PersistenceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersistenceLogService {

    PersistenceLog save(PersistenceLog log);

    Page<PersistenceLog> findAll(PersistenceLogCriteria criteria, Pageable pageable);

    PersistenceLog findById(Long id);

}
