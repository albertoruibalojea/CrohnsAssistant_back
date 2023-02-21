package crohnsassistantapi.repository;

import crohnsassistantapi.model.Food;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FoodRepository extends MongoRepository<Food, String> {
}
