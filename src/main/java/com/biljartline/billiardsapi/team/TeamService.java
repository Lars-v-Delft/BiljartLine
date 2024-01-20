package com.biljartline.billiardsapi.team;

import com.biljartline.billiardsapi.competition.Competition;
import com.biljartline.billiardsapi.competition.CompetitionRepo;
import com.biljartline.billiardsapi.exceptions.InvalidArgumentException;
import com.biljartline.billiardsapi.exceptions.ResourceNotFoundException;
import com.biljartline.billiardsapi.player.Player;
import com.biljartline.billiardsapi.player.PlayerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepo teamRepo;
    private final CompetitionRepo competitionRepo;
    private final PlayerRepo playerRepo;

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
                .toList();
    }

    public TeamDTO getById(long id) {
        Team team = getEntityById(id);
        return convertToDTO(team);
    }

    public TeamDTO add(TeamDTO teamDTO) {
        Team team = convertToEntity(teamDTO);
        if (team.getId() != 0)
            throw new InvalidArgumentException("Team cannot have Id at creation");
        if (team.getTimesViewed() != 0)
            throw new InvalidArgumentException("Team cannot have views at creation");

        genericValidation(team);
        return convertToDTO(teamRepo.save(team));
    }

    public TeamDTO update(TeamDTO teamDTO) {
        Team team = convertToEntity(teamDTO);
        if (!teamRepo.existsById(team.getId()))
            throw new ResourceNotFoundException("team with id " + team.getId() + " could not be found");

        Team original = getEntityById(team.getId());
        if (original.getCompetition().getId() != team.getCompetition().getId())
            throw new InvalidArgumentException("teamId cannot be changed");

        genericValidation(team);
        return convertToDTO(teamRepo.save(team));
    }

    private void genericValidation(Team team){
        Set<Long> playerIds = team.getPlayers().stream()
                .map(Player::getId)
                .collect(Collectors.toSet());
        if (playerIds.size() != team.getPlayers().size())
            throw new InvalidArgumentException("Team cannot have the same player twice");

        if (!competitionRepo.existsById(team.getCompetition().getId()))
            throw new InvalidArgumentException("Competition with id " + team.getCompetition().getId() + " is not known");
        for (Player player : team.getPlayers())
            if (!playerRepo.existsById(player.getId()))
                throw new InvalidArgumentException("Player with id " + player.getId() + " is not known");
    }

    public void delete(long id) {
        if (!teamRepo.existsById(id))
            throw new ResourceNotFoundException("team with id " + id + " could not be found");
        teamRepo.deleteById(id);
    }

    public int incrementViewCount(long id) {
        Team team = getEntityById(id);
        team.setTimesViewed(team.getTimesViewed() + 1);
        return teamRepo.save(team).getTimesViewed();
    }

    private TeamDTO convertToDTO(Team entity) {
        TeamDTO dto = new TeamDTO();
        dto.setId(entity.getId());
        dto.setCompetitionId(entity.getCompetition().getId());
        dto.setPlayerIds(entity
                .getPlayers()
                .stream()
                .mapToLong(Player::getId)
                .toArray());
        dto.setName(entity.getName());
        dto.setHomeGameDay(entity.getHomeGameDay().getValue());
        dto.setTimesViewed(entity.getTimesViewed());
        return dto;
    }

    private Team convertToEntity(TeamDTO dto) {
        Team entity = new Team();
        entity.setId(dto.getId());
        // set parent
        Competition competition = new Competition();
        competition.setId(dto.getCompetitionId());
        entity.setCompetition(competition);
        // set many-to-many
        Set<Player> players = new HashSet<>();
        for (long playerId : dto.getPlayerIds()) {
            Player player = new Player();
            player.setId(playerId);
            players.add(player);
        }
        entity.setPlayers(players);
        // set basic data
        entity.setName(dto.getName());
        entity.setHomeGameDay(DayOfWeek.of(dto.getHomeGameDay()));
        entity.setTimesViewed(dto.getTimesViewed());
        return entity;
    }
}
