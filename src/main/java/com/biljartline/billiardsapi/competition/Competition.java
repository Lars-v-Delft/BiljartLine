package com.biljartline.billiardsapi.competition;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
public class Competition {
    @Id
    private long id;
    private long federationId;
    private String name;
    @Enumerated(EnumType.STRING)
    private GameType gameType;
    private LocalDate endDate;
    private LocalDate startDate;
}
