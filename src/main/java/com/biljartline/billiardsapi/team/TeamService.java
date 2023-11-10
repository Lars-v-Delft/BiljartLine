package com.biljartline.billiardsapi.team;

import com.biljartline.billiardsapi.competition.CompetitionRepo;
import com.biljartline.billiardsapi.exceptions.InvalidArgumentException;
import com.biljartline.billiardsapi.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepo teamRepo;
    private final CompetitionRepo competitionRepo;

    private Team getEntityById(long id) {
        Optional<Team> team = teamRepo.findById(id);
        if (team.isPresent())
            return team.get();
        else
            throw new ResourceNotFoundException("team with id " + id + " could not be found");
    }

    public List<TeamDTO> getByCompetitionId(long competitionId) {
        List<Team> teams = teamRepo.findByCompetitionId(competitionId);
        return teams.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TeamDTO getById(long id) {
        return convertToDTO(getEntityById(id));
    }

    public TeamDTO add(TeamDTO teamDTO) {
        Team team = convertToEntity(teamDTO);
        if (team.getId() != 0)
            throw new InvalidArgumentException("Team cannot have Id before creation");
        if (team.getTimesViewed() != 0)
            throw new InvalidArgumentException("Team cannot have views before creation");
        if (!competitionRepo.existsById(team.getCompetitionId()))
            throw new InvalidArgumentException("Competition with id " + team.getCompetitionId() + " is not known");

        return convertToDTO(teamRepo.save(team));
    }

    public TeamDTO update(TeamDTO teamDTO) {
        Team team = convertToEntity(teamDTO);
        if (!teamRepo.existsById(team.getId()))
            throw new ResourceNotFoundException("team with id " + team.getId() + " could not be found");
        Team original = getEntityById(team.getId());
        if (original.getCompetitionId() != team.getCompetitionId())
            throw new InvalidArgumentException("teamId cannot be changed");

        return convertToDTO(teamRepo.save(team));
    }

    public void delete(long id) {
        if (!teamRepo.existsById(id))
            throw new ResourceNotFoundException("team with id " + id + " could not be found");
        teamRepo.deleteById(id);
    }

    public int incrementViewCount(long id) {
        Team team = getEntityById(id);
        team.setTimesViewed(team.getTimesViewed()+1);
        return teamRepo.save(team).getTimesViewed();
    }

    private TeamDTO convertToDTO(Team team) {
        TeamDTO dto = new TeamDTO();
        dto.setId(team.getId());
        dto.setCompetitionId(team.getCompetitionId());
        dto.setName(team.getName());
        dto.setHomeGameDay(team.getHomeGameDay().ordinal());
        dto.setTimesViewed(team.getTimesViewed());
        return dto;
    }

    private Team convertToEntity(TeamDTO dto) {
        Team entity = new Team();
        entity.setId(dto.getId());
        entity.setCompetitionId(dto.getCompetitionId());
        entity.setName(dto.getName());
        entity.setHomeGameDay(DayOfWeek.of(dto.getHomeGameDay()));
        entity.setTimesViewed(dto.getTimesViewed());
        return entity;
    }
}
