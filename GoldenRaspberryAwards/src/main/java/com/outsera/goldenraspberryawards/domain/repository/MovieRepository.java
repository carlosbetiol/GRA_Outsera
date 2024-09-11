package com.outsera.goldenraspberryawards.domain.repository;

import com.outsera.goldenraspberryawards.domain.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, MovieRepositoryCustom {

    @Query("from Movie m join fetch m.producers p join fetch m.studios s left join m.movieAwards ma order by m.name")
    List<Movie> findAllByOrderByNameAsc();

    Optional<Movie> findByName(String name);

}
