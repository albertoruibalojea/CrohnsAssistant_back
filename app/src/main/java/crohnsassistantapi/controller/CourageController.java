package crohnsassistantapi.controller;

import crohnsassistantapi.exceptions.NotFoundAttribute;
import crohnsassistantapi.exceptions.RequiredAttribute;
import crohnsassistantapi.model.Courage;
import crohnsassistantapi.service.CourageService;
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
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("courage")
@Tag(name = "Courage Endpoint", description = "Courage related operations")
@SecurityRequirement(name = "JWT")
public class CourageController {

    private final CourageService courageService;


    @Autowired
    public CourageController(CourageService courageService) {
        this.courageService = courageService;
    }


    @GetMapping(
            path = "{email}/{date}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("isAuthenticated()")
    @Operation(
            operationId = "getAllCourages",
            summary = "Get all Courages details for the user from a specific date until today"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The Foods details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Courage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not enough privileges",
                    content = @Content
            )
    })
    ResponseEntity<Page<Courage>> get(@PathVariable("email") String email, @PathVariable("date") String date, @RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "size", defaultValue = "20") int size, @RequestParam(name = "sort", defaultValue = "") List<String> sort) {
        List<Sort.Order> criteria = sort.stream().map(string -> {
                    if(string.equals("ASC") || string.equals("ASC.name") || string.isEmpty()){
                        return Sort.Order.asc("name");
                    } else if (string.equals("DESC") || string.equals("DESC.name")) {
                        return Sort.Order.desc("name");
                    } else if(string.equals("ASC.date")){
                        return Sort.Order.asc("timestamp");
                    } else if (string.equals("DESC.date")) {
                        return Sort.Order.desc("timestamp");
                    } else return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        //cast date param to Date
        LocalDate localDate = LocalDate.parse(date);
        Date date1 = java.sql.Date.valueOf(localDate);

        Optional<Page<Courage>> result = courageService.get(email, date1, page, size, Sort.by(criteria));

        if(result.isPresent()) {
            Page<Courage> data = result.get();
            Pageable metadata = data.getPageable();

            Link self = linkTo(methodOn(CourageController.class).get(email, String.valueOf(date1), page, size, sort)
            ).withSelfRel();

            Link first = linkTo(methodOn(CourageController.class).get(email, String.valueOf(date1), metadata.first().getPageNumber(), size, sort)
            ).withRel(IanaLinkRelations.FIRST);

            Link last = linkTo(methodOn(CourageController.class).get(email, String.valueOf(date1), data.getTotalPages() - 1, size, sort)
            ).withRel(IanaLinkRelations.LAST);

            Link next = linkTo(methodOn(CourageController.class).get(email, String.valueOf(date1), metadata.next().getPageNumber(), size, sort)
            ).withRel(IanaLinkRelations.NEXT);

            Link previous = linkTo(methodOn(CourageController.class).get(email, String.valueOf(date1), metadata.previousOrFirst().getPageNumber(), size, sort)
            ).withRel(IanaLinkRelations.PREVIOUS);

            Link one = linkTo(methodOn(CourageController.class).get(null)
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
            path = "{email}/{dateStart}/{dateEnd}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("isAuthenticated()")
    @Operation(
            operationId = "getAllCourages",
            summary = "Get all Courage details for the user from a specific period of time"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The Courage details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Courage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not enough privileges",
                    content = @Content
            )
    })
    ResponseEntity<Page<Courage>> get(@PathVariable("email") String email, @PathVariable("dateStart") String dateStart, @PathVariable("dateEnd") String dateEnd, @RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "size", defaultValue = "20") int size, @RequestParam(name = "sort", defaultValue = "") List<String> sort) {
        List<Sort.Order> criteria = sort.stream().map(string -> {
                    if(string.equals("ASC") || string.equals("ASC.name") || string.isEmpty()){
                        return Sort.Order.asc("name");
                    } else if (string.equals("DESC") || string.equals("DESC.name")) {
                        return Sort.Order.desc("name");
                    } else if(string.equals("ASC.date")){
                        return Sort.Order.asc("timestamp");
                    } else if (string.equals("DESC.date")) {
                        return Sort.Order.desc("timestamp");
                    } else return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        //cast date param to Date
        LocalDate localDateStart = LocalDate.parse(dateStart);
        Date date1 = java.sql.Date.valueOf(localDateStart);
        LocalDate localDateEnd = LocalDate.parse(dateEnd);
        Date date2 = java.sql.Date.valueOf(localDateEnd);

        Optional<Page<Courage>> result = courageService.get(email, date1, date2, page, size, Sort.by(criteria));

        if(result.isPresent()) {
            Page<Courage> data = result.get();
            Pageable metadata = data.getPageable();

            Link self = linkTo(methodOn(CourageController.class).get(email, String.valueOf(date1), String.valueOf(date2), page, size, sort)
            ).withSelfRel();

            Link first = linkTo(methodOn(CourageController.class).get(email, String.valueOf(date1), String.valueOf(date2), metadata.first().getPageNumber(), size, sort)
            ).withRel(IanaLinkRelations.FIRST);

            Link last = linkTo(methodOn(CourageController.class).get(email, String.valueOf(date1), String.valueOf(date2), data.getTotalPages() - 1, size, sort)
            ).withRel(IanaLinkRelations.LAST);

            Link next = linkTo(methodOn(CourageController.class).get(email, String.valueOf(date1), String.valueOf(date2), metadata.next().getPageNumber(), size, sort)
            ).withRel(IanaLinkRelations.NEXT);

            Link previous = linkTo(methodOn(CourageController.class).get(email, String.valueOf(date1), String.valueOf(date2), metadata.previousOrFirst().getPageNumber(), size, sort)
            ).withRel(IanaLinkRelations.PREVIOUS);

            Link one = linkTo(methodOn(CourageController.class).get(null)
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
    @PreAuthorize("isAuthenticated()")
    @Operation(
            operationId = "getOneCourage",
            summary = "Get one Courage details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The Courage details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Courage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not enough privileges",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Courage not found",
                    content = @Content
            )
    })
    public ResponseEntity<Courage> get(@PathVariable("id") String id) {
        try {
            Optional<Courage> result = courageService.get(id);

            if(result.isPresent()){
                Link self = linkTo(methodOn(CourageController.class).get(id)).withSelfRel();

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
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("isAuthenticated()")
    @Operation(
            operationId = "createCourage",
            summary = "Create a new Courage"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The Courage has been created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Courage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not enough privileges",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content
            )
    })
    public ResponseEntity<Courage> create(@Valid @RequestBody Courage courage) {
        try {
            Optional<Courage> result = courageService.create(courage);

            if(result.isPresent()){
                Link self = linkTo(methodOn(CourageController.class).get(result.get().getId())).withSelfRel();

                return ResponseEntity.ok()
                        .header(HttpHeaders.LINK, self.toString())
                        .body(result.get());
            }

        } catch (RequiredAttribute | NotFoundAttribute message) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.badRequest().build();
    }
}
