package com.outsera.goldenraspberryawards.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsera.goldenraspberryawards.api.v1.model.response.AwardBorderResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.AwardIntervalResponseDTO;
import com.outsera.goldenraspberryawards.core.database.DatabaseProperties;
import com.outsera.goldenraspberryawards.core.helper.CSVHelper;
import com.outsera.goldenraspberryawards.domain.model.Movie;
import com.outsera.goldenraspberryawards.domain.model.MovieAward;
import com.outsera.goldenraspberryawards.domain.model.Producer;
import com.outsera.goldenraspberryawards.domain.model.Studio;
import com.outsera.goldenraspberryawards.domain.service.*;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.yml")
@ActiveProfiles( "test")
@Log4j2
@Transactional
public class ProducerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void setup(@Autowired DatabasePopulateService databasePopulateService) {

        try {
            databasePopulateService.populateEntities();
        } catch (IllegalArgumentException e) {
            log.error("Failed to populate data from CSV file - Reason: {}", e.getMessage());
        }
    }


    @Test
    public void shouldMatchCSVStructure(@Autowired DatabaseProperties databaseProperties) {

        try {
            List<Map<String, Set<Object>>> parsedData = CSVHelper.parseData(databaseProperties.getCsvFilePath());

            assertEquals(206, parsedData.size(), String.format("CSV file row count should be 206, but %d was found", parsedData.size()));

            Map<String, Set<Object>> firstRow = parsedData.get(0);

            assertEquals(5, firstRow.size(), String.format("Columns count should be 5, but %d was found", firstRow.size()));

            assertTrue(firstRow.keySet().stream().anyMatch(key -> key.equalsIgnoreCase("year")), "Column 'year' not found");
            assertTrue(firstRow.keySet().stream().anyMatch(key -> key.equalsIgnoreCase("title")), "Column 'title' not found");
            assertTrue(firstRow.keySet().stream().anyMatch(key -> key.equalsIgnoreCase("studios")), "Column 'studios' not found");
            assertTrue(firstRow.keySet().stream().anyMatch(key -> key.equalsIgnoreCase("producers")), "Column 'producers' not found");
            assertTrue(firstRow.keySet().stream().anyMatch(key -> key.equalsIgnoreCase("winner")), "Column 'winner' not found");

        } catch (IllegalArgumentException e) {
            fail("An IllegalArgumentException was thrown during CSV parsing: " + e.getMessage());
        }

    }


//    @Test
//    public void shouldMatchCSVStructure(@Autowired DatabaseProperties databaseProperties) throws Exception {
//
//        try {
//            List<Map<String, Set<Object>>> parsedData = CSVHelper.parseData(databaseProperties.getCsvFilePath());
//
//            Assert.isTrue(parsedData.size() == 206, String.format("CSV file row count should be 206, but %d was found", parsedData.size()));
//
//            Map<String, Set<Object>> firstRow = parsedData.get(0);
//
//            Assert.isTrue(firstRow.size() == 5, String.format("Columns count should be 5, but %d was found", firstRow.size()));
//
//            boolean keyExists = firstRow.keySet().stream()
//                    .anyMatch(key -> key.equalsIgnoreCase("year"));
//            Assert.isTrue(keyExists, "Column 'year' not found");
//
//            keyExists = firstRow.keySet().stream()
//                    .anyMatch(key -> key.equalsIgnoreCase("title"));
//            Assert.isTrue(keyExists, "Column 'title' not found");
//
//            keyExists = firstRow.keySet().stream()
//                    .anyMatch(key -> key.equalsIgnoreCase("studios"));
//            Assert.isTrue(keyExists, "Column 'studios' not found");
//
//            keyExists = firstRow.keySet().stream()
//                    .anyMatch(key -> key.equalsIgnoreCase("producers"));
//            Assert.isTrue(keyExists, "Column 'producers' not found");
//
//            keyExists = firstRow.keySet().stream()
//                    .anyMatch(key -> key.equalsIgnoreCase("winner"));
//            Assert.isTrue(keyExists, "Column 'winner' not found");
//
//        } catch (IllegalArgumentException e) {
//            Assert.isTrue(false, "An IllegalArgumentException was thrown during CSV parsing: " + e.getMessage());
//        }
//
//    }

    @Test
    public void shouldStatusOkResponseToGetTopProducers() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/producers/reports/top-least")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.min").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.max").exists());

    }

    @Test
    public void shouldMatchTopProducer(@Autowired ObjectMapper objectMapper) throws Exception {

        String responseContent = mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/producers/reports/top-least")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AwardBorderResponseDTO expected = new AwardBorderResponseDTO()
                .setMin(
                        List.of(
                                new AwardIntervalResponseDTO()
                                        .setProducer("Joel Silver")
                                        .setInterval(1)
                                        .setPreviousWin(1990)
                                        .setFollowingWin(1991)
                        )
                )
                .setMax(
                        List.of(
                                new AwardIntervalResponseDTO()
                                        .setProducer("Matthew Vaughn")
                                        .setInterval(13)
                                        .setPreviousWin(2002)
                                        .setFollowingWin(2015)
                        )
                );

        AwardBorderResponseDTO actualResponse = objectMapper.readValue(responseContent, AwardBorderResponseDTO.class);

        assertEquals(expected, actualResponse);

    }

    @Test
    public void shouldMatchMoviesRowsCount(@Autowired MovieService movieService) throws Exception {

        List<Movie> movies = movieService.findAll();
        assertEquals(206, movies.size(), String.format("Movies count should be 206, but %d was found", movies.size()));

    }

    @Test
    public void shouldMatchStudiosRowsCount(@Autowired StudioService studioService) throws Exception {

        List<Studio> studios = studioService.findAll();
        assertEquals(59, studios.size(), String.format("Studios count should be 59, but %d was found", studios.size()));

    }

    @Test
    public void shouldMatchProducersRowsCount(@Autowired ProducerService producerService) throws Exception {

        List<Producer> producers = producerService.findAll();
        assertEquals(359, producers.size(), String.format("Producers count should be 359, but %d was found", producers.size()));

    }

    @Test
    public void shouldMatchMoviesAwardedRowsCount(@Autowired MovieAwardService movieAwardService) throws Exception {

        List<MovieAward> movieAwardedList = movieAwardService.findAll();
        assertEquals(42, movieAwardedList.size(), String.format("Movie Awards count should be 42, but %d was found", movieAwardedList.size()));

    }


}