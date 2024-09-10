package com.outsera.goldenraspberryawards.domain.service;


import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.PermissionCriteria;
import com.outsera.goldenraspberryawards.domain.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PermissionService {

    Permission findById(Long id);

    Permission findByIdentifier(String identifier);

    Page<Permission> findAll(PermissionCriteria criteria, Pageable pageable);

    List<Permission> findAll();

}
