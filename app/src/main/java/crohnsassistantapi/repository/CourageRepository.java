package crohnsassistantapi.repository;

import crohnsassistantapi.model.Courage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourageRepository extends MongoRepository<Courage, String> {
}
