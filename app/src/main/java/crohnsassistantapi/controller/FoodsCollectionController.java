package crohnsassistantapi.controller;

import crohnsassistantapi.exceptions.NotFoundAttribute;
import crohnsassistantapi.exceptions.RequiredAttribute;
import crohnsassistantapi.model.FoodsCollection;
import crohnsassistantapi.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("foodsCollection")
@Tag(name = "Foods Collection Endpoint", description = "Foods Collection related operations")
@SecurityRequirement(name = "JWT")
public class FoodsCollectionController {
    private final FoodService foodsCollection;

    @Autowired
    public FoodsCollectionController(FoodService foodsCollection) {
        this.foodsCollection = foodsCollection;
    }

    @GetMapping(
            produces = "application/json"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            operationId = "getAllFoodsCollection",
            summary = "Get all FoodCollection details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The FoodCollections details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FoodsCollection.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not enough privileges",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "FoodCollection not found",
                    content = @Content
            )
    })
    ResponseEntity<Page<FoodsCollection>> get(@RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "20") int size, @RequestParam(name = "sort", defaultValue = "") List<String> sort) throws NotFoundAttribute {

        List<Sort.Order> criteria = sort.stream().map(string -> {
                    if(string.equals("ASC") || string.equals("ASC.name") || string.isEmpty()){
                        return Sort.Order.asc("name");
                    } else if (string.equals("DESC") || string.equals("DESC.name")) {
                        return Sort.Order.desc("name");
                    } else return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Optional<Page<FoodsCollection>> result = foodsCollection.getCollection(page, size, Sort.by(criteria));

        if(result.isPresent()) {
            Page<FoodsCollection> data = result.get();
            Pageable metadata = data.getPageable();

            Link self = linkTo(methodOn(FoodsCollectionController.class).get(page, size, sort)
            ).withSelfRel();

            Link first = linkTo(methodOn(FoodsCollectionController.class).get(metadata.first().getPageNumber(), size, sort)
            ).withRel(IanaLinkRelations.FIRST);

            Link last = linkTo(methodOn(FoodsCollectionController.class).get(data.getTotalPages() - 1, size, sort)
            ).withRel(IanaLinkRelations.LAST);

            Link next = linkTo(methodOn(FoodsCollectionController.class).get(metadata.next().getPageNumber(), size, sort)
            ).withRel(IanaLinkRelations.NEXT);

            Link previous = linkTo(methodOn(FoodsCollectionController.class).get(metadata.previousOrFirst().getPageNumber(), size, sort)
            ).withRel(IanaLinkRelations.PREVIOUS);

            Link one = linkTo(methodOn(FoodsCollectionController.class).get(null)
            ).withRel(IanaLinkRelations.CURRENT);

            return ResponseEntity.ok()
                    .header(HttpHeaders.LINK, self.toString())
                    .header(HttpHeaders.LINK, first.toString())
                    .header(HttpHeaders.LINK, last.toString())
                    .header(HttpHeaders.LINK, next.toString())
                    .header(HttpHeaders.LINK, previous.toString())
                    .header(HttpHeaders.LINK, one.toString())
                    .body(result.get());
        }

        return ResponseEntity.notFound().build();
    }


    @GetMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            operationId = "getOneFoodsCollection",
            summary = "Get one FoodCollection details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The FoodCollection details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FoodsCollection.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not enough privileges",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "FoodCollection not found",
                    content = @Content
            )
    })
    ResponseEntity<FoodsCollection> get(@PathVariable("id") String food) throws NotFoundAttribute {
        try {
            Optional<FoodsCollection> result = foodsCollection.getCollection(food);

            if(result.isPresent()){
                Link self = linkTo(methodOn(FoodsCollectionController.class).get(food)).withSelfRel();

                return ResponseEntity.ok()
                        .header(HttpHeaders.LINK, self.toString())
                        .body(result.get());
            }

        } catch (NotFoundAttribute e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.notFound().build();
    }


    @PostMapping(
            consumes = "application/json",
            produces = "application/json"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            operationId = "addFoodsCollection",
            summary = "Creates a new FoodsCollection"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The FoodsCollection details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FoodsCollection.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not enough privileges",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request: you must set at least the name",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict: a FoodsCollection with this name already exists",
                    content = @Content
            )
    })
    public ResponseEntity<FoodsCollection> create(@RequestBody @Valid FoodsCollection food) throws RequiredAttribute {
        try {
            Optional<FoodsCollection> result = foodsCollection.create(food);

            if(result.isPresent()) {
                Link self = linkTo(methodOn(FoodsCollectionController.class).create(food)).withSelfRel();

                return ResponseEntity.ok()
                        .header(HttpHeaders.LINK, self.toString())
                        .body(result.get());
            }
        } catch (RequiredAttribute message) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.notFound().build();
    }
}
