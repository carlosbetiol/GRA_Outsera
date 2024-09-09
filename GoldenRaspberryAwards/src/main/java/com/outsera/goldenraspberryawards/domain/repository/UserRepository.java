package com.outsera.goldenraspberryawards.domain.repository;

import com.outsera.goldenraspberryawards.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.roles ur LEFT JOIN FETCH ur.rolePermissions urp LEFT JOIN FETCH urp.permission WHERE u.email = :email")
    Optional<User> findByEmailToAuthentication(String email);

}
