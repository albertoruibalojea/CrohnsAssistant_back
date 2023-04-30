package crohnsassistantapi.controller;

import crohnsassistantapi.exceptions.NotFoundAttribute;
import crohnsassistantapi.model.Food;
import crohnsassistantapi.model.Professional;
import crohnsassistantapi.service.ProfessionalService;
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
@RequestMapping("professionals")
public class ProfessionalController {
    private final ProfessionalService professionalService;

    @Autowired
    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    @GetMapping(
            path = "{id}",
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
    public ResponseEntity<Professional> get(@PathVariable("id") String id) {
        try {
            Optional<Professional> result = professionalService.get(id);

            if(result.isPresent()){
                Link self = linkTo(methodOn(ProfessionalController.class).get(id)).withSelfRel();

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
