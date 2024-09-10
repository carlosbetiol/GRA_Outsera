package com.outsera.goldenraspberryawards.api.v1.openapi;

import com.outsera.goldenraspberryawards.api.exceptionhandler.Problem;
import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.MovieCriteria;
import com.outsera.goldenraspberryawards.api.v1.model.request.MovieRequestDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.MovieResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.ProducerResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.openapi.model.PagedMovieResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Movies", description = "Movies management")
public interface MovieControllerOpenApi {

    @Operation(summary = "List paged resources", security = @SecurityRequirement(name="userScheme"))
    @PageableAsQueryParam
    @Parameters({
            @Parameter(required = false, in = ParameterIn.QUERY, description = "Page number (start at 0)",
                    name = "page", example = "1"),
            @Parameter(required = false, in = ParameterIn.QUERY, description = "Page elements count",
                    name = "size", example = "10"),
            @Parameter(required = false, in = ParameterIn.QUERY, description = "Order by property name. User format property[,asc|desc]. ",
                    name = "sort", example = "id,desc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paged list of resources",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagedMovieResponseDTO.class)))
    })
    public PagedModel<MovieResponseDTO> getAllResources(
            MovieCriteria criteria,
            @ParameterObject
            @PageableDefault(size = 20, page = 0)
            Pageable pageable);


    @Operation(summary = "List all resources", security = @SecurityRequirement(name = "userScheme"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resources list (empty if no resources found)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "array", implementation = MovieResponseDTO.class)))
    })
    public List<MovieResponseDTO>  getAllResources();

    @Operation(summary = "Find resource by id", security = @SecurityRequirement(name="userScheme"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MovieResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))})
    })
    public MovieResponseDTO getResource(@Parameter(description = "Resource id") Long id);

    @Operation(summary = "Add resource", security = @SecurityRequirement(name="userScheme"))
    @ApiResponse(responseCode = "201", description = "Resource successfully added", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = MovieResponseDTO.class))
    })
    public MovieResponseDTO createResource(MovieRequestDTO dto);

    @Operation(summary = "Update resource", security = @SecurityRequirement(name="userScheme"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource successfully updated", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProducerResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))})
    })
    public MovieResponseDTO updateResource(@Parameter(description = "Resource id") Long id,
                                           MovieRequestDTO dto);

    @Operation(summary = "Remove a resource", security = @SecurityRequirement(name="userScheme"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resource successfully removed"),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))})
    })
    public ResponseEntity<Void> removeResource(@Parameter(description = "Resource Id") Long id);

}
