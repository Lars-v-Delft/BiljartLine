package com.biljartline.billiardsapi.federation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Federation {
    @Id
    private long id;
    private String name;
}
