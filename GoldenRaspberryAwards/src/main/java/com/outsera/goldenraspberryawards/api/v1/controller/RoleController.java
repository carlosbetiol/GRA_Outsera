package com.outsera.goldenraspberryawards.api.v1.controller;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.RoleCriteria;
import com.outsera.goldenraspberryawards.api.v1.model.response.RoleResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.openapi.RoleControllerOpenApi;
import com.outsera.goldenraspberryawards.core.security.CheckSecurity;
import com.outsera.goldenraspberryawards.domain.model.Role;
import com.outsera.goldenraspberryawards.domain.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.outsera.goldenraspberryawards.api.v1.mapper.RoleMapper.ROLE_MAPPER;

@RestController
@RequestMapping("/v1/roles")
public class RoleController implements RoleControllerOpenApi {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @CheckSecurity.Roles.CanView
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PagedModel<RoleResponseDTO> getAllResources(@ModelAttribute RoleCriteria criteria, Pageable pageable) {
        Page<Role> page = roleService.findAll(criteria, pageable);
        return new PagedModel<>(new PageImpl<>(ROLE_MAPPER.toResponseCollectionModel(page.getContent()), pageable, page.getTotalElements()));
    }

    @CheckSecurity.Roles.CanView
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public RoleResponseDTO getResource(@PathVariable(name = "id") Long id) {
        return ROLE_MAPPER.toResponseModel(roleService.findById(id));
    }

}
