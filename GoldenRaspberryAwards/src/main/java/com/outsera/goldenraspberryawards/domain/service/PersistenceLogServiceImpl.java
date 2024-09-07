package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.PersistenceLogCriteria;
import com.outsera.goldenraspberryawards.domain.exception.PersistenceLogNotFoundException;
import com.outsera.goldenraspberryawards.domain.model.PersistenceLog;
import com.outsera.goldenraspberryawards.domain.repository.PersistenceLogRepository;
import com.outsera.goldenraspberryawards.domain.specification.PersistenceLogSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersistenceLogServiceImpl implements PersistenceLogService {

    private final PersistenceLogRepository persistenceLogRepository;

    public PersistenceLogServiceImpl(PersistenceLogRepository persistenceLogRepository) {
        this.persistenceLogRepository = persistenceLogRepository;
    }

    @Override
    @Transactional
    public PersistenceLog save(PersistenceLog log) {
        return persistenceLogRepository.save(log);
    }

    @Override
    public Page<PersistenceLog> findAll(PersistenceLogCriteria criteria, Pageable pageable) {
        Specification<PersistenceLog> spec = PersistenceLogSpecification.byCriteria(criteria);
        return persistenceLogRepository.findAll(spec, pageable);
    }

    @Override
    public PersistenceLog findById(Long id) {
        return persistenceLogRepository.findById(id).orElseThrow(() -> new PersistenceLogNotFoundException(id));
    }
}
