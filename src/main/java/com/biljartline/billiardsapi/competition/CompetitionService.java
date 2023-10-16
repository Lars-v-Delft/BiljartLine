package com.biljartline.billiardsapi.competition;

import com.biljartline.billiardsapi.exceptions.ApiNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompetitionService {
    private final CompetitionRepo competitionRepo;

    @Autowired
    public CompetitionService(CompetitionRepo competitionRepo) {
        this.competitionRepo = competitionRepo;
    }

    public List<CompetitionDTO> getAllByFederationId(long federationId) {
        List<Competition> competitions = competitionRepo.findByFederationId(federationId);
        return competitions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CompetitionDTO> getByFederationDuring(long federationId, LocalDate fromDate, LocalDate toDate){
        if (fromDate == null)
            fromDate = LocalDate.ofYearDay(1, 1);
        if (toDate == null)
            toDate = LocalDate.ofYearDay(9999,364);
        List<Competition> competitions = competitionRepo
                .findByFederationIdAndStartDateBeforeAndEndDateAfter(federationId, toDate, fromDate);
        return competitions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CompetitionDTO getById(long competitionId) {
        Optional<Competition> competition = competitionRepo.findById(competitionId);
        if (competition.isPresent())
            return convertToDTO(competition.get());
        else
            throw new ApiNotFoundException("competition with id " + competitionId + " could not be found");
    }

    private CompetitionDTO convertToDTO(Competition competition) {
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
