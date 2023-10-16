package com.biljartline.billiardsapi.competition;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/competition")
@RequiredArgsConstructor
public class CompetitionController {
    private final CompetitionService competitionService;

    @GetMapping("/byFederation")
    public List<CompetitionDTO> getByFederation(
            @RequestParam(value = "id") long federationId,
            @RequestParam(required = false, value = "fromDate") LocalDate fromDate,
            @RequestParam(required = false, value = "toDate") LocalDate toDate) {
        if (fromDate == null && toDate == null)
            return competitionService.getAllByFederationId(federationId);
        else {
            return competitionService.getByFederationDuring(federationId, fromDate, toDate);
        }
    }

    @GetMapping("/{id}")
    public CompetitionDTO getById(@PathVariable("id") long id) {
        return competitionService.getById(id);
    }
}
