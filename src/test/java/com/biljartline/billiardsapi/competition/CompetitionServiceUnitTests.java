package com.biljartline.billiardsapi.competition;

import com.biljartline.billiardsapi.exceptions.InvalidArgumentException;
import com.biljartline.billiardsapi.exceptions.ResourceNotFoundException;
import com.biljartline.billiardsapi.federation.Federation;
import com.biljartline.billiardsapi.federation.FederationRepo;
import com.biljartline.billiardsapi.team.Team;
import com.biljartline.billiardsapi.team.TeamRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitionServiceUnitTests {
    @InjectMocks
    private CompetitionService competitionService;

    @Mock
    private CompetitionRepo competitionRepo;
    @Mock
    private FederationRepo federationRepo;
    @Mock
    private TeamRepo teamRepo;

    private static List<Federation> federationList;
    private static List<Team> teamList;

    @BeforeAll
    static void init() {
        federationList = List.of(
                new Federation(1),
                new Federation(2)
        );

        teamList = List.of(
                new Team(1),
                new Team(2),
                new Team(3),
                new Team(4)
        );
    }

    @Test
    void save_Happy() {
        // Arrange
        CompetitionDTO competitionDTO = new CompetitionDTO(
                0, federationList.get(0).getId(), "Happy Feet", new ArrayList<>(), "STRAIGHT_RAIL",
                "2020-01-01", "2021-01-01", false
        );
        Competition newCompetition = new Competition(
                0, federationList.get(0), "Happy Feet", new HashSet<>(), GameType.STRAIGHT_RAIL,
                LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), false
        );
        Competition addedCompetition = new Competition(
                10, federationList.get(0), "Happy Feet", new HashSet<>(), GameType.STRAIGHT_RAIL,
                LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), false
        );
        CompetitionDTO addedCompetitionDTO = new CompetitionDTO(
                10, federationList.get(0).getId(), "Happy Feet", new ArrayList<>(), "STRAIGHT_RAIL",
                "2020-01-01", "2021-01-01", false
        );
        when(federationRepo.existsById(federationList.get(0).getId())).thenReturn(true);
        ArgumentCaptor<Competition> competitionCaptor = ArgumentCaptor.forClass(Competition.class);
        when(competitionRepo.save(competitionCaptor.capture())).thenReturn(addedCompetition);

        // Act
        CompetitionDTO dto = competitionService.add(competitionDTO);

        // Assert
        assertEquals(newCompetition, competitionCaptor.getValue());
        assertTrue(Compare.equalsCompetitionDTO(addedCompetitionDTO, dto));

        verify(federationRepo).existsById(federationList.get(0).getId());
        verify(competitionRepo).save(any(Competition.class));
    }

    @Test
    void save_Sad_FederationIdUnknown() {
        // Arrange
        CompetitionDTO competitionDTO = new CompetitionDTO(
                0, federationList.get(0).getId(), "The unknown", new ArrayList<>(), "STRAIGHT_RAIL",
                "2020-01-01", "2021-01-01", false
        );
        when(federationRepo.existsById(federationList.get(0).getId())).thenReturn(false);

        // Act and Assert
        Exception exception = assertThrows(InvalidArgumentException.class, () -> competitionService.add(competitionDTO));

        assertEquals("Federation with id " + competitionDTO.getFederationId() + " is not known", exception.getMessage());
        verify(federationRepo).existsById(federationList.get(0).getId());
        verify(federationRepo, never()).save(any());
    }

    @Test
    void save_Sad_TeamIdPresent() {
        // Arrange
        CompetitionDTO competitionDTO = new CompetitionDTO(
                0, 1, "The Premature", List.of(1L), "STRAIGHT_RAIL",
                "2020-01-01", "2021-01-01", false
        );

        // Act and Assert
        Exception exception = assertThrows(InvalidArgumentException.class, () -> competitionService.add(competitionDTO));

        assertEquals("Competition cannot have teams at creation", exception.getMessage());
        Mockito.verify(federationRepo, Mockito.never()).save(any());
    }

    @Test
    void save_Sad_IdIsPresent() {
        // Arrange
        CompetitionDTO competitionDTO = new CompetitionDTO(
                1, 1, "The Premature", List.of(1L), "STRAIGHT_RAIL",
                "2020-01-01", "2021-01-01", false
        );

        // Act and Assert
        Exception exception = assertThrows(InvalidArgumentException.class, () -> competitionService.add(competitionDTO));

        assertEquals("Competition cannot have Id at creation", exception.getMessage());
        verify(federationRepo, never()).save(any());
    }

    @Test
    void save_Sad_StartDateAfterEndDate() {
        // Arrange
        CompetitionDTO competitionDTO = new CompetitionDTO(
                0, 1, "The Premature", new ArrayList<>(), "STRAIGHT_RAIL",
                "2021-01-01", "2020-01-01", false
        );

        // Act and Assert
        Exception exception = assertThrows(InvalidArgumentException.class, () -> competitionService.add(competitionDTO));

        assertEquals("Start date cannot be after end date", exception.getMessage());
        Mockito.verify(federationRepo, Mockito.never()).save(any());
    }

    @Test
    void update_Happy() {
        CompetitionDTO competitionDTO = new CompetitionDTO(
                1, 1, "Happy Hands", List.of(teamList.get(0).getId(), teamList.get(1).getId()), "BALKLINE",
                "2022-02-02", "2023-02-02", true
        );
        Competition existingCompetition = new Competition(
                1, federationList.get(0), "Happy Feet", Set.of(teamList.get(0), teamList.get(1)), GameType.STRAIGHT_RAIL,
                LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), false
        );
        Competition updatedCompetition = new Competition(
                1, federationList.get(0), "Happy Hands", Set.of(teamList.get(0), teamList.get(1)), GameType.BALKLINE,
                LocalDate.of(2022, 2, 2), LocalDate.of(2023, 2, 2), true
        );

        when(competitionRepo.findById(competitionDTO.getId())).thenReturn(Optional.of(existingCompetition));
        ArgumentCaptor<Competition> competitionCaptor = ArgumentCaptor.forClass(Competition.class);
        when(competitionRepo.save(competitionCaptor.capture())).thenReturn(updatedCompetition);

        // Act
        CompetitionDTO dto = competitionService.update(competitionDTO);

        // Assert
        assertEquals(updatedCompetition, competitionCaptor.getValue());
        assertTrue(Compare.equalsCompetitionDTO(competitionDTO, dto));

        verify(competitionRepo).findById(competitionDTO.getId());
        verify(competitionRepo).save(any(Competition.class));
    }

    @Test
    void update_Sad_StartDateAfterEndDate() {
        // Arrange
        CompetitionDTO competitionDTO = new CompetitionDTO(
                1, 1, "Happy Hands", List.of(teamList.get(0).getId(), teamList.get(1).getId()), "BALKLINE",
                "2022-02-02", "2021-02-02", true
        );

        // Act and Assert
        Exception exception = assertThrows(InvalidArgumentException.class, () -> competitionService.update(competitionDTO));

        assertEquals("Start date cannot be after end date", exception.getMessage());
        verify(federationRepo, never()).save(any());
    }

    @Test
    void update_Sad_UnknownId() {
        // Arrange
        CompetitionDTO competitionDTO = new CompetitionDTO(
                1, 1, "Happy Hands", List.of(teamList.get(0).getId(), teamList.get(1).getId()), "BALKLINE",
                "2022-02-02", "2023-02-02", true
        );
        when(competitionRepo.findById(competitionDTO.getId())).thenReturn(Optional.empty());

        // Act and Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> competitionService.update(competitionDTO));

        assertEquals("competition with id " + competitionDTO.getId() + " could not be found", exception.getMessage());
        verify(federationRepo, never()).save(any());
    }

    @Test
    void update_Sad_FederationIdChanged() {
        // Arrange
        CompetitionDTO competitionDTO = new CompetitionDTO(
                1, 2, "Happy Hands", List.of(teamList.get(0).getId(), teamList.get(1).getId()), "BALKLINE",
                "2022-02-02", "2023-02-02", true
        );
        Competition existing = new Competition(
                1, federationList.get(0), "Happy Hands", Set.of(teamList.get(0), teamList.get(1)), GameType.BALKLINE,
                LocalDate.of(2022, 2, 2), LocalDate.of(2022, 2, 2), true
        );
        when(competitionRepo.findById(competitionDTO.getId())).thenReturn(Optional.of(existing));

        // Act and Assert
        Exception exception = assertThrows(InvalidArgumentException.class, () -> competitionService.update(competitionDTO));

        assertEquals("federationId cannot be changed", exception.getMessage());
        verify(federationRepo, never()).save(any());
    }

    @Test
    void update_Sad_TeamIdsChanged() {
        // Arrange
        CompetitionDTO competitionDTO = new CompetitionDTO(
                1, 1, "Happy Hands", List.of(teamList.get(2).getId(), teamList.get(3).getId()), "BALKLINE",
                "2022-02-02", "2023-02-02", true
        );
        Competition existing = new Competition(
                1, federationList.get(0), "Happy Hands", Set.of(teamList.get(0), teamList.get(1)), GameType.BALKLINE,
                LocalDate.of(2022, 2, 2), LocalDate.of(2022, 2, 2), true
        );
        when(competitionRepo.findById(competitionDTO.getId())).thenReturn(Optional.of(existing));

        // Act and Assert
        Exception exception = assertThrows(InvalidArgumentException.class, () -> competitionService.update(competitionDTO));

        assertEquals("TeamsIds cannot be changed through competition, use team instead", exception.getMessage());
        verify(federationRepo, never()).save(any());
    }

    @Test
    void delete_Happy() {
        // Arrange
        long competitionId = 1;
        when(competitionRepo.existsById(competitionId)).thenReturn(true);
        when(teamRepo.findByCompetitionId(competitionId)).thenReturn(List.of(teamList.get(0), teamList.get(1)));
        doNothing().when(teamRepo).deleteById(teamList.get(0).getId());
        doNothing().when(teamRepo).deleteById(teamList.get(1).getId());
        doNothing().when(competitionRepo).deleteById(competitionId);

        // Act
        competitionService.delete(competitionId);

        // Assert
        verify(competitionRepo).existsById(competitionId);
        verify(teamRepo).findByCompetitionId(competitionId);
        verify(teamRepo).deleteById(teamList.get(0).getId());
        verify(teamRepo).deleteById(teamList.get(1).getId());
        verify(competitionRepo).deleteById(competitionId);
    }

    @Test
    void delete_Sad_UnknownId() {
        // Arrange
        long competitionId = 1;
        when(competitionRepo.existsById(competitionId)).thenReturn(false);

        // Act and Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> competitionService.delete(competitionId));

        assertEquals("competition with id " + competitionId + " could not be found", exception.getMessage());
        verify(competitionRepo, never()).deleteById(any());
        verify(teamRepo, never()).deleteById(any());
    }
}