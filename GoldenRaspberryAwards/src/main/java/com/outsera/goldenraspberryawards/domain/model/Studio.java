package com.outsera.goldenraspberryawards.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "\"studio\"")
@Getter
@Setter
public class Studio extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @Column(name = "\"name\"", nullable = false)
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "studios")
    private Set<Movie> movies;

    @Column(name = "\"created_at\"", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "\"updated_at\"", nullable = false)
    private OffsetDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Studio studio = (Studio) o;
        return Objects.equals(id, studio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
