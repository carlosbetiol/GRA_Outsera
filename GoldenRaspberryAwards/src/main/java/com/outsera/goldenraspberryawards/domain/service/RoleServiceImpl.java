package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.RoleCriteria;
import com.outsera.goldenraspberryawards.domain.exception.RoleNotFoundException;
import com.outsera.goldenraspberryawards.domain.model.Role;
import com.outsera.goldenraspberryawards.domain.repository.RoleRepository;
import com.outsera.goldenraspberryawards.domain.specification.RoleSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(id));
    }

    @Override
    public Role findByIdentifier(String identifier) {
        return roleRepository.findByIdentifier(identifier).orElseThrow(() -> new RoleNotFoundException(identifier));
    }

    @Override
    public Page<Role> findAll(RoleCriteria criteria, Pageable pageable) {
        Specification<Role> spec = RoleSpecification.byCriteria( (RoleCriteria) criteria.parseSearch());
        return roleRepository.findAll(spec, pageable);
    }

}
