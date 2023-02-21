package crohnsassistantapi.repository;

import crohnsassistantapi.model.Poop;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PoopRepository extends MongoRepository<Poop, String> {
}
