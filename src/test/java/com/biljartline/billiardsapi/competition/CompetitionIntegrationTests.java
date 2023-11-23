package com.biljartline.billiardsapi.competition;

import com.biljartline.billiardsapi.federation.Federation;
import com.biljartline.billiardsapi.federation.FederationRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class CompetitionIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private FederationRepo federationRepo;
    @Autowired
    private CompetitionRepo competitionRepo;

    @BeforeAll
    public void init() {
        Federation fed1 = new Federation(0, "Baseliners");
        Federation federation = federationRepo.save(fed1);
        Competition comp1 = new Competition(
                0, fed1, "The First", new HashSet<>(), GameType.STRAIGHT_RAIL,
                LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), true
        );
        Competition competition = competitionRepo.save(comp1);
    }

    @Test
    @Order(1)
    public void add() throws Exception {
        CompetitionDTO competitionDTO = new CompetitionDTO(
                0, 1, "Happy Feet", new ArrayList<>(), "STRAIGHT_RAIL",
                "2020-01-01", "2021-01-01", false
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/competitions")
                        .content(objectMapper.writeValueAsBytes(competitionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    public void put() throws Exception {
        CompetitionDTO competitionDTO = new CompetitionDTO(
                2, 1, "Happy Hands", new ArrayList<>(), "BALKLINE",
                "2021-01-01", "2022-01-01", true
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/competitions")
                        .content(objectMapper.writeValueAsBytes(competitionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void patch() throws Exception {
        Map<String, Object> patchMap = new HashMap<>();
        patchMap.put("name", "Happy Hands");
        patchMap.put("gameType", "BALKLINE");
        patchMap.put("startDate", "2021-01-01");
        patchMap.put("endDate", "2022-01-01");
        patchMap.put("published", true);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/competitions/2")
                        .content(objectMapper.writeValueAsBytes(patchMap))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/competitions/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(5)
    public void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/competitions/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    public void findByFederation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/competitions/by-federation/1")
                        .param("fromDate", "2020-01-01")
                        .param("toDate", "2021-01-01")
                        .param("publishedOnly", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
