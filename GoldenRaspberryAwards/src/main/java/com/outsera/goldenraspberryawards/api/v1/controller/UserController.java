package com.outsera.goldenraspberryawards.api.v1.controller;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.UserCriteria;
import com.outsera.goldenraspberryawards.api.v1.model.response.UserResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.openapi.UserControllerOpenApi;
import com.outsera.goldenraspberryawards.core.security.CheckSecurity;
import com.outsera.goldenraspberryawards.domain.model.User;
import com.outsera.goldenraspberryawards.domain.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.outsera.goldenraspberryawards.api.v1.mapper.UserMapper.USER_MAPPER;

@RestController
@RequestMapping("/v1/users")
public class UserController implements UserControllerOpenApi {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CheckSecurity.Users.CanView
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PagedModel<UserResponseDTO> getAllResources(@ModelAttribute UserCriteria criteria, Pageable pageable) {
        Page<User> page = userService.findAll(criteria, pageable);
        return new PagedModel<>(new PageImpl<>(USER_MAPPER.toResponseCollectionModel(page.getContent()), pageable, page.getTotalElements()));
    }

    @CheckSecurity.Users.CanView
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public UserResponseDTO getResource(@PathVariable(name = "id") Long id) {
        return USER_MAPPER.toResponseModel(userService.findById(id));
    }

}
