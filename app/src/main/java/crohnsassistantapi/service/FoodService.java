package crohnsassistantapi.service;

import crohnsassistantapi.exceptions.AlreadyExistsAttribute;
import crohnsassistantapi.exceptions.NotFoundAttribute;
import crohnsassistantapi.exceptions.RequiredAttribute;
import crohnsassistantapi.model.Food;
import crohnsassistantapi.model.FoodsCollection;
import crohnsassistantapi.repository.FoodRepository;
import crohnsassistantapi.repository.FoodsCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FoodService {

    private final FoodRepository foods;
    private final FoodsCollectionRepository foodsCollectionRepository;
    private final MongoTemplate mongo;



    //forbidden



    @Autowired
    public FoodService(FoodRepository foods, FoodsCollectionRepository foodsCollectionRepository, MongoTemplate mongo) {
        this.foods = foods;
        this.foodsCollectionRepository = foodsCollectionRepository;
        this.mongo = mongo;
    }

    //get foods from a specific date to today for a specific user
    public Optional<Page<Food>> get(String email, Date start, int page, int size, Sort sort) {
        Pageable request = PageRequest.of(page, size, sort);
        Page<Food> result;

        if (email != null && start != null) {
            List<Criteria> criterios = new ArrayList<>();
            Query query = new Query();

            if (!email.isEmpty()) {
                criterios.add(Criteria.where("user").in(email));
            }
            if (!start.toString().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

                Calendar cal = sdf.getCalendar();
                Date d = new Date(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));

                //start must be the date between the timestamp and today
                criterios.add(Criteria.where("timestamp").gte(d));
            }

            query.addCriteria(new Criteria().andOperator(criterios.toArray(new Criteria[criterios.size()])));

            result = PageableExecutionUtils.getPage(
                    mongo.find(query, Food.class),
                    request,
                    () -> mongo.count(query, Food.class)
            );
        } else {
            Example<Food> filter = Example.of(new Food());

            result = foods.findAll(filter, request);
        }

        if(result.isEmpty())
            return Optional.empty();
        /*else result.map(food ->{
            return Optional.of(result);
        });*/

        return Optional.of(result);
    }


    //get foods from a specific period of time for a specific user
    public Optional<Page<Food>> get(String email, Date start, Date end, int page, int size, Sort sort) {
        Pageable request = PageRequest.of(page, size, sort);
        Page<Food> result;

        if (email != null && start != null && end != null) {
            List<Criteria> criterios = new ArrayList<>();
            Query query = new Query();

            if (!email.isEmpty()) {
                criterios.add(Criteria.where("user").in(email));
            }
            if (!start.toString().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

                Calendar cal = sdf.getCalendar();
                Date d = new Date(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));

                //start must be the date between the timestamp and today
                criterios.add(Criteria.where("timestamp").gte(d));
            }
            if (!end.toString().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

                Calendar cal = sdf.getCalendar();
                Date d = new Date(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));

                //end must be the date between the timestamp and today
                criterios.add(Criteria.where("timestamp").lte(d));
            }

            query.addCriteria(new Criteria().andOperator(criterios.toArray(new Criteria[criterios.size()])));

            result = PageableExecutionUtils.getPage(
                    mongo.find(query, Food.class),
                    request,
                    () -> mongo.count(query, Food.class)
            );
        } else {
            Example<Food> filter = Example.of(new Food());

            result = foods.findAll(filter, request);
        }

        if(result.isEmpty())
            return Optional.empty();
        /*else result.map(health ->{
            return Optional.of(result);
        });*/

        return Optional.of(result);
    }


    //get one food by id
    public Optional<Food> get(String id) throws NotFoundAttribute {
        if(foods.findById(id).isPresent()){
            return foods.findById(id);
        } else throw new NotFoundAttribute("Food with ID" + id + " does not exist in database");
    }


    //create a new food
    public Optional<Food> create(Food food) throws AlreadyExistsAttribute, RequiredAttribute {
        //check if food already exists
        if (food.getId() != null && foods.findById(food.getId()).isPresent()) {
            throw new AlreadyExistsAttribute("Food with ID" + food.getId() + " already exists in database");
        } else {
            if(food.getUser() != null && !food.getUser().isEmpty()){
                if(food.getName() == null || !food.getName().isEmpty()){
                    if(food.getTimestamp() != null){
                        return Optional.of(foods.insert(food));
                    } else throw new RequiredAttribute("Timestamp is required");
                } else throw new RequiredAttribute("Name of food is required");
            } else throw new RequiredAttribute("User is required");
        }
    }

    //update a food
    public Optional<Food> update(Food food) throws RequiredAttribute, NotFoundAttribute {
        if (food.getId() != null && foods.findById(food.getId()).isEmpty()) {
            if (food.getUser() != null && !food.getUser().isEmpty()) {
                if (food.getTimestamp() != null) {
                    if (food.getName() != null && !food.getName().isEmpty()) {
                        return Optional.of(foods.save(food));
                    } else throw new RequiredAttribute("Name of food is empty");
                } else throw new RequiredAttribute("Timestamp is empty");
            } else throw new RequiredAttribute("User is empty");
        } else throw new NotFoundAttribute("Food with ID" + food.getId() + " does not exist in database");
    }

    //delete a food
    public Optional<Food> delete(String id) throws NotFoundAttribute {
        Optional<Food> food = foods.findById(id);

        if (food.isPresent()) {
            foods.delete(food.get());
            return food;
        } else throw new NotFoundAttribute("Food with ID" + id + " does not exist in database");
    }


    //FoodsCollection
    //get all foods
    public Optional<Page<FoodsCollection>> getCollection(int page, int size, Sort sort) {
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

    public Optional<FoodsCollection> getCollection(String id) throws NotFoundAttribute {
        if(foodsCollectionRepository.findById(id).isPresent()){
            return foodsCollectionRepository.findById(id);
        } else throw new NotFoundAttribute("Food with ID" + id + " does not exist in database");
    }

    //add a new food to the database
    public Optional<FoodsCollection> create(FoodsCollection food) throws RequiredAttribute {
        if(!food.getName().isEmpty()){
            return Optional.of(foodsCollectionRepository.insert(food));
        } else throw new RequiredAttribute("Food name cannot be empty");
    }
}
