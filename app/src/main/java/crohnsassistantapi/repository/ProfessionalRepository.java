package crohnsassistantapi.repository;

import crohnsassistantapi.model.Professional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfessionalRepository extends MongoRepository<Professional, String> {
}
