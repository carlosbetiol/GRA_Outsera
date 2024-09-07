package com.outsera.goldenraspberryawards.domain.repository;

import com.outsera.goldenraspberryawards.domain.model.Producer;
import com.outsera.goldenraspberryawards.domain.specification.ProducerSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {

    Page<Producer> findAll(Specification<Producer> spec, Pageable pageable);

    List<Producer> findAllByOrderByNameAsc();
}
