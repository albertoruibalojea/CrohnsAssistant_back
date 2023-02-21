package crohnsassistantapi.repository;

import crohnsassistantapi.model.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecommendationRepository extends MongoRepository<Recommendation, String> {
}
