package crohnsassistantapi.service;

import crohnsassistantapi.model.Food;
import crohnsassistantapi.model.Symptom;
import crohnsassistantapi.model.SymptomTypes;
import crohnsassistantapi.repository.FoodRepository;
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
    private final MongoTemplate mongo;

    @Autowired
    public FoodService(FoodRepository foods, MongoTemplate mongo) {
        this.foods = foods;
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
        /*else result.map(symptom ->{
            return Optional.of(result);
        });*/

        return Optional.of(result);
    }

    public Optional<Food> get(String id) {
        return foods.findById(id);
    }

    public Optional<Food> create(Food food) {
        //check if food already exists
        if (food.getId() != null && foods.findById(food.getId()).isPresent()) {
            throw new IllegalArgumentException("Food already exists");
        } else {
            if(food.getUser() != null && !food.getUser().isEmpty()){
                if(food.getName() == null || !food.getName().isEmpty()){
                    if(food.getTimestamp() != null){
                        return Optional.of(foods.save(food));
                    } else throw new IllegalArgumentException("Timestamp is required");
                } else throw new IllegalArgumentException("Name is required");
            } else throw new IllegalArgumentException("User is required");
        }
    }

    public Optional<Food> update(Food food) {
        if (food.getId() != null && foods.findById(food.getId()).isPresent()) {
            if (food.getUser() != null && !food.getUser().isEmpty()) {
                if (food.getTimestamp() != null) {
                    if (food.getName() != null && !food.getName().isEmpty()) {
                        return Optional.of(foods.save(food));
                    } else throw new IllegalArgumentException("Name is empty");
                } else throw new IllegalArgumentException("Timestamp is empty");
            } else throw new IllegalArgumentException("User is empty");
        } else throw new IllegalArgumentException("Food doesn´t exist");
    }

    public Optional<Food> delete(String id) {
        Optional<Food> food = foods.findById(id);
        if (food.isPresent()) {
            foods.delete(food.get());
            return food;
        } else throw new IllegalArgumentException("Food doesn´t exist");
    }
}
