package com.biljartline.billiardsapi.team;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/byCompetition")
    public List<TeamDTO> getByCompetitionId(@RequestParam(value = "id") long competitionId) {
        return teamService.getByCompetitionId(competitionId);
    }
}