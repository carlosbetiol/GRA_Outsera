package com.outsera.goldenraspberryawards.domain.service;

import com.outsera.goldenraspberryawards.core.database.DatabaseProperties;
import com.outsera.goldenraspberryawards.core.helper.CSVHelper;
import com.outsera.goldenraspberryawards.domain.model.Movie;
import com.outsera.goldenraspberryawards.domain.model.MovieAward;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.outsera.goldenraspberryawards.domain.mapper.DomainMovieAwardMapper.DOMAIN_MOVIE_AWARD_MAPPER;
import static com.outsera.goldenraspberryawards.domain.mapper.DomainMovieMapper.DOMAIN_MOVIE_MAPPER;
import static com.outsera.goldenraspberryawards.domain.mapper.DomainProducerMapper.DOMAIN_PRODUCER_MAPPER;
import static com.outsera.goldenraspberryawards.domain.mapper.DomainStudioMapper.DOMAIN_STUDIO_MAPPER;
import static java.util.Objects.isNull;

@Service
@Log4j2
public class DatabasePopulateServiceImpl implements DatabasePopulateService {

    private final ProducerService producerService;

    private final StudioService studioService;

    private final MovieService movieService;

    private final MovieAwardService movieAwardService;

    private final DatabaseProperties databaseProperties;

    public DatabasePopulateServiceImpl(ProducerService producerService, StudioService studioService, MovieService movieService, MovieAwardService movieAwardService, DatabaseProperties databaseProperties) {
        this.producerService = producerService;
        this.studioService = studioService;
        this.movieService = movieService;
        this.movieAwardService = movieAwardService;
        this.databaseProperties = databaseProperties;
    }

    @Override
    @Transactional
    public void populateEntities() {

        log.info("Initializing Entities from CSV file");


        try {
            List<Map<String, Set<Object>>> parsedData = CSVHelper.parseData(databaseProperties.getCsvFilePath());

            if( isNull(parsedData) || parsedData.isEmpty() ) {
                log.error("Failed to populate data from CSV file - Reason: No data found to populate");
                return;
            }

            populateEntities(parsedData);

            log.info("Entities initialized from CSV file");
        } catch (IllegalArgumentException e) {
            log.error("Failed to populate data from CSV file - Reason: {}", e.getMessage());

        }

    }

    @Override
    @Transactional
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
            Optional<Object> winner = winners.stream().filter(Objects::nonNull).findFirst();

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
