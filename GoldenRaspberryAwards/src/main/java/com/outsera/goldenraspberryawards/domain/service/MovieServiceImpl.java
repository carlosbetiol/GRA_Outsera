package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.contextual.ContextualRequest;
import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.MovieCriteria;
import com.outsera.goldenraspberryawards.domain.exception.EntityInUseException;
import com.outsera.goldenraspberryawards.domain.exception.MovieNotFoundException;
import com.outsera.goldenraspberryawards.domain.model.Movie;
import com.outsera.goldenraspberryawards.domain.model.SysEntity;
import com.outsera.goldenraspberryawards.domain.repository.MovieRepository;
import com.outsera.goldenraspberryawards.domain.specification.MovieSpecification;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieServiceImpl extends AbstractService implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(ContextualRequest contextualRequest, MovieRepository movieRepository) {
        super(contextualRequest);
        this.movieRepository = movieRepository;
    }

    @Override
    @Transactional
    public Movie save(Movie movie) {
        return (Movie) super.registerLog(movieRepository.save(movie));
    }

    @Override
    public Movie findById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }

    @Override
    public Page<Movie> findAll(MovieCriteria criteria, Pageable pageable) {
        Specification<Movie> spec = MovieSpecification.byCriteria( (MovieCriteria) criteria.parseSearch());
        return movieRepository.findAll(spec, pageable);
    }

    @Override
    public List<Movie> findAll() {
        return movieRepository.findAllByOrderByNameAsc();
    }

    @Override
    @Transactional
    public void delete(Long id) {

        SysEntity entity = movieRepository.findById(id).orElse(null);
        try {
            movieRepository.deleteById(id);
            movieRepository.flush();
            super.registerLog(entity);
        } catch (EmptyResultDataAccessException e) {
            throw new MovieNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(id.toString());
        }

    }


}
