package com.biljartline.billiardsapi.competition;

import com.biljartline.billiardsapi.exceptions.ApiNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompetitionService {
    private final CompetitionRepo competitionRepo;

    public List<CompetitionDTO> getByFederation
            (long federationId, LocalDate fromDate, LocalDate toDate, boolean includeUnpublished){
        List<Competition> competitions = competitionRepo
                .findByFederationIdAndStartDateBeforeAndEndDateAfterAndPublishedIsGreaterThanEqual
                        (federationId, toDate, fromDate, !includeUnpublished);
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
        dto.setPublished(competition.isPublished());
        return dto;
    }
}
