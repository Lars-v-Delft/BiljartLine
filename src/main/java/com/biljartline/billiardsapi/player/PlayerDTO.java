package com.biljartline.billiardsapi.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDTO {
    private long id;
    private long[] teamIds = new long[0];
    private String name;
}