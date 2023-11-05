package com.biljartline.billiardsapi.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CompetitionRepo extends JpaRepository<Competition, Long> {
    List<Competition> findByFederationId(long federationId);

    List<Competition> findByFederationIdAndEndDateAfterAndStartDateBeforeAndPublishedIsGreaterThanEqual
            (long federationId, LocalDate fromDate, LocalDate toDate, boolean publishedOnly);
    @Query("SELECT c FROM Competition c" +
            " WHERE c.federation.id = ?1" +
            " and c.endDate > ?2" +
            " and c.startDate < ?3" +
            " and c.published >= ?4")
    List<Competition> findDuring(long federationId, LocalDate fromDate, LocalDate toDate, boolean publishedOnly);
}
