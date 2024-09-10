package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.contextual.ContextualRequest;
import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.MovieAwardCriteria;
import com.outsera.goldenraspberryawards.domain.exception.EntityInUseException;
import com.outsera.goldenraspberryawards.domain.exception.MovieAwardNotFoundException;
import com.outsera.goldenraspberryawards.domain.model.MovieAward;
import com.outsera.goldenraspberryawards.domain.model.SysEntity;
import com.outsera.goldenraspberryawards.domain.repository.MovieAwardRepository;
import com.outsera.goldenraspberryawards.domain.specification.MovieAwardSpecification;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieAwardServiceImpl extends AbstractService implements MovieAwardService {

    private final MovieAwardRepository movieAwardRepository;

    public MovieAwardServiceImpl(ContextualRequest contextualRequest, MovieAwardRepository movieAwardRepository) {
        super(contextualRequest);
        this.movieAwardRepository = movieAwardRepository;
    }

    @Override
    @Transactional
    public MovieAward save(MovieAward movieAward) {
        MovieAward mv = (MovieAward) super.registerLog(movieAwardRepository.save(movieAward));
        movieAwardRepository.detach(mv);
        return findById(mv.getId());
    }

    @Transactional
    @Override
    public MovieAward update(MovieAward movieAward) {
        return (MovieAward) super.registerLog(movieAwardRepository.save(movieAward));
    }

    @Override
    @Transactional
    public MovieAward saveLogLess(MovieAward movieAward) {
        return movieAwardRepository.save(movieAward);
    }

    @Override
    public MovieAward findById(Long id) {
        return movieAwardRepository.findById(id)
                .orElseThrow(() -> new MovieAwardNotFoundException(id));
    }

    @Override
    public Page<MovieAward> findAll(MovieAwardCriteria criteria, Pageable pageable) {
        Specification<MovieAward> spec = MovieAwardSpecification.byCriteria( (MovieAwardCriteria) criteria.parseSearch());
        return movieAwardRepository.findAll(spec, pageable);
    }

    @Override
    public List<MovieAward> findAll() {
        return movieAwardRepository.findAllByOrderByAwardYearAsc();
    }

    @Override
    @Transactional
    public void delete(Long id) {

        SysEntity entity = findById(id);
        try {
            movieAwardRepository.deleteById(id);
            movieAwardRepository.flush();
            super.registerLog(entity);
        } catch (EmptyResultDataAccessException e) {
            throw new MovieAwardNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(id.toString());
        }

    }


}
