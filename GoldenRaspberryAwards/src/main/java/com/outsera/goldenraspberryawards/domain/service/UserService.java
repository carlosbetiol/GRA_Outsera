package com.outsera.goldenraspberryawards.domain.service;


import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.UserCriteria;
import com.outsera.goldenraspberryawards.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User findById(Long id);

    Page<User> findAll(UserCriteria criteria, Pageable pageable);

    User findByEmail(String email);

    User findByEmailToAuthentication(String email);

}
