package crohnsassistantapi.controller;

import crohnsassistantapi.model.Food;
import crohnsassistantapi.model.Symptom;
import crohnsassistantapi.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("foods")
public class FoodController {
    private final FoodService foods;

    @Autowired
    public FoodController(FoodService foods) {
        this.foods = foods;
    }

    @GetMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void get(@PathVariable("id") String id) {
        foods.get(id);
    }

    @GetMapping(
            path = "{email}/{date}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Page<Food>> get(@PathVariable("email") String email, @PathVariable("date") String date, @RequestParam(name = "page", defaultValue = "0") int page,
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

        return ResponseEntity.of(foods.get(email, date1, page, size, Sort.by(criteria)));
    }

    @GetMapping(
            path = "{email}/{dateStart}/{dateEnd}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Page<Food>> get(@PathVariable("email") String email, @PathVariable("dateStart") String dateStart, @PathVariable("dateEnd") String dateEnd, @RequestParam(name = "page", defaultValue = "0") int page,
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

        return ResponseEntity.of(foods.get(email, date1, date2, page, size, Sort.by(criteria)));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Food> create(@RequestBody Food food) {
        return ResponseEntity.of(foods.create(food));
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Food> update(@RequestBody Food food) {
        return ResponseEntity.of(foods.update(food));
    }

    @DeleteMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Food> delete(@PathVariable("id") String id) {
        return ResponseEntity.of(foods.delete(id));
    }
}
