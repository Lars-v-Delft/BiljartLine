package com.biljartline.billiardsapi.team;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.DayOfWeek;

@Entity
@Getter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long competitionId;
    private String name;
    private DayOfWeek homeGameDay;
    private int timesViewed;
}
