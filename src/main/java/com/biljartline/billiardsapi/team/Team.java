package com.biljartline.billiardsapi.team;

import com.biljartline.billiardsapi.competition.Competition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;

@Entity
@Getter
@Setter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;
    private String name;
    private DayOfWeek homeGameDay;
    private int timesViewed;
}
