package com.biljartline.billiardsapi.competition;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class CompetitionDTO {
    private long id;
    private long federationId;
    private String name;
    private GameType gameType;
    private LocalDate startDate;
    private LocalDate endDate;
}
