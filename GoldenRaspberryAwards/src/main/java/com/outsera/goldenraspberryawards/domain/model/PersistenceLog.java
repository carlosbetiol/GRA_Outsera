package com.outsera.goldenraspberryawards.domain.model;


import com.outsera.goldenraspberryawards.core.jackson.JsonObjectToJsonStringConverter;
import com.outsera.goldenraspberryawards.domain.enums.SyslogActionEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.time.OffsetDateTime;

@Entity
@Table(name = "persistence_log")
@Getter
@Setter
@Accessors(chain = true)
public class PersistenceLog extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @Column(name="entity_name", nullable = false)
    private String entityName;

    @Column(name="entity_id", nullable = false)
    private Long entityId;

    @Column(name="json_content", columnDefinition = "json", nullable = false)
    @Convert(converter = JsonObjectToJsonStringConverter.class)
    private JSONObject jsonContent;

    @Column(name="request_uuid", nullable = true)
    private String requestUUID;

    @Column(name="created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private SyslogActionEnum action;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersistenceLog persistenceLog = (PersistenceLog) o;

        return getId().equals(persistenceLog.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
