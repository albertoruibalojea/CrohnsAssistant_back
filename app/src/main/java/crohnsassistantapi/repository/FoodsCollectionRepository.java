package crohnsassistantapi.repository;

import crohnsassistantapi.model.FoodsCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FoodsCollectionRepository extends MongoRepository<FoodsCollection, String> {

}
