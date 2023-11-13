package com.biljartline.billiardsapi.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepo extends  JpaRepository<Player, Long>{
}


