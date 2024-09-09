package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.domain.model.Movie;
import com.outsera.goldenraspberryawards.domain.model.MovieAward;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.outsera.goldenraspberryawards.domain.mapper.DomainMovieAwardMapper.DOMAIN_MOVIE_AWARD_MAPPER;
import static com.outsera.goldenraspberryawards.domain.mapper.DomainMovieMapper.DOMAIN_MOVIE_MAPPER;
import static com.outsera.goldenraspberryawards.domain.mapper.DomainProducerMapper.DOMAIN_PRODUCER_MAPPER;
import static com.outsera.goldenraspberryawards.domain.mapper.DomainStudioMapper.DOMAIN_STUDIO_MAPPER;

@Service
public class DatabasePopulateServiceImpl implements DatabasePopulateService {

    private final ProducerService producerService;

    private final StudioService studioService;

    private final MovieService movieService;

    private final MovieAwardService movieAwardService;

    public DatabasePopulateServiceImpl(ProducerService producerService, StudioService studioService, MovieService movieService, MovieAwardService movieAwardService) {
        this.producerService = producerService;
        this.studioService = studioService;
        this.movieService = movieService;
        this.movieAwardService = movieAwardService;
    }

    @Override
    public void populateEntities(List<Map<String, Set<Object>>> data) {

        data.forEach( map -> {

            Set<Object> movies = map.get("title");
            String movieName = movies.stream().findFirst().get().toString();
            Movie movie = movieService.findByName(movieName).orElseGet(() -> DOMAIN_MOVIE_MAPPER.createEntity(movieName));

            Set<Object> studios = map.get("studios");
            Set<Object> producers = map.get("producers");

            studios.forEach(studio -> movie.getStudios().add(
                    studioService.findByName(studio.toString())
                            .orElseGet(() ->
                                    studioService.saveLogLess(DOMAIN_STUDIO_MAPPER.createEntity(studio.toString()))
                            )));


            producers.forEach(producer -> movie.getProducers().add(
                    producerService.findByName(producer.toString())
                            .orElseGet(() ->
                                    producerService.saveLogLess(DOMAIN_PRODUCER_MAPPER.createEntity(producer.toString()))
                            )));

            Movie persistedMovie = movieService.saveLogLess(movie);

            Set<Object> winners = map.get("winner");
            Optional<Object> winner = winners.stream().findFirst();

            if (winner.isPresent() && List.of("yes", "winner").contains(winner.get().toString())) {

                Set<Object> years = map.get("year");
                Integer year = Integer.parseInt(years.stream().findFirst().get().toString());

                MovieAward movieAward = DOMAIN_MOVIE_AWARD_MAPPER.createEntity(year);
                movieAward.setMovieWinner(persistedMovie);
                movieAwardService.saveLogLess(movieAward);

            }

        });

    }

}
