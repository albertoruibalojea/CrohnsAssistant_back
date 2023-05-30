package crohnsassistantapi.repository;

import crohnsassistantapi.model.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecommendationRepository extends MongoRepository<Recommendation, String> {
    List<Recommendation> findByCategory(String category);
}
