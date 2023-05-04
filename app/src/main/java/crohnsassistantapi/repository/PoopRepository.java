package crohnsassistantapi.repository;

import crohnsassistantapi.model.Poop;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.Optional;

public interface PoopRepository extends MongoRepository<Poop, String> {
    Optional<Poop> findByUserAndTimestamp(String user, Date date);
}
