package com.biljartline.billiardsapi.competition;

import com.biljartline.billiardsapi.annotations.After;
import com.biljartline.billiardsapi.annotations.ValueOfEnum;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionDTO {
    private long id;
    private long federationId;
    @Size(min = 5, max = 40)
    private String name;
    private List<Long> teamIds = new ArrayList<>();
    @ValueOfEnum(enumClass = GameType.class)
    private String gameType;
    @After(compareDate = "1900-01-01")
    private String startDate;
    @After(compareDate = "1900-01-01")
    private String endDate;
    private boolean published;
}
