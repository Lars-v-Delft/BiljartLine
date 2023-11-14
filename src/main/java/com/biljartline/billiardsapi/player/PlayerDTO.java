package com.biljartline.billiardsapi.player;

import com.biljartline.billiardsapi.team.Team;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PlayerDTO {
    private long id;
    private long[] teamIds = new long[0];
    private String name;
}