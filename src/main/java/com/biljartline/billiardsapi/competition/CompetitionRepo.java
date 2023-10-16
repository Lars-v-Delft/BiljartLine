package com.biljartline.billiardsapi.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CompetitionRepo extends JpaRepository<Competition, Long> {
    List<Competition> findByFederationId(long federationId);
    List<Competition> findByFederationIdAndStartDateBeforeAndEndDateAfter(long federationId, LocalDate startDate, LocalDate endDate);
}
