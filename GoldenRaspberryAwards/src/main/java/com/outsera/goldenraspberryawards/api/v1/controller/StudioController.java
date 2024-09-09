package com.outsera.goldenraspberryawards.api.v1.controller;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.StudioCriteria;
import com.outsera.goldenraspberryawards.api.v1.model.request.StudioRequestDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.StudioResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.openapi.StudioControllerOpenApi;
import com.outsera.goldenraspberryawards.core.security.CheckSecurity;
import com.outsera.goldenraspberryawards.domain.model.Studio;
import com.outsera.goldenraspberryawards.domain.service.StudioService;
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

import static com.outsera.goldenraspberryawards.api.v1.mapper.StudioMapper.STUDIO_MAPPER;

@RestController
@RequestMapping("/v1/studios")
public class StudioController implements StudioControllerOpenApi {

    private final StudioService studioService;

    public StudioController(StudioService studioService) {
        this.studioService = studioService;
    }

    @CheckSecurity.Studios.CanView
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PagedModel<StudioResponseDTO> getAllResources(@ModelAttribute StudioCriteria criteria, Pageable pageable) {
        Page<Studio> page = studioService.findAll(criteria, pageable);
        return new PagedModel<>(new PageImpl<>(STUDIO_MAPPER.toResponseCollectionModel(page.getContent()), pageable, page.getTotalElements()));
    }

    @CheckSecurity.Studios.CanView
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public List<StudioResponseDTO> getAllResources() {
        return STUDIO_MAPPER.toResponseCollectionModel(studioService.findAll());
    }

    @CheckSecurity.Studios.CanView
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public StudioResponseDTO getResource(@PathVariable(name = "id") Long id) {
        return STUDIO_MAPPER.toResponseModel(studioService.findById(id));
    }

    @CheckSecurity.Studios.CanInsert
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public StudioResponseDTO createResource(@RequestBody @Valid StudioRequestDTO dto) {
        return Optional.of(studioService.save(STUDIO_MAPPER.toEntity(dto)))
                .map(STUDIO_MAPPER::toResponseModel).get();
    }

    @CheckSecurity.Studios.CanEdit
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public StudioResponseDTO updateResource(@PathVariable Long id, @RequestBody @Valid StudioRequestDTO dto) {
        Studio entity = studioService.findById(id);
        return Optional.of(studioService.save(STUDIO_MAPPER.mergeEntity(dto, entity)))
                .map(STUDIO_MAPPER::toResponseModel).get();
    }

    @CheckSecurity.Studios.CanDelete
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> removeResource(@PathVariable Long id) {
        studioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
