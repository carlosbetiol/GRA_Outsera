package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.PermissionCriteria;
import com.outsera.goldenraspberryawards.domain.exception.PermissionNotFoundException;
import com.outsera.goldenraspberryawards.domain.model.Permission;
import com.outsera.goldenraspberryawards.domain.repository.PermissionRepository;
import com.outsera.goldenraspberryawards.domain.specification.PermissionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission findById(Long id) {
        return permissionRepository.findById(id).orElseThrow(() -> new PermissionNotFoundException(id));
    }

    @Override
    public Permission findByIdentifier(String identifier) {
        return permissionRepository.findByIdentifier(identifier).orElseThrow(() -> new PermissionNotFoundException(identifier));
    }

    @Override
    public Page<Permission> findAll(PermissionCriteria criteria, Pageable pageable) {
        Specification<Permission> spec = PermissionSpecification.byCriteria( (PermissionCriteria) criteria.parseSearch());
        return permissionRepository.findAll(spec, pageable);
    }

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

}
