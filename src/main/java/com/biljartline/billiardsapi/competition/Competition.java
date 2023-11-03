package com.biljartline.billiardsapi.competition;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long federationId;
    private String name;
    @Enumerated(EnumType.STRING)
    private GameType gameType;
    private LocalDate endDate;
    private LocalDate startDate;
    private boolean published;
}
