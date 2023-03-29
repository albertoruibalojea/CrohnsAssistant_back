package crohnsassistantapi.service;

import crohnsassistantapi.exceptions.NotFoundAttribute;
import crohnsassistantapi.exceptions.RequiredAttribute;
import crohnsassistantapi.model.Food;
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

    public Optional<FoodsCollection> get(String id) throws NotFoundAttribute {
        if(foodsCollectionRepository.findById(id).isPresent()){
            return foodsCollectionRepository.findById(id);
        } else throw new NotFoundAttribute("Food does not exists in database");
    }

    //add a new food to the database
    public FoodsCollection add(FoodsCollection food) throws RequiredAttribute {
        if(!food.getName().isEmpty()){
            foodsCollectionRepository.insert(food);
            return food;
        } else throw new RequiredAttribute("Food name cannot be empty");
    }
}
