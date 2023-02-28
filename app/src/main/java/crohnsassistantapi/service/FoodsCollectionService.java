package crohnsassistantapi.service;

import crohnsassistantapi.model.FoodsCollection;
import crohnsassistantapi.repository.FoodsCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodsCollectionService {
    private final FoodsCollectionRepository foodsCollectionRepository;

    @Autowired
    public FoodsCollectionService(FoodsCollectionRepository foodsCollectionRepository) {
        this.foodsCollectionRepository = foodsCollectionRepository;
    }

    //get all foods
    public Optional<Page<FoodsCollection>> get(int page, int size, Sort sort) {
        Pageable request = PageRequest.of(page, size, sort);
        Page<FoodsCollection> result;

        Example<FoodsCollection> filter = Example.of(new FoodsCollection());
        result = foodsCollectionRepository.findAll(filter, request);

        if(result.isEmpty())
            return Optional.empty();
        /*else result.map(food ->{
            return Optional.of(result);
        });*/

        return Optional.of(result);
    }

    //add a new food to the database
    public FoodsCollection add(FoodsCollection food) {
        return foodsCollectionRepository.save(food);
    }
}
