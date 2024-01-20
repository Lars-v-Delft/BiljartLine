package com.biljartline.billiardsapi.competition;

import com.biljartline.billiardsapi.federation.Federation;
import com.biljartline.billiardsapi.federation.FederationRepo;
import com.biljartline.billiardsapi.security.UserDetailsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CompetitionIntegrationTests {
    private final String jwt = "ThisIsAFakeJWT_DoNotBeFooled";

    private final UserDetailsDTO adminDTO = new UserDetailsDTO(
            "admin", Set.of("ADMIN"),
            true, true, true, true
    );

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    // these should be Mock?
    @Autowired
    private FederationRepo federationRepo;
    @Autowired
    private CompetitionRepo competitionRepo;

    @BeforeAll
    void init() {
        Federation fed1 = new Federation(0, "Baseliners");
        federationRepo.save(fed1);
        Competition comp1 = new Competition(
                0, fed1, "The First", new HashSet<>(), GameType.STRAIGHT_RAIL,
                LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), true
        );
        competitionRepo.save(comp1);
    }

    @Test
    @Order(1)
    void add() throws Exception {
        when(restTemplate.postForEntity(
                anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(
                        objectMapper.writeValueAsString(adminDTO), HttpStatus.OK));

        CompetitionDTO competitionDTO = new CompetitionDTO(
                0, 1, "Happy Feet", new ArrayList<>(), "STRAIGHT_RAIL",
                "2020-01-01", "2021-01-01", false
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/competitions")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .content(objectMapper.writeValueAsBytes(competitionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void put() throws Exception {
        when(restTemplate.postForEntity(
                anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(
                        objectMapper.writeValueAsString(adminDTO), HttpStatus.OK));

        CompetitionDTO competitionDTO = new CompetitionDTO(
                2, 1, "Happy Hands", new ArrayList<>(), "BALKLINE",
                "2021-01-01", "2022-01-01", true
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/competitions")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .content(objectMapper.writeValueAsBytes(competitionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void patch() throws Exception {
        when(restTemplate.postForEntity(
                anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(
                        objectMapper.writeValueAsString(adminDTO), HttpStatus.OK));

        String jsonPatch = "[" +
                "{ \"op\": \"replace\", \"path\": \"/name\", \"value\": \"Newbies\" }," +
                "{ \"op\": \"replace\", \"path\": \"/gameType\", \"value\": \"BALKLINE\" }," +
                "{ \"op\": \"replace\", \"path\": \"/startDate\", \"value\": \"2023-01-01\" }," +
                "{ \"op\": \"replace\", \"path\": \"/endDate\", \"value\": \"2023-12-31\" }," +
                "{ \"op\": \"replace\", \"path\": \"/published\", \"value\": true }" +
                "]";

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/competitions/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .content(jsonPatch)
                        .contentType("application/json-patch+json"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void delete() throws Exception {
        when(restTemplate.postForEntity(
                anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(
                        objectMapper.writeValueAsString(adminDTO), HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/competitions/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(5)
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/competitions/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void findByFederation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/competitions/by-federation/1")
                        .param("fromDate", "2020-01-01")
                        .param("toDate", "2021-01-01")
                        .param("publishedOnly", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
