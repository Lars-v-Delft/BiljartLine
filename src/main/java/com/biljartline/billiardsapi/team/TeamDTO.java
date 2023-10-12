package com.biljartline.billiardsapi.team;

import lombok.Setter;

import java.time.DayOfWeek;

@Setter
public class TeamDTO {
    private long id;
    private long competitionId;
    private String name;
    private DayOfWeek homeGameDay;
    private int timesViewed;
}
