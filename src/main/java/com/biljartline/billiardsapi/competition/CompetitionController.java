package com.biljartline.billiardsapi.competition;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/competition")
@RequiredArgsConstructor
public class CompetitionController {
    private final CompetitionService competitionService;

    @GetMapping("/byFederation")
    public List<CompetitionDTO> getByFederationId(@RequestParam(value = "federationId") long federationId) {
        return competitionService.getByFederationId(federationId);
    }
}
