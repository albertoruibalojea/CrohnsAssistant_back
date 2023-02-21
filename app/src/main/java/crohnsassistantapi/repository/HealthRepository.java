package crohnsassistantapi.repository;

import crohnsassistantapi.model.Health;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HealthRepository extends MongoRepository<Health, String> {
}
