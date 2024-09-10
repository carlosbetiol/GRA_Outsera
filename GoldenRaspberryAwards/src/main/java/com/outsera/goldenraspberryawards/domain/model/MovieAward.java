package com.outsera.goldenraspberryawards.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "\"movie_award\"")
@Getter
@Setter
public class MovieAward extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @Column(name = "\"award_year\"", nullable = false)
    private Integer awardYear;

    @ManyToOne
    @JoinColumn(name = "\"movie_id\"")
    private Movie movieWinner;

    @Column(name = "\"created_at\"", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "\"updated_at\"", nullable = false)
    private OffsetDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieAward movieAward = (MovieAward) o;
        return Objects.equals(id, movieAward.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
