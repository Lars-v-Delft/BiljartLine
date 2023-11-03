package com.biljartline.billiardsapi.competition;

import com.biljartline.billiardsapi.exceptions.InvalidArgumentException;
import com.biljartline.billiardsapi.exceptions.InvalidArgumentsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/competition")
@RequiredArgsConstructor
public class CompetitionController {
    private final CompetitionService competitionService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/byFederation")
    public List<CompetitionDTO> findByFederation(
            @RequestParam(value = "id") long federationId,
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
    public CompetitionDTO get(@PathVariable("id") long id) {
        return competitionService.getById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public CompetitionDTO add(@RequestBody @Valid CompetitionDTO competition) {
        return competitionService.add(competition);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/update")
    public CompetitionDTO update(@Valid @RequestBody CompetitionDTO competition) {
        return competitionService.update(competition);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/update/{id}")
    public CompetitionDTO update(@PathVariable long id, @RequestBody Map<String, Object> fields) {
        CompetitionDTO competitionDTO = competitionService.getById(id);;

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(CompetitionDTO.class, key);
            if (field == null)
                throw new InvalidArgumentException(key + " is not a valid field for competition");
            field.setAccessible(true);
            ReflectionUtils.setField(field, competitionDTO, value);
        });

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<CompetitionDTO>> violations = validator.validate(competitionDTO);

        List<FieldError> fieldErrors = new ArrayList<>();
        violations.forEach(violation -> {
            fieldErrors.add(new FieldError(
                    "competitionDTO",
                    violation.getPropertyPath().toString(),
                    violation.getMessage()
            ));
        });

        if (!fieldErrors.isEmpty())
            throw new InvalidArgumentsException(fieldErrors);

        return competitionService.update(competitionDTO);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete")
    public void delete(@RequestParam long id) {
        competitionService.delete(id);
    }
}
