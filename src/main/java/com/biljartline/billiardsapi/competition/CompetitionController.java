package com.biljartline.billiardsapi.competition;

import jakarta.validation.Valid;
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
    public List<CompetitionDTO> findByFederation(
            @RequestParam(value = "id") long federationId,
            @RequestParam(required = false, defaultValue = "0001-01-01", value = "fromDate") LocalDate fromDate,
            @RequestParam(required = false, defaultValue = "9999-09-09", value = "toDate") LocalDate toDate,
            @RequestParam(required = false, defaultValue = "true", value = "publishedOnly") boolean publishedOnly) {
        if (!publishedOnly) {
            // check permissions
        }
        return competitionService.getByFederation(federationId, fromDate, toDate, publishedOnly);
    }

    @GetMapping("/{id}")
    public CompetitionDTO get(@PathVariable("id") long id) {
        return competitionService.getById(id);
    }

    @PostMapping("/add")
    public CompetitionDTO add(@RequestBody @Valid CompetitionDTO competition) {
        return competitionService.add(competition);
    }

    @PutMapping("/update")
    public void update(@Valid @RequestBody CompetitionDTO competition) {
        competitionService.update(competition);
    }
}
