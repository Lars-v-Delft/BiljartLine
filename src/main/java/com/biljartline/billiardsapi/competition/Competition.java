package com.biljartline.billiardsapi.competition;

import com.biljartline.billiardsapi.federation.Federation;
import com.biljartline.billiardsapi.team.Team;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "federation_id")
    private Federation federation;
    private String name;
    @OneToMany(mappedBy = "competition")
    private Set<Team> teams;
    @Enumerated(EnumType.STRING)
    private GameType gameType;
    private LocalDate endDate;
    private LocalDate startDate;
    private boolean published;
}
