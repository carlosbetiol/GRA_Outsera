package com.outsera.goldenraspberryawards.domain.service;


import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.RequestLogCriteria;
import com.outsera.goldenraspberryawards.domain.model.RequestLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestLogService {

    RequestLog save(RequestLog log);

    Page<RequestLog> findAll(RequestLogCriteria criteria, Pageable pageable);

    RequestLog findById(Long id);

}
