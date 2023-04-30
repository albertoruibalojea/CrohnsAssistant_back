package crohnsassistantapi.controller;

import crohnsassistantapi.exceptions.NotFoundAttribute;
import crohnsassistantapi.model.Food;
import crohnsassistantapi.model.Recommendation;
import crohnsassistantapi.service.RecommendationService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping(
            path = "{category}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("isAuthenticated()")
    @Operation(
            operationId = "getOneCategory",
            summary = "Get one Recommendation category details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The Recommendations details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Recommendation.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not enough privileges",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recommendation not found",
                    content = @Content
            )
    })
    public ResponseEntity<List<Recommendation>> getByCategory(String category) {
        try {
            Optional<List<Recommendation>> result = recommendationService.getByCategory(category);

            if(result.isPresent()){

                return ResponseEntity.ok()
                        .header(HttpHeaders.LINK,
                                linkTo(methodOn(RecommendationController.class).getByCategory(category)).withSelfRel().toString())
                        .body(result.get());
            }

        } catch (NotFoundAttribute e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("isAuthenticated()")
    @Operation(
            operationId = "getOneRecommendation",
            summary = "Get one Recommendation category details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The Recommendations details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Recommendation.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not enough privileges",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recommendation not found",
                    content = @Content
            )
    })
    public ResponseEntity<Recommendation> get(String id) {
        try {
            Optional<Recommendation> result = recommendationService.get(id);

            if(result.isPresent()){
                Link self = linkTo(methodOn(FoodsCollectionController.class).get(id)).withSelfRel();

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
