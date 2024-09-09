package com.outsera.goldenraspberryawards.api.v1.controller;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.MovieAwardCriteria;
import com.outsera.goldenraspberryawards.api.v1.model.request.MovieAwardRequestDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.MovieAwardResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.openapi.MovieAwardControllerOpenApi;
import com.outsera.goldenraspberryawards.core.security.CheckSecurity;
import com.outsera.goldenraspberryawards.domain.exception.ReferencedEntityNotFoundException;
import com.outsera.goldenraspberryawards.domain.model.MovieAward;
import com.outsera.goldenraspberryawards.domain.service.MovieAwardService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
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

import static com.outsera.goldenraspberryawards.api.v1.mapper.MovieAwardMapper.MOVIE_AWARD_MAPPER;

@RestController
@RequestMapping("/v1/winners")
public class MovieAwardController implements MovieAwardControllerOpenApi {

    private final MovieAwardService movieAwardService;

    public MovieAwardController(MovieAwardService movieAwardService) {
        this.movieAwardService = movieAwardService;
    }

    @CheckSecurity.Awards.CanView
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PagedModel<MovieAwardResponseDTO> getAllResources(@ModelAttribute MovieAwardCriteria criteria, Pageable pageable) {
        Page<MovieAward> page = movieAwardService.findAll(criteria, pageable);
        return new PagedModel<>(new PageImpl<>(MOVIE_AWARD_MAPPER.toResponseCollectionModel(page.getContent()), pageable, page.getTotalElements()));
    }

    @CheckSecurity.Awards.CanView
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public List<MovieAwardResponseDTO> getAllResources() {
        return MOVIE_AWARD_MAPPER.toMinimalResponseCollectionModel(movieAwardService.findAll());
    }

    @CheckSecurity.Awards.CanView
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public MovieAwardResponseDTO getResource(@PathVariable(name = "id") Long id) {
        return MOVIE_AWARD_MAPPER.toResponseModel(movieAwardService.findById(id));
    }

    @CheckSecurity.Awards.CanInsert
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public MovieAwardResponseDTO createResource(@RequestBody @Valid MovieAwardRequestDTO dto) {
        try {
            return Optional.of(movieAwardService.save(MOVIE_AWARD_MAPPER.toEntity(dto)))
                    .map(MOVIE_AWARD_MAPPER::toResponseModel).get();
        } catch (DataIntegrityViolationException e) {
            throw new ReferencedEntityNotFoundException( "MovieId");
        }
    }

    @CheckSecurity.Awards.CanEdit
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public MovieAwardResponseDTO updateResource(@PathVariable Long id, @RequestBody @Valid MovieAwardRequestDTO dto) {
        MovieAward entity = movieAwardService.findById(id);
        try {
            return Optional.of(movieAwardService.update(MOVIE_AWARD_MAPPER.mergeEntity(dto, entity)))
                    .map(MOVIE_AWARD_MAPPER::toResponseModel).get();
        } catch (DataIntegrityViolationException e) {
            throw new ReferencedEntityNotFoundException( "movieId");
        }
    }

    @CheckSecurity.Awards.CanDelete
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> removeResource(@PathVariable Long id) {
        movieAwardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
