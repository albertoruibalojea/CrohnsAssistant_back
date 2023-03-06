package crohnsassistantapi.service;

import crohnsassistantapi.model.Recommendation;
import crohnsassistantapi.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {
    private final RecommendationRepository recommendations;

    @Autowired
    public RecommendationService(RecommendationRepository recommendations) {
        this.recommendations = recommendations;
    }

    //get all recommendations
    public Optional<List<Recommendation>> getAll() {
        return Optional.of(recommendations.findAll());
    }

    //get all recommendations by category
    public Optional<List<Recommendation>> getByCategory(String category) {
        return Optional.of(recommendations.findByCategory(category));
    }

    //get a specific recommendation
    public Optional<Recommendation> get(String id) {
        return Optional.of(recommendations.findById(id).get());
    }
}
