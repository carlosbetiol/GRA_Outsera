package com.outsera.goldenraspberryawards.api.v1.controller;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.PersistenceLogCriteria;
import com.outsera.goldenraspberryawards.api.v1.model.response.PersistenceLogResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.openapi.PersistenceLogControllerOpenApi;
import com.outsera.goldenraspberryawards.core.security.CheckSecurity;
import com.outsera.goldenraspberryawards.domain.model.PersistenceLog;
import com.outsera.goldenraspberryawards.domain.service.PersistenceLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.outsera.goldenraspberryawards.api.v1.mapper.PersistenceLogMapper.PERSISTENCE_LOG_MAPPER;

@RestController
@RequestMapping("/v1/persistence-logs")
public class PersistenceLogController implements PersistenceLogControllerOpenApi {

    private final PersistenceLogService persistenceLogService;

    public PersistenceLogController(PersistenceLogService persistenceLogService) {
        this.persistenceLogService = persistenceLogService;
    }

    @CheckSecurity.Logs.CanView
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PagedModel<PersistenceLogResponseDTO> getAllResources(@ModelAttribute PersistenceLogCriteria criteria, Pageable pageable) {
        Page<PersistenceLog> page = persistenceLogService.findAll(criteria, pageable);
        return new PagedModel<>(new PageImpl<>(PERSISTENCE_LOG_MAPPER.toResponseCollectionModel(page.getContent()), pageable, page.getTotalElements()));
    }

    @CheckSecurity.Logs.CanView
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PersistenceLogResponseDTO getResource(@PathVariable(name = "id") Long id) {
        return PERSISTENCE_LOG_MAPPER.toResponseModel(persistenceLogService.findById(id));
    }

}
