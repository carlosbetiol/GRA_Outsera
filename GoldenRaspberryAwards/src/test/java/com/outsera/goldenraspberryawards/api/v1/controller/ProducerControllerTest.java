package com.outsera.goldenraspberryawards.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsera.goldenraspberryawards.api.v1.model.response.AwardBorderResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.AwardIntervalResponseDTO;
import com.outsera.goldenraspberryawards.domain.service.DatabasePopulateService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.yml")
@ActiveProfiles( "test")
public class ProducerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void setup(@Autowired DatabasePopulateService databasePopulateService) {
        databasePopulateService.populateEntities();
    }

    @Test
    @Transactional
    public void shouldStatusOkResponseToGetTopProducers() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/producers/reports/top")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.min").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.max").exists());

    }

    @Test
    @Transactional
    public void shouldMatchTopProducer(@Autowired ObjectMapper objectMapper) throws Exception {

        String responseContent = mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/producers/reports/top")
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
                                        .setProducer("Adam Sandler")
                                        .setInterval(0)
                                        .setPreviousWin(2015)
                                        .setFollowingWin(2015)
                        )
                )
                .setMax(
                        List.of(
                                new AwardIntervalResponseDTO()
                                        .setProducer("Jennifer Davisson")
                                        .setInterval(22)
                                        .setPreviousWin(1996)
                                        .setFollowingWin(2018)
                        )
                );

        AwardBorderResponseDTO actualResponse = objectMapper.readValue(responseContent, AwardBorderResponseDTO.class);

        assertEquals(expected, actualResponse);

    }

}