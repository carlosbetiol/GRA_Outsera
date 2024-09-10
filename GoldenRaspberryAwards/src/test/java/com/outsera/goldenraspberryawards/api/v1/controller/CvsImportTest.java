package com.outsera.goldenraspberryawards.api.v1.controller;

import com.outsera.goldenraspberryawards.domain.model.Movie;
import com.outsera.goldenraspberryawards.domain.model.MovieAward;
import com.outsera.goldenraspberryawards.domain.model.Producer;
import com.outsera.goldenraspberryawards.domain.model.Studio;
import com.outsera.goldenraspberryawards.domain.service.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.yml")
@ActiveProfiles( "test")
public class CvsImportTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void setup(@Autowired DatabasePopulateService databasePopulateService) {
        databasePopulateService.populateEntities();
    }

    @Test
    @Transactional
    public void shouldMatchMoviesRowsCount(@Autowired MovieService movieService) throws Exception {

        List<Movie> movies = movieService.findAll();
        Assert.isTrue(movies.size() == 51, String.format("Movies count should be 51, but %d was found", movies.size()));

    }

    @Test
    @Transactional
    public void shouldMatchStudiosRowsCount(@Autowired StudioService studioService) throws Exception {

        List<Studio> studios = studioService.findAll();
        Assert.isTrue(studios.size() == 27, String.format("Studios count should be 27, but %d was found", studios.size()));

    }

    @Test
    @Transactional
    public void shouldMatchProducersRowsCount(@Autowired ProducerService producerService) throws Exception {

        List<Producer> producers = producerService.findAll();
        Assert.isTrue(producers.size() == 132, String.format("Producers count should be 132, but %d was found", producers.size()));

    }

    @Test
    @Transactional
    public void shouldMatchMoviesAwardedRowsCount(@Autowired MovieAwardService movieAwardService) throws Exception {

        List<MovieAward> movieAwardedList = movieAwardService.findAll();
        Assert.isTrue(movieAwardedList.size() == 19, String.format("Movie Awards count should be 19, but %d was found", movieAwardedList.size()));

    }

}