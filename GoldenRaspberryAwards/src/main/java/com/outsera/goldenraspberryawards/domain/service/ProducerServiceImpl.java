package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.contextual.ContextualRequest;
import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.ProducerCriteria;
import com.outsera.goldenraspberryawards.domain.exception.EntityInUseException;
import com.outsera.goldenraspberryawards.domain.exception.ProducerNotFoundException;
import com.outsera.goldenraspberryawards.domain.model.Producer;
import com.outsera.goldenraspberryawards.domain.model.SysEntity;
import com.outsera.goldenraspberryawards.domain.repository.ProducerRepository;
import com.outsera.goldenraspberryawards.domain.specification.ProducerSpecification;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProducerServiceImpl extends AbstractService implements ProducerService {

    private final ProducerRepository producerRepository;

    public ProducerServiceImpl(ContextualRequest contextualRequest, ProducerRepository producerRepository) {
        super(contextualRequest);
        this.producerRepository = producerRepository;
    }

    @Override
    @Transactional
    public Producer save(Producer producer) {
        return (Producer) super.registerLog(producerRepository.save(producer));
    }

    @Override
    public Producer findById(Long id) {
        return producerRepository.findById(id)
                .orElseThrow(() -> new ProducerNotFoundException(id));
    }

    @Override
    public Page<Producer> findAll(ProducerCriteria criteria, Pageable pageable) {
        Specification<Producer> spec = ProducerSpecification.byCriteria( (ProducerCriteria) criteria.parseSearch());
        return producerRepository.findAll(spec, pageable);
    }

    @Override
    public List<Producer> findAll() {
        return producerRepository.findAllByOrderByNameAsc();
    }

    @Override
    @Transactional
    public void delete(Long id) {

        SysEntity entity = producerRepository.findById(id).orElse(null);
        try {
            producerRepository.deleteById(id);
            producerRepository.flush();
            super.registerLog(entity);
        } catch (EmptyResultDataAccessException e) {
            throw new ProducerNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(id.toString());
        }

    }


}
