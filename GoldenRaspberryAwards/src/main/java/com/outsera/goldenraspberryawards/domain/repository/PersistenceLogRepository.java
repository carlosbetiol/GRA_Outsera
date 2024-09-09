package com.outsera.goldenraspberryawards.domain.repository;

import com.outsera.goldenraspberryawards.domain.model.PersistenceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersistenceLogRepository extends JpaRepository<PersistenceLog, Long>, PersistenceLogRepositoryCustom {

}
