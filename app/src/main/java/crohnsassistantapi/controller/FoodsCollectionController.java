package crohnsassistantapi.controller;

import crohnsassistantapi.model.FoodsCollection;
import crohnsassistantapi.service.FoodsCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("foodsCollection")
public class FoodsCollectionController {
    private final FoodsCollectionService foodsCollection;

    @Autowired
    public FoodsCollectionController(FoodsCollectionService foodsCollection) {
        this.foodsCollection = foodsCollection;
    }

    @GetMapping(
            path = "all",
            produces = "application/json"
    )
    ResponseEntity<Optional<Page<FoodsCollection>>> get(@RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "20") int size, @RequestParam(name = "sort", defaultValue = "") List<String> sort) {

        List<Sort.Order> criteria = sort.stream().map(string -> {
                    if(string.equals("ASC") || string.equals("ASC.name") || string.isEmpty()){
                        return Sort.Order.asc("name");
                    } else if (string.equals("DESC") || string.equals("DESC.name")) {
                        return Sort.Order.desc("name");
                    } else return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return ResponseEntity.ok(foodsCollection.get(page, size, Sort.by(criteria)));
    }

    @PostMapping(
            path = "add",
            consumes = "application/json",
            produces = "application/json"
    )
    ResponseEntity<FoodsCollection> add(@RequestBody FoodsCollection foodsCollection) {
        return ResponseEntity.ok(this.foodsCollection.add(foodsCollection));
    }
}
