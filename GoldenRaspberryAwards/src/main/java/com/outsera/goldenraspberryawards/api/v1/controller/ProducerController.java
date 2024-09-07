package com.outsera.goldenraspberryawards.api.v1.controller;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.ProducerCriteria;
import com.outsera.goldenraspberryawards.api.v1.model.request.ProducerRequestDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.ProducerResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.openapi.ProducerControllerOpenApi;
import com.outsera.goldenraspberryawards.core.security.CheckSecurity;
import com.outsera.goldenraspberryawards.domain.model.Producer;
import com.outsera.goldenraspberryawards.domain.service.ProducerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.outsera.goldenraspberryawards.api.v1.mapper.ProducerMapper.PRODUCER_MAPPER;

@RestController
@RequestMapping("/v1/producers")
public class ProducerController implements ProducerControllerOpenApi {

    private final ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @CheckSecurity.Producers.CanView
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PagedModel<ProducerResponseDTO> getAllResources(@ModelAttribute ProducerCriteria criteria, Pageable pageable) {
        Page<Producer> page = producerService.findAll(criteria, pageable);
        return new PagedModel<>(new PageImpl<>(PRODUCER_MAPPER.toResponseCollectionModel(page.getContent()), pageable, page.getTotalElements()));
    }

    @CheckSecurity.Producers.CanView
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public List<ProducerResponseDTO> getAllResources() {
        return PRODUCER_MAPPER.toResponseCollectionModel(producerService.findAll());
    }

    @CheckSecurity.Producers.CanView
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public ProducerResponseDTO getResource(@PathVariable(name = "id") Long id) {
        return PRODUCER_MAPPER.toResponseModel(producerService.findById(id));
    }

    @CheckSecurity.Producers.CanInsert
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public ProducerResponseDTO createResource(@RequestBody @Valid ProducerRequestDTO dto) {
        return Optional.of(producerService.save(PRODUCER_MAPPER.toEntity(dto)))
                .map(PRODUCER_MAPPER::toResponseModel).get();
    }

    @CheckSecurity.Producers.CanEdit
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public ProducerResponseDTO updateResource(@PathVariable Long id, @RequestBody @Valid ProducerRequestDTO dto) {
        Producer entity = producerService.findById(id);
        return Optional.of(producerService.save(PRODUCER_MAPPER.mergeEntity(dto, entity)))
                .map(PRODUCER_MAPPER::toResponseModel).get();
    }

    @CheckSecurity.Producers.CanEdit
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> removeResource(@PathVariable Long id) {
        producerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
