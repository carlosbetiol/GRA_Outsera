package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.contextual.ContextualRequest;
import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.StudioCriteria;
import com.outsera.goldenraspberryawards.domain.exception.EntityInUseException;
import com.outsera.goldenraspberryawards.domain.exception.StudioNotFoundException;
import com.outsera.goldenraspberryawards.domain.model.Studio;
import com.outsera.goldenraspberryawards.domain.model.SysEntity;
import com.outsera.goldenraspberryawards.domain.repository.StudioRepository;
import com.outsera.goldenraspberryawards.domain.specification.StudioSpecification;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudioServiceImpl extends AbstractService implements StudioService {

    private final StudioRepository studioRepository;

    public StudioServiceImpl(ContextualRequest contextualRequest, StudioRepository studioRepository) {
        super(contextualRequest);
        this.studioRepository = studioRepository;
    }

    @Override
    @Transactional
    public Studio save(Studio studio) {
        return (Studio) super.registerLog(studioRepository.save(studio));
    }

    @Override
    @Transactional
    public Studio saveLogLess(Studio studio) {
        return studioRepository.save(studio);
    }

    @Override
    public Studio findById(Long id) {
        return studioRepository.findById(id)
                .orElseThrow(() -> new StudioNotFoundException(id));
    }

    @Override
    public Optional<Studio> findByName(String name) {
        return studioRepository.findByName(name);
    }

    @Override
    public Page<Studio> findAll(StudioCriteria criteria, Pageable pageable) {
        Specification<Studio> spec = StudioSpecification.byCriteria( (StudioCriteria) criteria.parseSearch());
        return studioRepository.findAll(spec, pageable);
    }

    @Override
    public List<Studio> findAll() {
        return studioRepository.findAllByOrderByNameAsc();
    }

    @Override
    @Transactional
    public void delete(Long id) {

        SysEntity entity = studioRepository.findById(id).orElse(null);
        try {
            studioRepository.deleteById(id);
            studioRepository.flush();
            super.registerLog(entity);
        } catch (EmptyResultDataAccessException e) {
            throw new StudioNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(id.toString());
        }

    }


}
