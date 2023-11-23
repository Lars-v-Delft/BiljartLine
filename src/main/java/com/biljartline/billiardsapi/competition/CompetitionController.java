package com.biljartline.billiardsapi.competition;

import com.biljartline.billiardsapi.exceptions.InvalidArgumentException;
import com.biljartline.billiardsapi.exceptions.InvalidArgumentsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/competitions")
@RequiredArgsConstructor
public class CompetitionController {
    private final CompetitionService competitionService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/by-federation/{federationId}")
    public List<CompetitionDTO> findByFederation(
            @PathVariable long federationId,
            @RequestParam(required = false, defaultValue = "0001-01-01", value = "fromDate") LocalDate fromDate,
            @RequestParam(required = false, defaultValue = "9999-09-09", value = "toDate") LocalDate toDate,
            @RequestParam(required = false, defaultValue = "true", value = "publishedOnly") boolean publishedOnly) {
        if (!publishedOnly) {
            // check permissions
        }
        return competitionService.getByFederation(federationId, fromDate, toDate, publishedOnly);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public CompetitionDTO get(@PathVariable long id) {
        return competitionService.getById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public CompetitionDTO post(@RequestBody @Valid CompetitionDTO competition) {
        return competitionService.add(competition);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("")
    public CompetitionDTO put(@Valid @RequestBody CompetitionDTO competition) {
        return competitionService.update(competition);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public CompetitionDTO patch(@PathVariable long id, @RequestBody Map<String, Object> fields) {
        CompetitionDTO competitionDTO = competitionService.getById(id);

        fields.forEach((key, value) -> {
            if (Objects.equals(key, "id"))
                throw new InvalidArgumentException("id cannot be changed");
            Field field = ReflectionUtils.findField(CompetitionDTO.class, key);
            if (field == null)
                throw new InvalidArgumentException(key + " is not a valid field for competition");
            field.setAccessible(true);
            ReflectionUtils.setField(field, competitionDTO, value);
        });

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<CompetitionDTO>> violations = validator.validate(competitionDTO);

        List<FieldError> fieldErrors = new ArrayList<>();
        violations.forEach(violation ->
                fieldErrors.add(new FieldError(
                        "competitionDTO",
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                )));

        if (!fieldErrors.isEmpty())
            throw new InvalidArgumentsException(fieldErrors);

        return competitionService.update(competitionDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        competitionService.delete(id);
    }
}
