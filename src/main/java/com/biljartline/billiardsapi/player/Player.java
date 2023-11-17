package com.biljartline.billiardsapi.player;

import com.biljartline.billiardsapi.team.Team;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Player {
    public Player(long id){
        this.id = id;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToMany(mappedBy = "players")
    private Set<Team> teams;
    private String name;
}
