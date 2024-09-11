package com.outsera.goldenraspberryawards.domain.repository;

import com.outsera.goldenraspberryawards.domain.model.MovieAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieAwardRepository extends JpaRepository<MovieAward, Long>, MovieAwardRepositoryCustom {

    @Query("from MovieAward ma join fetch ma.movieWinner mw join fetch mw.producers p join fetch mw.studios s order by ma.awardYear")
    List<MovieAward> findAllByOrderByAwardYearAsc();

}
