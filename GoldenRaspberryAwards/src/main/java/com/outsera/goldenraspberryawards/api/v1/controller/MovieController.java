package com.outsera.goldenraspberryawards.api.v1.controller;

import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.MovieCriteria;
import com.outsera.goldenraspberryawards.api.v1.model.request.MovieRequestDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.MovieResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.openapi.MovieControllerOpenApi;
import com.outsera.goldenraspberryawards.core.security.CheckSecurity;
import com.outsera.goldenraspberryawards.domain.exception.ReferencedEntityNotFoundException;
import com.outsera.goldenraspberryawards.domain.model.Movie;
import com.outsera.goldenraspberryawards.domain.service.MovieService;
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

import static com.outsera.goldenraspberryawards.api.v1.mapper.MovieMapper.MOVIE_MAPPER;

@RestController
@RequestMapping("/v1/movies")
public class MovieController implements MovieControllerOpenApi {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @CheckSecurity.Movies.CanView
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public PagedModel<MovieResponseDTO> getAllResources(@ModelAttribute MovieCriteria criteria, Pageable pageable) {
        Page<Movie> page = movieService.findAll(criteria, pageable);
        return new PagedModel<>(new PageImpl<>(MOVIE_MAPPER.toResponseCollectionModel(page.getContent()), pageable, page.getTotalElements()));
    }

    @CheckSecurity.Movies.CanView
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public List<MovieResponseDTO> getAllResources() {
        return MOVIE_MAPPER.toMinimalResponseCollectionModel(movieService.findAll());
    }

    @CheckSecurity.Movies.CanView
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public MovieResponseDTO getResource(@PathVariable(name = "id") Long id) {
        return MOVIE_MAPPER.toResponseModel(movieService.findById(id));
    }

    @CheckSecurity.Movies.CanInsert
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public MovieResponseDTO createResource(@RequestBody @Valid MovieRequestDTO dto) {
        try {
            return Optional.of(movieService.save(MOVIE_MAPPER.toEntity(dto)))
                    .map(MOVIE_MAPPER::toResponseModel).get();
        } catch (DataIntegrityViolationException e) {
            throw new ReferencedEntityNotFoundException( "producerId/studioId");
        }
    }

    @CheckSecurity.Movies.CanEdit
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public MovieResponseDTO updateResource(@PathVariable Long id, @RequestBody @Valid MovieRequestDTO dto) {
        Movie entity = movieService.findById(id);
        try {
            return Optional.of(movieService.update(MOVIE_MAPPER.mergeEntity(dto, entity)))
                    .map(MOVIE_MAPPER::toResponseModel).get();
        } catch (DataIntegrityViolationException e) {
            throw new ReferencedEntityNotFoundException( "producerId/studioId");
        }
    }

    @CheckSecurity.Movies.CanDelete
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<Void> removeResource(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
