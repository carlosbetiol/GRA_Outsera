package com.outsera.goldenraspberryawards.api.v1.controller;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.RequestLogCriteria;
import com.outsera.goldenraspberryawards.api.v1.model.response.RequestLogResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.openapi.RequestLogControllerOpenApi;
import com.outsera.goldenraspberryawards.core.security.CheckSecurity;
import com.outsera.goldenraspberryawards.domain.model.RequestLog;
import com.outsera.goldenraspberryawards.domain.service.RequestLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.outsera.goldenraspberryawards.api.v1.mapper.RequestLogMapper.REQUEST_LOG_MAPPER;

@RestController
@RequestMapping("/v1/request-logs")
public class RequestLogController implements RequestLogControllerOpenApi {

    private final RequestLogService requestLogService;

    public RequestLogController(RequestLogService requestLogService) {
        this.requestLogService = requestLogService;
    }

    @CheckSecurity.Logs.CanView
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PagedModel<RequestLogResponseDTO> getAllResources(@ModelAttribute RequestLogCriteria criteria, Pageable pageable) {
        Page<RequestLog> page = requestLogService.findAll(criteria, pageable);
        return new PagedModel<>(new PageImpl<>(REQUEST_LOG_MAPPER.toResponseCollectionModel(page.getContent()), pageable, page.getTotalElements()));
    }

    @CheckSecurity.Logs.CanView
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public RequestLogResponseDTO getResource(@PathVariable(name = "id") Long id) {
        return REQUEST_LOG_MAPPER.toResponseModel(requestLogService.findById(id));
    }

}
