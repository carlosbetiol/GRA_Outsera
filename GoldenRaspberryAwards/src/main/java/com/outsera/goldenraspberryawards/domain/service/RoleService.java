package com.outsera.goldenraspberryawards.domain.service;


import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.RoleCriteria;
import com.outsera.goldenraspberryawards.domain.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {

    Role findById(Long id);

    Role findByIdentifier(String identifier);

    Page<Role> findAll(RoleCriteria criteria, Pageable pageable);

}
