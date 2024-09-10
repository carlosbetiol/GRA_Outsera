package com.outsera.goldenraspberryawards.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "\"movie\"")
@Getter
@Setter
public class Movie extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @Column(name = "\"name\"", nullable = false)
    private String name;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "\"movie_studio\"",
            joinColumns = @JoinColumn(name = "\"movie_id\""),
            inverseJoinColumns = @JoinColumn(name = "\"studio_id\""),
            uniqueConstraints = @UniqueConstraint(columnNames = {"\"movie_id\"", "\"studio_id\""})
    )
    private Set<Studio> studios;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "\"movie_producer\"",
            joinColumns = @JoinColumn(name = "\"movie_id\""),
            inverseJoinColumns = @JoinColumn(name = "\"producer_id\""),
            uniqueConstraints = @UniqueConstraint(columnNames = {"\"movie_id\"", "\"producer_id\""})
    )
    private Set<Producer> producers;

    @JsonIgnore
    @OneToMany(mappedBy = "movieWinner")
    private Set<MovieAward> movieAwards;

    @Column(name = "\"created_at\"", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "\"updated_at\"", nullable = false)
    private OffsetDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
