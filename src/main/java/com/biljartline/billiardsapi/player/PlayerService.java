package com.biljartline.billiardsapi.player;

import com.biljartline.billiardsapi.team.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepo playerRepo;

    public Set<PlayerDTO> getAll(){
        List<Player> players = playerRepo.findAll();
        return players.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toSet());
    }

    public Set<PlayerDTO> getByTeam(long teamId){
        List<Player> players = playerRepo.findByTeams_Id(teamId);
        return players.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toSet());
    }

    private PlayerDTO convertToDTO(Player entity){
        PlayerDTO dto = new PlayerDTO();
        dto.setId(entity.getId());
        dto.setTeamIds(entity
                .getTeams()
                .stream()
                .mapToLong(Team::getId)
                .toArray());
        dto.setName(entity.getName());
        return dto;
    }

    //untested
    private Player convertToEntity(PlayerDTO dto){
        Player player = new Player();
        player.setId(dto.getId());

        Set<Team> teams = new HashSet<>();
        for (long teamId: dto.getTeamIds()) {
            Team team = new Team();
            team.setId(teamId);
            teams.add(team);
        }
        player.setTeams(teams);

        player.setName(dto.getName());
        return player;
    }
}
