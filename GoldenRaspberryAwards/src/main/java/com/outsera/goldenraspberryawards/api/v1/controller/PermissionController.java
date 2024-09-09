package com.outsera.goldenraspberryawards.api.v1.controller;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.PermissionCriteria;
import com.outsera.goldenraspberryawards.api.v1.model.response.PermissionResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.openapi.PermissionControllerOpenApi;
import com.outsera.goldenraspberryawards.core.security.CheckSecurity;
import com.outsera.goldenraspberryawards.domain.model.Permission;
import com.outsera.goldenraspberryawards.domain.service.PermissionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.outsera.goldenraspberryawards.api.v1.mapper.PermissionMapper.PERMISSION_MAPPER;

@RestController
@RequestMapping("/v1/permissions")
public class PermissionController implements PermissionControllerOpenApi {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @CheckSecurity.Permissions.CanView
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PagedModel<PermissionResponseDTO> getAllResources(@ModelAttribute PermissionCriteria criteria, Pageable pageable) {
        Page<Permission> page = permissionService.findAll(criteria, pageable);
        return new PagedModel<>(new PageImpl<>(PERMISSION_MAPPER.toResponseCollectionModel(page.getContent()), pageable, page.getTotalElements()));
    }

    @CheckSecurity.Permissions.CanView
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PermissionResponseDTO getResource(@PathVariable(name = "id") Long id) {
        return PERMISSION_MAPPER.toResponseModel(permissionService.findById(id));
    }

}
