package com.outsera.goldenraspberryawards.api.springdoc;

import com.outsera.goldenraspberryawards.core.security.SecurityProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class SpringDocConfig {

//    static {
//        SpringDocUtils.getConfig().replaceWithClass(Links.class, LinksModelOpenApi.class);
//    }

    @Bean
    public OpenAPI graOpenAPI(SecurityProperties securityProperties) {

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("integratorScheme", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .description("Access with CLIENT_CREDENTIALS")
                                .flows(new OAuthFlows()
                                        .clientCredentials(new OAuthFlow()
                                                .tokenUrl(securityProperties.getSecurity()
                                                        .getAuthServerUrl() + "/oauth2/token")
                                                .scopes(new Scopes()
                                                        .addString("INTEGRATION",
                                                                "Integration scope")
                                                )
                                        )
                                )
                        )
                        .addSecuritySchemes("userScheme", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .description("Access with AUTHORIZATION_CODE")
                                .flows(new OAuthFlows()
                                        .authorizationCode(new OAuthFlow()
                                                .authorizationUrl(securityProperties.getSecurity()
                                                        .getAuthServerUrl() + "/oauth2/authorize")
                                                .tokenUrl(securityProperties.getSecurity()
                                                        .getAuthServerUrl() + "/oauth2/token")
                                                .scopes(new Scopes()
                                                        .addString("READ",
                                                                "Read scope")
                                                        .addString("WRITE",
                                                                "Write scope")
                                                )
                                                .refreshUrl(securityProperties.getSecurity()
                                                        .getAuthServerUrl() + "/oauth2/token")

                                        )
                                )
                                .name("oauth2")
                        )
                )
                .addServersItem(new Server()
                        .url(securityProperties.getSecurity()
                                .getAuthServerUrl())
                        .description("Default Server"))
                .addSecurityItem(new SecurityRequirement()
                        .addList("integratorScheme")
                        .addList("userScheme"))
                .info(apiInfo())
                .externalDocs(new ExternalDocumentation()
                        .description("github documentation")
                        .url("https://github.com/carlosbetiol/GRA_Outsera/tree/main"))
                ;
    }

    @Bean
    public OpenApiCustomizer customOpenApiCustomiserGlobalGetResponseMessages() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperationsMap().entrySet().stream()
                    .filter((httpMethodAndOperation) -> httpMethodAndOperation.getKey().equals(PathItem.HttpMethod.GET))
                    .forEach((httpMethodAndOperation) -> {
                        var operation = httpMethodAndOperation.getValue();
                        ApiResponses apiResponses = operation.getResponses();
                        ApiResponse response = new ApiResponse()
                                .description("The resource does not have a representation that can be accepted by the consumer")
                                .content(new Content()
                                        .addMediaType(APPLICATION_JSON_VALUE, new MediaType()));
                        apiResponses.addApiResponse("406", response);
                    })
            );
        };
    }

    @Bean
    public OpenApiCustomizer customOpenApiCustomiserGlobalPostPutResponseMessages() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperationsMap().entrySet().stream()
                    .filter((httpMethodAndOperation) -> httpMethodAndOperation.getKey().equals(PathItem.HttpMethod.POST)
                            || httpMethodAndOperation.getKey().equals(PathItem.HttpMethod.PUT))
                    .forEach((httpMethodAndOperation) -> {
                        var operation = httpMethodAndOperation.getValue();
                        MediaType sharedMediaType = new MediaType().schema(new Schema().$ref("Problem"));
                        Content sharedContent = new Content()
                                .addMediaType(APPLICATION_JSON_VALUE, sharedMediaType);
                        ApiResponses apiResponses = operation.getResponses();
                        ApiResponse response;
                        response = new ApiResponse()
                                .description("Invalid request")
                                .content(sharedContent);
                        apiResponses.addApiResponse("400", response);
                        response = new ApiResponse()
                                .description("Invalid request body format")
                                .content(sharedContent);
                        apiResponses.addApiResponse("415", response);
                        response = new ApiResponse()
                                .description("The resource does not have a representation that can be accepted by the consumer")
                                .content(new Content()
                                        .addMediaType(APPLICATION_JSON_VALUE, new MediaType()));
                        apiResponses.addApiResponse("406", response);
                    })
            );
        };
    }

    @Bean
    public OpenApiCustomizer customOpenApiCustomiserGlobalDeleteResponseMessages() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperationsMap().entrySet().stream()
                    .filter((httpMethodAndOperation) -> httpMethodAndOperation.getKey().equals(PathItem.HttpMethod.DELETE))
                    .forEach((httpMethodAndOperation) -> {
                        var operation = httpMethodAndOperation.getValue();
                        MediaType sharedMediaType = new MediaType().schema(new Schema().$ref("Problem"));
                        Content sharedContent = new Content()
                                .addMediaType(APPLICATION_JSON_VALUE, sharedMediaType);
                        ApiResponses apiResponses = operation.getResponses();
                        ApiResponse response;
                        response = new ApiResponse()
                                .description("Invalid request")
                                .content(sharedContent);
                        apiResponses.addApiResponse("400", response);
                    })
            );
        };
    }

    @Bean
    public OpenApiCustomizer customOpenApiCustomiserGlobalResponseMessages() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                MediaType sharedMediaType = new MediaType().schema(new Schema().$ref("Problem"));
                Content sharedContent = new Content()
                        .addMediaType(APPLICATION_JSON_VALUE, sharedMediaType);
                ApiResponses apiResponses = operation.getResponses();
                ApiResponse response = new ApiResponse()
                        .description("Server internal error")
                        .content(sharedContent);
                apiResponses.addApiResponse("500", response);
            }));
        };
    }


    private Info apiInfo() {
        return new Info().title("Golden Raspberry Awards API")
                .description("Annual Golden Raspberry Awards API")
                .version("v1")
                .termsOfService("https://to-do/terms-of-service")
                .contact(new Contact().name("Outsera").url("outsera.com").email("carlos@betiol.dev"))
                .license(new License().name("Apache 2.0").url("http://to-do/license"));
    }


}
