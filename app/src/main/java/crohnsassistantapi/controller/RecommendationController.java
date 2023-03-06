package crohnsassistantapi.controller;

import crohnsassistantapi.model.Recommendation;
import crohnsassistantapi.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping(
            path = "all",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Optional<List<Recommendation>> getAll() {
        return recommendationService.getAll();
    }

    @GetMapping(
            path = "{category}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Optional<List<Recommendation>> getByCategory(String category) {
        return recommendationService.getByCategory(category);
    }

    @GetMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Optional<Recommendation> get(String id) {
        return recommendationService.get(id);
    }
}
