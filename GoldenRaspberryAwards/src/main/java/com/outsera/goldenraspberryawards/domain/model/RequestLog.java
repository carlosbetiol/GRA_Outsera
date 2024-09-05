package com.outsera.goldenraspberryawards.domain.model;

import com.outsera.goldenraspberryawards.core.jackson.JsonObjectToJsonStringConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.time.OffsetDateTime;

@Entity
@Table(name = "request_log")
@Getter
@Setter
@Accessors(chain = true)
public class RequestLog extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="request_uuid", nullable = false)
    private String requestUUID;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name="method", nullable = false)
    private String method;

    @Column(name="api_url", nullable = false)
    private String apiUrl;

    @Column(name="request_parameters", nullable = true)
    private String requestParameters;

    @Column(name="request_headers", columnDefinition = "json", nullable = true)
    @Convert(converter = JsonObjectToJsonStringConverter.class)
    private JSONObject requestHeaders;

    @Column(name="request_dto", nullable = true)
    private String requestDto;

    @Column(name="request_content", columnDefinition = "json", nullable = true)
    @Convert(converter = JsonObjectToJsonStringConverter.class)
    private JSONObject requestContent;

    @Column(name="request_time", nullable = false)
    private OffsetDateTime requestTime;

    @Column(name="response_status", nullable = true)
    private Integer responseStatus;

    @Column(name="response_headers", columnDefinition = "json", nullable = true)
    @Convert(converter = JsonObjectToJsonStringConverter.class)
    private JSONObject responseHeaders;

    @Column(name="response_dto", nullable = true)
    private String responseDto;

    @Column(name="response_content", columnDefinition = "json", nullable = true)
    @Convert(converter = JsonObjectToJsonStringConverter.class)
    private JSONObject responseContent;

    @Column(name="response_time", nullable = false)
    private OffsetDateTime responseTime;

    @Column(name="created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestLog that = (RequestLog) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
