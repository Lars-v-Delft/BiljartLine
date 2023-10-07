package com.biljartline.billiardsapi.competition;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class Competition {
    @Id
    private long id;
    private long federationId;
    private String name;
    private GameType GameType;
    private LocalDate startDate;
    private LocalDate endDate;

    private enum GameType {
        STRAIGHT_RAIL, BALKLINE, ONE_CUSHION, THREE_CUSHION
    }
}
