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
            @RequestParam(required = false, defaultValue = "0001-01-01", value = "fromDate") LocalDate fromDate,
            @RequestParam(required = false, defaultValue = "9999-09-09", value = "toDate") LocalDate toDate,
            @RequestParam(required = false, defaultValue = "false", value = "includeUnpublished") boolean includeUnpublished) {
        if (includeUnpublished) {
            // check permissions
        }
        return competitionService.getByFederation(federationId, fromDate, toDate, includeUnpublished);
    }

    @GetMapping("/{id}")
    public CompetitionDTO getById(@PathVariable("id") long id) {
        return competitionService.getById(id);
    }
}
