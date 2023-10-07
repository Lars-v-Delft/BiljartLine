package com.biljartline.billiardsapi.competition;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionRepo extends JpaRepository<Competition, Long> {
}
