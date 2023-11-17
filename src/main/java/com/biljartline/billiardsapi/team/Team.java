package com.biljartline.billiardsapi.team;

import com.biljartline.billiardsapi.competition.Competition;
import com.biljartline.billiardsapi.player.Player;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Team {
    public Team(long id) {
        this.id = id;
    }

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
    @Enumerated(EnumType.STRING)
    private DayOfWeek homeGameDay;
    private int timesViewed;
}
