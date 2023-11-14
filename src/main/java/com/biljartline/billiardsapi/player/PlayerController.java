package com.biljartline.billiardsapi.player;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/by-team/{teamId}")
    public Set<PlayerDTO> getByTeamId(@PathVariable long teamId) {
        return playerService.getByTeam(teamId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public Set<PlayerDTO> getAll() {
        return playerService.getAll();
    }
}
