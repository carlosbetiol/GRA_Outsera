package com.outsera.goldenraspberryawards.domain.repository;

import com.outsera.goldenraspberryawards.domain.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface RoleRepositoryCustom {

    Page<Role> findAll(Specification<Role> spec, Pageable pageable);

}
