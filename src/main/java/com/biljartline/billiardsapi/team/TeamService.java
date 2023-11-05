package com.biljartline.billiardsapi.team;

import com.biljartline.billiardsapi.competition.CompetitionDTO;
import com.biljartline.billiardsapi.exceptions.ResourceNotFoundException;
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

    public boolean delete(long id){
        if (!teamRepo.existsById(id))
            throw new ResourceNotFoundException("team with id " + id + " could not be found");
        teamRepo.deleteById(id);
        return true;
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
