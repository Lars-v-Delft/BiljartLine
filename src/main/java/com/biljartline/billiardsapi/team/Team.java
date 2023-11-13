package com.biljartline.billiardsapi.team;

import com.biljartline.billiardsapi.competition.Competition;
import com.biljartline.billiardsapi.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.Set;

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
    @ManyToMany
    @JoinTable(
            name = "team_player",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private Set<Player> players;
    private String name;
    private DayOfWeek homeGameDay;
    private int timesViewed;
}
