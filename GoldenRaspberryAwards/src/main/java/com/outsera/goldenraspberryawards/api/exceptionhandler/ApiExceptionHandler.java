package com.outsera.goldenraspberryawards.api.exceptionhandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.outsera.goldenraspberryawards.api.contextual.ContextualRequest;
import com.outsera.goldenraspberryawards.core.internationalization.MessageSystem;
import com.outsera.goldenraspberryawards.domain.exception.BusinessException;
import com.outsera.goldenraspberryawards.domain.exception.EntityInUseException;
import com.outsera.goldenraspberryawards.domain.exception.EntityNotFoundException;
import com.outsera.goldenraspberryawards.domain.exception.SystemInvalidParameterException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final ContextualRequest contextualRequest;
    public static final String GENERIC_ERROR_MESSAGE_TO_END_USER
            = MessageSystem.getInstance().getLocalizedMessage("exception.unexpectedInternalError");

    private final MessageSource messageSource;

    private final ObjectMapper objectMapper;

    public ApiExceptionHandler(ContextualRequest contextualRequest, MessageSource messageSource, ObjectMapper objectMapper) {
        this.contextualRequest = contextualRequest;
        this.messageSource = messageSource;
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                                      HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.status(status).headers(headers).build();
    }


    @ExceptionHandler(BindException.class)
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
                                                         WebRequest request) {

        return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
    }

    private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers,
                                                            HttpStatus status, WebRequest request, BindingResult bindingResult) {
        ProblemType problemType = ProblemType.INVALID_DATA;
        String detail = MessageSystem.getInstance().getLocalizedMessage("exception.invalidData");

        List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                    String name = objectError.getObjectName();

                    if (objectError instanceof FieldError) {
                        name = ((FieldError) objectError).getField();
                    }

                    return Problem.Object.builder()
                            .name(name)
                            .userMessage(message)
                            .build();
                })
                .collect(Collectors.toList());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .objects(problemObjects)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {

        return handleValidationExternal(ex, request, ex.getConstraintViolations());
    }

    private ResponseEntity<Object> handleValidationExternal(ConstraintViolationException ex, WebRequest request, Set<ConstraintViolation<?>> violations) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ProblemType problemType = ProblemType.INVALID_DATA;
        String detail = MessageSystem.getInstance().getLocalizedMessage("exception.invalidData");

        List<Problem.Object> problemObjects = violations.stream()
                .map(violation -> {
                    String path = violation.getPropertyPath().toString();
                    String message = violation.getMessage();

                    // Extract the field name or method parameter name from path
                    String name = path.substring(path.lastIndexOf('.') + 1);

                    return Problem.Object.builder()
                            .name(name)
                            .userMessage(message)
                            .build();
                })
                .collect(Collectors.toList());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .objects(problemObjects)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.SYSTEM_ERROR;
        String detail = GENERIC_ERROR_MESSAGE_TO_END_USER;

        log.error(ex.getMessage(), ex);

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers, HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.RESOURCE_NOT_FOUND;
        String detail = MessageSystem.getInstance().getLocalizedMessage("exception.resourceNotFound",
                new Object[]{ex.getRequestURL()});

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(GENERIC_ERROR_MESSAGE_TO_END_USER)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(TypeMismatchException.class)
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatus status, WebRequest request) {

        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch(
                    (MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.INVALID_PARAMETER;

        String detail = MessageSystem.getInstance().getLocalizedMessage("exception.invalidParameter",
                new Object[]{ex.getName(),ex.getValue(),ex.getRequiredType().getSimpleName()});

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(GENERIC_ERROR_MESSAGE_TO_END_USER)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
        }

        ProblemType problemType = ProblemType.INCOMPREHENSIBLE_MESSAGE;
        String detail = MessageSystem.getInstance().getLocalizedMessage("exception.invalidRequest");

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(GENERIC_ERROR_MESSAGE_TO_END_USER)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {

        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.INCOMPREHENSIBLE_MESSAGE;
        String detail = MessageSystem.getInstance().getLocalizedMessage("exception.propertyUnknown",
                new Object[]{path});

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(GENERIC_ERROR_MESSAGE_TO_END_USER)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
                                                       HttpHeaders headers, HttpStatus status, WebRequest request) {

        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.INCOMPREHENSIBLE_MESSAGE;
        String detail = MessageSystem.getInstance().getLocalizedMessage("exception.invalidValueType",
                new Object[]{path,ex.getValue(), ex.getTargetType().getSimpleName()});

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(GENERIC_ERROR_MESSAGE_TO_END_USER)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleEntityNotFound(AccessDeniedException ex, WebRequest request) {

        HttpStatus status = HttpStatus.FORBIDDEN;
        ProblemType problemType = ProblemType.ACCESS_DENIED;
        String detail = MessageSystem.getInstance().getLocalizedMessage("exception.accessDenied");

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .userMessage(MessageSystem.getInstance().getLocalizedMessage("exception.noPermission"))
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(SystemInvalidParameterException.class)
    public ResponseEntity<?> handleSystemInvalidParameter(EntityNotFoundException ex,
                                                          WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.INVALID_DATA;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex,
                                                  WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.RESOURCE_NOT_FOUND;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntityInUseException.class)
    public ResponseEntity<?> handleEntityInUse(EntityInUseException ex, WebRequest request) {

        HttpStatus status = HttpStatus.LOCKED;
        ProblemType problemType = ProblemType.ENTITY_IN_USE;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusiness(BusinessException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.BUSINESS_ERROR;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {

        if (body == null) {
            body = Problem.builder()
                    .timestamp(OffsetDateTime.now())
                    .title(status.getReasonPhrase())
                    .status(status.value())
                    .userMessage(GENERIC_ERROR_MESSAGE_TO_END_USER)
                    .build();
        } else if (body instanceof String) {
            body = Problem.builder()
                    .timestamp(OffsetDateTime.now())
                    .title((String) body)
                    .status(status.value())
                    .userMessage(GENERIC_ERROR_MESSAGE_TO_END_USER)
                    .build();
        }

        Object finalBody = body;
        contextualRequest.getRequestLog().ifPresent(requestLog -> {
            Map<String, Object> map = objectMapper.convertValue(finalBody, new TypeReference<Map<String, Object>>() {});
            requestLog.setResponseDto(finalBody.getClass().getSimpleName());
            requestLog.setResponseContent(new JSONObject(map));
            requestLog.setResponseHeaders(new JSONObject(headers.toSingleValueMap()));
            requestLog.setResponseStatus(status.value());
            requestLog.setResponseTime(OffsetDateTime.now());
        });

        contextualRequest.triggerRegisterRequestLog();

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status,
                                                        ProblemType problemType, String detail) {

        return Problem.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail);
    }

    private String joinPath(List<Reference> references) {
        return references.stream()
                .map(Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

}
