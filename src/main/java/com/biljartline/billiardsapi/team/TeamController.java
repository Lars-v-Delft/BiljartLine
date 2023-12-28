package com.biljartline.billiardsapi.team;

import com.biljartline.billiardsapi.exceptions.InvalidArgumentException;
import com.biljartline.billiardsapi.exceptions.InvalidArgumentsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;
    private final Validator validator;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/by-competition/{competitionId}")
    public List<TeamDTO> getByCompetitionId(@PathVariable long competitionId) {
        return teamService.getByCompetitionId(competitionId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public TeamDTO get(@PathVariable long id) {
        return teamService.getById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public TeamDTO post(@RequestBody @Valid TeamDTO teamDTO) {
        return teamService.add(teamDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("")
    public TeamDTO put(@Valid @RequestBody TeamDTO teamDTO) {
        return teamService.update(teamDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public TeamDTO patch(@PathVariable long id, @RequestBody JsonPatch patch) {
        try {
            // get existing team
            TeamDTO teamDTO = teamService.getById(id);

            // apply patch to existing team
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode patchedJsonNode = patch.apply(objectMapper.convertValue(teamDTO, JsonNode.class));
            TeamDTO patchedTeamDTO = objectMapper.treeToValue(patchedJsonNode, TeamDTO.class);

            // validate patched team
            Set<ConstraintViolation<TeamDTO>> violations = validator.validate(patchedTeamDTO);
            List<FieldError> fieldErrors = new ArrayList<>();
            violations.forEach(violation ->
                    fieldErrors.add(new FieldError(
                            "teamDTO",
                            violation.getPropertyPath().toString(),
                            violation.getMessage()
                    )));
            if (!fieldErrors.isEmpty())
                throw new InvalidArgumentsException(fieldErrors);

            // update team
            return teamService.update(patchedTeamDTO);
        } catch (JsonPatchException e) {
            throw new InvalidArgumentException("Field unknown");
        } catch (JsonProcessingException e) {
            throw new InvalidArgumentException("Value invalid");
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        teamService.delete(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}/view-count")
    public int incrementViewCount(@PathVariable long id) {
        return teamService.incrementViewCount(id);
    }
}
