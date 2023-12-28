package com.biljartline.billiardsapi.competition;

import com.biljartline.billiardsapi.exceptions.InvalidArgumentException;
import com.biljartline.billiardsapi.exceptions.ResourceNotFoundException;
import com.biljartline.billiardsapi.federation.Federation;
import com.biljartline.billiardsapi.federation.FederationRepo;
import com.biljartline.billiardsapi.team.Team;
import com.biljartline.billiardsapi.team.TeamRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompetitionService {
    private final CompetitionRepo competitionRepo;
    private final FederationRepo federationRepo;
    private final TeamRepo teamRepo;

    public List<CompetitionDTO> getByFederation
            (long federationId, LocalDate fromDate, LocalDate toDate, boolean publishedOnly) {
        List<Competition> competitions = competitionRepo
                .findDuring(federationId, fromDate, toDate, publishedOnly);

        return competitions.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private Competition getEntityById(long id) {
        Optional<Competition> competition = competitionRepo.findById(id);
        if (competition.isPresent())
            return competition.get();
        else
            throw new ResourceNotFoundException("competition with id " + id + " could not be found");
    }

    public CompetitionDTO getById(long id) {
        return convertToDTO(getEntityById(id));
    }

    public CompetitionDTO add(CompetitionDTO competitionDTO) {
        Competition competition = convertToEntity(competitionDTO);
        if (competition.getId() != 0)
            throw new InvalidArgumentException("Competition cannot have Id at creation");
        if (!competition.getTeams().isEmpty())
            throw new InvalidArgumentException("Competition cannot have teams at creation");
        if (competition.getStartDate().isAfter(competition.getEndDate()))

            throw new InvalidArgumentException("Start date cannot be after end date");
        if (!federationRepo.existsById(competition.getFederation().getId()))
            throw new InvalidArgumentException("Federation with id " + competitionDTO.getFederationId() + " is not known");

        return convertToDTO(competitionRepo.save(competition));
    }

    public CompetitionDTO update(CompetitionDTO competitionDTO) {
        Competition competition = convertToEntity(competitionDTO);
        if (competition.getStartDate().isAfter(competition.getEndDate()))
            throw new InvalidArgumentException("Start date cannot be after end date");

        Competition original = getEntityById(competition.getId());
        if (original.getFederation().getId() != competition.getFederation().getId())
            throw new InvalidArgumentException("federationId cannot be changed");
        if (!original.getTeams().stream().map(Team::getId).collect(Collectors.toSet())
                .equals(competition.getTeams().stream().map(Team::getId).collect(Collectors.toSet())))
            throw new InvalidArgumentException("TeamsIds cannot be changed through competition, use team instead");

        return convertToDTO(competitionRepo.save(competition));
    }

    public void delete(long id) {
        if (!competitionRepo.existsById(id))
            throw new ResourceNotFoundException("competition with id " + id + " could not be found");
        List<Team> teams = teamRepo.findByCompetitionId(id);
        teams.forEach(t -> teamRepo.deleteById(t.getId()));
        competitionRepo.deleteById(id);
    }

    private CompetitionDTO convertToDTO(Competition entity) {
        CompetitionDTO dto = new CompetitionDTO();
        dto.setId(entity.getId());
        dto.setFederationId(entity.getFederation().getId());
        dto.setName(entity.getName());
        dto.setTeamIds(entity.getTeams().stream().map(Team::getId).toList());
        dto.setGameType(entity.getGameType().toString());
        dto.setStartDate(entity.getStartDate().toString());
        dto.setEndDate(entity.getEndDate().toString());
        dto.setPublished(entity.isPublished());
        return dto;
    }

    private Competition convertToEntity(CompetitionDTO dto) {
        Competition entity = new Competition();
        entity.setId(dto.getId());
        // set parent
        Federation federation = new Federation();
        federation.setId(dto.getFederationId());
        entity.setFederation(federation);
        // set children
        Set<Team> teams = dto.getTeamIds().stream().map(teamId -> {
            Team team = new Team();
            team.setId(teamId);
            return team;
        }).collect(Collectors.toSet());
        entity.setTeams(teams);
        // set basic data
        entity.setName(dto.getName());
        entity.setGameType(GameType.valueOf(dto.getGameType()));
        entity.setStartDate(LocalDate.parse(dto.getStartDate()));
        entity.setEndDate(LocalDate.parse(dto.getEndDate()));
        entity.setPublished(dto.isPublished());

        return entity;
    }
}
