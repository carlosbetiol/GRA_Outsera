package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.api.contextual.ContextualRequest;
import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.ProducerCriteria;
import com.outsera.goldenraspberryawards.domain.exception.EntityInUseException;
import com.outsera.goldenraspberryawards.domain.exception.ProducerNotFoundException;
import com.outsera.goldenraspberryawards.domain.model.Producer;
import com.outsera.goldenraspberryawards.domain.model.SysEntity;
import com.outsera.goldenraspberryawards.domain.model.virtual.AwardBorder;
import com.outsera.goldenraspberryawards.domain.model.virtual.AwardInterval;
import com.outsera.goldenraspberryawards.domain.repository.ProducerRepository;
import com.outsera.goldenraspberryawards.domain.specification.ProducerSpecification;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    @Transactional
    public Producer saveLogLess(Producer producer) {
        return producerRepository.save(producer);
    }

    @Override
    public Producer findById(Long id) {
        return producerRepository.findById(id)
                .orElseThrow(() -> new ProducerNotFoundException(id));
    }

    @Override
    public Optional<Producer> findByName(String name) {
        return producerRepository.findByName(name);
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

        SysEntity entity = findById(id);
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

    @Override
    public AwardBorder minMaxProducerWinners() {

        List<AwardInterval> awardIntervals = populateAwardIntervals(producerRepository.findProducersAwardedYears());

        if( !awardIntervals.isEmpty() ) {

            awardIntervals.sort(Comparator.comparingInt(AwardInterval::getInterval));

            Integer first = awardIntervals.get(0).getInterval();
            List<AwardInterval> min = awardIntervals.stream().filter(a -> a.getInterval().equals(first)).toList();
            Integer last = awardIntervals.get(awardIntervals.size()-1).getInterval();
            List<AwardInterval> max = awardIntervals.stream().filter(a -> a.getInterval().equals(last)).toList();

            return new AwardBorder()
                    .setMin(min)
                    .setMax(max);

        }

        return new AwardBorder()
                .setMin(new ArrayList<>())
                .setMax(new ArrayList<>());

    }

    private List<AwardInterval> populateAwardIntervals(Map<String, List<Integer>> producersAwardedYears) {

        List<AwardInterval> intervals = new ArrayList<>();

        producersAwardedYears.forEach((producerName, years) -> {
            Integer[] year = {0};
            years.forEach(y -> {
                if (!year[0].equals(0)) {
                    intervals.add(new AwardInterval()
                            .setInterval(y - year[0])
                            .setPreviousWin(year[0])
                            .setFollowingWin(y)
                            .setProducer(producerName));
                }
                year[0] = y;
            });
        });

        return intervals;

    }


}
