package com.biljartline.billiardsapi.team;

import com.biljartline.billiardsapi.exceptions.InvalidArgumentException;
import com.biljartline.billiardsapi.exceptions.InvalidArgumentsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.*;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/by-competition/{competitionId}")
    public List<TeamDTO> getByCompetitionId(@PathVariable long competitionId) {
        return teamService.getByCompetitionId(competitionId);
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
    @PatchMapping("/{id}")
    public TeamDTO patch(@PathVariable long id, @RequestBody Map<String, Object> fields) {
        TeamDTO teamDTO = teamService.getById(id);

        fields.forEach((key, value) -> {
            if (Objects.equals(key, "id"))
                throw new InvalidArgumentException("id cannot be changed");
            Field field = ReflectionUtils.findField(TeamDTO.class, key);
            if (field == null)
                throw new InvalidArgumentException(key + " is not a valid field for team");
            field.setAccessible(true);
            ReflectionUtils.setField(field, teamDTO, value);
        });

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<TeamDTO>> violations = validator.validate(teamDTO);

        List<FieldError> fieldErrors = new ArrayList<>();
        violations.forEach(violation ->
                fieldErrors.add(new FieldError(
                        "teamDTO",
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                )));

        if (!fieldErrors.isEmpty())
            throw new InvalidArgumentsException(fieldErrors);

        return teamService.update(teamDTO);
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
