package com.biljartline.billiardsapi.team;

import com.biljartline.billiardsapi.competition.Competition;
import com.biljartline.billiardsapi.competition.CompetitionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepo extends JpaRepository<Team, Long> {
    List<Team> findByCompetitionId(long competitionId);
}


