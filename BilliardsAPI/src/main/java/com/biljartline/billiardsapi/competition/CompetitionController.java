package com.biljartline.billiardsapi.competition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/competition")
public class CompetitionController {
private final CompetitionService competitionService;
@Autowired
    public CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @GetMapping("/byFederation")
    public List<CompetitionDTO> getByFederationId(@RequestParam(value = "id") long federationId){
    return competitionService.getByFederationId(federationId);
    }
}
