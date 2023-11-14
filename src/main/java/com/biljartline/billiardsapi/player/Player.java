package com.biljartline.billiardsapi.player;

import com.biljartline.billiardsapi.team.Team;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToMany(mappedBy = "players")
    private Set<Team> teams;
    private String name;
}
