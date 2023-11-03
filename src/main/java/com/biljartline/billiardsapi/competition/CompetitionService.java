package com.biljartline.billiardsapi.competition;

import com.biljartline.billiardsapi.exceptions.InvalidArgumentException;
import com.biljartline.billiardsapi.exceptions.ResourceNotFoundException;
import com.biljartline.billiardsapi.federation.FederationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompetitionService {
    private final CompetitionRepo competitionRepo;
    private final FederationRepo federationRepo;


    public List<CompetitionDTO> getByFederation
            (long federationId, LocalDate fromDate, LocalDate toDate, boolean publishedOnly){
        List<Competition> competitions = competitionRepo
                .findDuring(federationId, fromDate, toDate, publishedOnly);

        return competitions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CompetitionDTO getById(long competitionId) {
        Optional<Competition> competition = competitionRepo.findById(competitionId);
        if (competition.isPresent())
            return convertToDTO(competition.get());
        else
            throw new ResourceNotFoundException("competition with id " + competitionId + " could not be found");
    }

    public CompetitionDTO add(CompetitionDTO competitionDTO){
        Competition competition = convertToEntity(competitionDTO);
        if (competition.getId() != 0)
            throw new InvalidArgumentException("Competition cannot have Id before creation");
        if (competition.getStartDate().isAfter(competition.getEndDate()))
            throw new InvalidArgumentException("Start date cannot be after end date");
        if (!federationRepo.existsById(competition.getFederationId()))
            throw new InvalidArgumentException("Federation with id " + competitionDTO.getFederationId() + " is not known");

        return convertToDTO(competitionRepo.save(competition));
    }

    public void update(CompetitionDTO competitionDTO){
        if (!competitionRepo.existsById(competitionDTO.getId()))
            throw new ResourceNotFoundException("competition with id " + competitionDTO.getId() + " could not be found");
        competitionRepo.save(convertToEntity(competitionDTO));
    }

    private CompetitionDTO convertToDTO(Competition entity) {
        CompetitionDTO dto = new CompetitionDTO();
        dto.setId(entity.getId());
        dto.setFederationId(entity.getFederationId());
        dto.setName(entity.getName());
        dto.setGameType(entity.getGameType().toString());
        dto.setStartDate(entity.getStartDate().toString());
        dto.setEndDate(entity.getEndDate().toString());
        dto.setPublished(entity.isPublished());
        return dto;
    }

    private Competition convertToEntity(CompetitionDTO dto){
        Competition entity = new Competition();
        entity.setId(dto.getId());
        entity.setFederationId(dto.getFederationId());
        entity.setName(dto.getName());
        entity.setGameType(GameType.valueOf(dto.getGameType()));
        entity.setStartDate(LocalDate.parse(dto.getStartDate()));
        entity.setEndDate(LocalDate.parse(dto.getEndDate()));
        entity.setPublished(dto.isPublished());
        return entity;
    }
}
