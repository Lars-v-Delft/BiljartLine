package com.biljartline.billiardsapi.team;

import com.biljartline.billiardsapi.competition.CompetitionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepo teamRepo;

    public List<TeamDTO> getByCompetitionId(long competitionId) {
    List<Team> teams = teamRepo.findByCompetitionId(competitionId);
    return teams.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private TeamDTO convertToDTO(Team team){
        TeamDTO dto = new TeamDTO();
        dto.setId(team.getId());
        dto.setCompetitionId(team.getCompetitionId());
        dto.setName(team.getName());
        dto.setHomeGameDay(team.getHomeGameDay());
        dto.setTimesViewed(team.getTimesViewed());
        return dto;
    }
}
