package com.biljartline.billiardsapi.competition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompetitionService {
    private final CompetitionRepo competitionRepo;
    @Autowired
    public CompetitionService(CompetitionRepo competitionRepo) {
        this.competitionRepo = competitionRepo;
    }

    public List<CompetitionDTO> getByFederationId(long federationId){
        List<Competition> temp = competitionRepo.findByFederationId(federationId);
        return temp.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CompetitionDTO convertToDTO(Competition competition){
        CompetitionDTO dto = new CompetitionDTO();
        dto.setId(competition.getId());
        dto.setFederationId(competition.getFederationId());
        dto.setName(competition.getName());
        dto.setGameType(competition.getGameType());
        dto.setStartDate(competition.getStartDate());
        dto.setEndDate(competition.getEndDate());
        return dto;
    }
}
