package com.biljartline.billiardsapi.competition;

import com.biljartline.billiardsapi.BilliardsApiApplication;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
// indicates this class is a springboot integration test
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,    // what environment to use
        classes = BilliardsApiApplication.class)                // what @SpringBootApplication class to use
@AutoConfigureMockMvc
// what application properties to use
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class CompetitionRestControllerIntegrationTests {
    @LocalServerPort
    private int port;
    private String baseURL = "http://localhost";
    private static TestRestTemplate template;
    @BeforeAll
    public static void init(){
        template = new TestRestTemplate();
    }

    @BeforeEach
    public void setup(){
        baseURL = baseURL
                .concat(":")
                .concat(String.valueOf(port))
                .concat("/competitions");
    }
//    @Test
//    public void getById(){
//
//    }

    @Test
    public void add(){
        CompetitionDTO competitionDTO = new CompetitionDTO(
                0, 1, "Happy Feet", new ArrayList<>(), "STRAIGHT_RAIL",
                "2020-01-01", "2021-01-01", false
        );
    }
}
