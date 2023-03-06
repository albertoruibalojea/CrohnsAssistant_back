package crohnsassistantapi.controller;

import crohnsassistantapi.model.Symptom;
import crohnsassistantapi.service.SymptomService;
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
@RequestMapping("symptoms")
public class SymptomController {
    private final SymptomService symptomService;

    @Autowired
    public SymptomController(SymptomService symptomService) {
        this.symptomService = symptomService;
    }

    @GetMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Symptom> get(@PathVariable("id") String id) {
        return ResponseEntity.of(symptomService.get(id));
    }

    @GetMapping(
            path = "{email}/{date}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Page<Symptom>> get(@PathVariable("email") String email, @PathVariable("date") String date, @RequestParam(name = "page", defaultValue = "0") int page,
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

        return ResponseEntity.of(symptomService.get(email, date1, page, size, Sort.by(criteria)));
    }

    @GetMapping(
            path = "{email}/{dateStart}/{dateEnd}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Page<Symptom>> get(@PathVariable("email") String email, @PathVariable("dateStart") String dateStart, @PathVariable("dateEnd") String dateEnd, @RequestParam(name = "page", defaultValue = "0") int page,
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

        return ResponseEntity.of(symptomService.get(email, date1, date2, page, size, Sort.by(criteria)));
    }

    @PostMapping(
            //path="{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Symptom> create(@RequestBody Symptom symptom){
        return ResponseEntity.of(symptomService.create(symptom));
    }

    @PutMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Symptom> update(@PathVariable("id") String id, @RequestBody Symptom symptom) {
        return ResponseEntity.of(symptomService.update(symptom));
    }

    @DeleteMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Symptom> delete(@PathVariable("id") String id) {
        return ResponseEntity.of(symptomService.delete(id));
    }
}
