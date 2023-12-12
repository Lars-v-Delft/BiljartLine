package com.biljartline.billiardsapi.competition;

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

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/competitions")
@RequiredArgsConstructor
public class CompetitionController {
    private final CompetitionService competitionService;
    private final Validator validator;

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
    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public CompetitionDTO patch(@PathVariable long id, @RequestBody JsonPatch patch) {
        try {
            // get existing competition
            CompetitionDTO competitionDTO = competitionService.getById(id);

            // apply patch to existing competition
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode patchedJsonNode = patch.apply(objectMapper.convertValue(competitionDTO, JsonNode.class));
            CompetitionDTO patchedCompetitionDTO = objectMapper.treeToValue(patchedJsonNode, CompetitionDTO.class);

            // validate patched competition
            Set<ConstraintViolation<CompetitionDTO>> violations = validator.validate(patchedCompetitionDTO);
            List<FieldError> fieldErrors = new ArrayList<>();
            violations.forEach(violation ->
                    fieldErrors.add(new FieldError(
                            "competitionDTO",
                            violation.getPropertyPath().toString(),
                            violation.getMessage()
                    )));
            if (!fieldErrors.isEmpty())
                throw new InvalidArgumentsException(fieldErrors);

            // update competition
            return competitionService.update(patchedCompetitionDTO);
        } catch (JsonPatchException e) {
            throw new InvalidArgumentException("Field unknown");
        } catch (JsonProcessingException e) {
            throw new InvalidArgumentException("Value invalid");
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        competitionService.delete(id);
    }
}
