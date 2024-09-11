package com.outsera.goldenraspberryawards.domain.repository;

import com.outsera.goldenraspberryawards.domain.model.Studio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudioRepository extends JpaRepository<Studio, Long>, StudioRepositoryCustom {

    @Query("from Studio s left join fetch s.movies m left join fetch m.producers p left join m.movieAwards ma order by s.name")
    List<Studio> findAllByOrderByNameAsc();

    Optional<Studio> findByName(String name);

}
