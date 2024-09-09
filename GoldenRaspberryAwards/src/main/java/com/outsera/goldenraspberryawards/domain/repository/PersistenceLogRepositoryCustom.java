package com.outsera.goldenraspberryawards.domain.repository;

import com.outsera.goldenraspberryawards.domain.model.PersistenceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface PersistenceLogRepositoryCustom {

    Page<PersistenceLog> findAll(Specification<PersistenceLog> spec, Pageable pageable);

}
