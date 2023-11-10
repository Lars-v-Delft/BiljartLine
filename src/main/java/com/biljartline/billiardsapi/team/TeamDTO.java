package com.biljartline.billiardsapi.team;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;

@Setter
@Getter
public class TeamDTO {
    private long id;
    private long competitionId;
    @Size(min = 5, max = 40)
    private String name;
    @Min(value = 1)
    @Max(value = 7)
    private int homeGameDay;
    @Min(value = 0)
    private int timesViewed;
}
