package crohnsassistantapi.repository;

import crohnsassistantapi.model.Symptom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SymptomRepository extends MongoRepository<Symptom, String> {
}
