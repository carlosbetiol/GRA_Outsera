package com.outsera.goldenraspberryawards.domain.repository;

import com.outsera.goldenraspberryawards.domain.model.MovieAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieAwardRepository extends JpaRepository<MovieAward, Long>, MovieAwardRepositoryCustom {

    List<MovieAward> findAllByOrderByAwardYearAsc();

}
