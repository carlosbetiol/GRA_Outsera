package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.UserCriteria;
import com.outsera.goldenraspberryawards.domain.exception.UserNotFoundException;
import com.outsera.goldenraspberryawards.domain.model.User;
import com.outsera.goldenraspberryawards.domain.repository.UserRepository;
import com.outsera.goldenraspberryawards.domain.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public Page<User> findAll(UserCriteria criteria, Pageable pageable) {
        Specification<User> spec = UserSpecification.byCriteria( (UserCriteria) criteria.parseSearch());
        return userRepository.findAll(spec, pageable);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public User findByEmailToAuthentication(String email) {
        return userRepository.findByEmailToAuthentication(email).orElseThrow(() -> new UserNotFoundException(email));
    }

}
