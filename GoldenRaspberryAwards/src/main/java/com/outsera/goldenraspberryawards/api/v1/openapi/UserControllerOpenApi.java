package com.outsera.goldenraspberryawards.api.v1.openapi;

import com.outsera.goldenraspberryawards.api.exceptionhandler.Problem;
import com.outsera.goldenraspberryawards.api.v1.model.criteriafilter.UserCriteria;
import com.outsera.goldenraspberryawards.api.v1.model.response.UserResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.openapi.model.PagedUserResponseDTO;
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

@Tag(name = "Users", description = "Users management")
public interface UserControllerOpenApi {

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
                            schema = @Schema(implementation = PagedUserResponseDTO.class)))
    })
    public PagedModel<UserResponseDTO> getAllResources(
            UserCriteria criteria,
            @ParameterObject
            @PageableDefault(size = 20, page = 0)
            Pageable pageable);


    @Operation(summary = "Find resource by id", security = @SecurityRequirement(name="userScheme"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Problem.class))})
    })
    public UserResponseDTO getResource(@Parameter(description = "Resource id") Long id);

}
