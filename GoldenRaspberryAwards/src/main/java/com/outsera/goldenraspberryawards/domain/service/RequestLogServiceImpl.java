package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.RequestLogCriteria;
import com.outsera.goldenraspberryawards.domain.exception.RequestLogNotFoundException;
import com.outsera.goldenraspberryawards.domain.model.RequestLog;
import com.outsera.goldenraspberryawards.domain.repository.RequestLogRepository;
import com.outsera.goldenraspberryawards.domain.specification.RequestLogSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestLogServiceImpl implements RequestLogService {

    private final RequestLogRepository requestLogRepository;

    public RequestLogServiceImpl(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    @Override
    @Transactional
    public RequestLog save(RequestLog log) {
        return requestLogRepository.save(log);
    }

    @Override
    public Page<RequestLog> findAll(RequestLogCriteria criteria, Pageable pageable) {
        Specification<RequestLog> spec = RequestLogSpecification.byCriteria(criteria);
        return requestLogRepository.findAll(spec, pageable);
    }

    @Override
    public RequestLog findById(Long id) {
        return requestLogRepository.findById(id).orElseThrow(() -> new RequestLogNotFoundException(id));
    }
}
