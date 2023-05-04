package crohnsassistantapi.controller;

import crohnsassistantapi.exceptions.NotFoundAttribute;
import crohnsassistantapi.exceptions.RequiredAttribute;
import crohnsassistantapi.model.EiiTeam;
import crohnsassistantapi.model.Professional;
import crohnsassistantapi.service.EiiTeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("eiiteam")
public class EiiTeamController {
    private final EiiTeamService eiiTeamService;

    @Autowired
    public EiiTeamController(EiiTeamService eiiTeamService) {
        this.eiiTeamService = eiiTeamService;
    }

    @GetMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("isAuthenticated()")
    @Operation(
            operationId = "getOneEiiTeam",
            summary = "Get a single EiiTeam details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The EiiTeam details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EiiTeam.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "EiiTeam not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not enough privileges",
                    content = @Content
            )
    })
    public ResponseEntity<EiiTeam> get(@PathVariable("id") String id) {
        try {
            Optional<EiiTeam> result = eiiTeamService.getEiiTeam(id);

            if(result.isPresent()){
                Link self = linkTo(methodOn(EiiTeamController.class).get(id)).withSelfRel();

                return ResponseEntity.ok()
                        .header(HttpHeaders.LINK, self.toString())
                        .body(result.get());
            }

        } catch (NotFoundAttribute | RequiredAttribute e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.notFound().build();
    }


    @GetMapping(
            path = "{id}/professional",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("isAuthenticated()")
    @Operation(
            operationId = "getOneProfessional",
            summary = "Get one Professional"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The Professional details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Professional.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not enough privileges",
                    content = @Content
            )
    })
    public ResponseEntity<Professional> getProfessional(@PathVariable("id") String id) {
        try {
            Optional<Professional> result = eiiTeamService.getProfessional(id);

            if(result.isPresent()){
                Link self = linkTo(methodOn(EiiTeamController.class).getProfessional(id)).withSelfRel();

                return ResponseEntity.ok()
                        .header(HttpHeaders.LINK, self.toString())
                        .body(result.get());
            }

        } catch (NotFoundAttribute e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.notFound().build();
    }
}
