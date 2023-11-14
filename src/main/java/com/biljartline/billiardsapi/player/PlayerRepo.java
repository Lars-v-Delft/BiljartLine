package com.biljartline.billiardsapi.player;

import com.biljartline.billiardsapi.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepo extends  JpaRepository<Player, Long>{
    List<Player> findByTeams_Id(Long teamId);
}


