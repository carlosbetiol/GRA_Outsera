package com.outsera.goldenraspberryawards.domain.repository;

import com.outsera.goldenraspberryawards.domain.model.RequestLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface RequestLogRepositoryCustom {

    Page<RequestLog> findAll(Specification<RequestLog> spec, Pageable pageable);

}
