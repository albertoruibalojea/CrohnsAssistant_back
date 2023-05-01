package crohnsassistantapi.service;

import crohnsassistantapi.exceptions.NotFoundAttribute;
import crohnsassistantapi.exceptions.RequiredAttribute;
import crohnsassistantapi.model.Courage;
import crohnsassistantapi.repository.CourageRepository;
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
public class CourageService {
    private final CourageRepository courage;
    private final MongoTemplate mongo;

    @Autowired
    public CourageService(CourageRepository courage, MongoTemplate mongo) {
        this.courage = courage;
        this.mongo = mongo;
    }

    //get courage from a specific date to today for a specific user
    public Optional<Page<Courage>> get(String email, Date start, int page, int size, Sort sort) {
        Pageable request = PageRequest.of(page, size, sort);
        Page<Courage> result;

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
                    mongo.find(query, Courage.class),
                    request,
                    () -> mongo.count(query, Courage.class)
            );
        } else {
            Example<Courage> filter = Example.of(new Courage());
            result = courage.findAll(filter, request);
        }

        if(result.isEmpty())
            return Optional.empty();

        return Optional.of(result);
    }


    //get courage from a specific period of time for a specific user
    public Optional<Page<Courage>> get(String email, Date start, Date end, int page, int size, Sort sort) {
        Pageable request = PageRequest.of(page, size, sort);
        Page<Courage> result;

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
                    mongo.find(query, Courage.class),
                    request,
                    () -> mongo.count(query, Courage.class)
            );
        } else {
            Example<Courage> filter = Example.of(new Courage());
            result = courage.findAll(filter, request);
        }

        if(result.isEmpty())
            return Optional.empty();

        return Optional.of(result);
    }


    //get one courage by id
    public Optional<Courage> get(String id) throws NotFoundAttribute {
        if(courage.findById(id).isPresent()) {
            return courage.findById(id);
        } else throw new NotFoundAttribute("Courage with ID " + id + " not found");
    }


    //create a new courage
    public Optional<Courage> create(Courage courage) throws NotFoundAttribute, RequiredAttribute {
        if(courage.getId() != null && this.courage.findById(courage.getId()).isEmpty()) {
            if(courage.getLevel() != null && courage.getLevel() >= 0 && courage.getLevel() <= 4) {
                if(courage.getActivities() != null){
                    return Optional.of(this.courage.save(courage));
                } else throw new RequiredAttribute("Activities cannot be null. It must be empty or have at least one activity");
            } else throw new RequiredAttribute("Courage level must be between 0 and 4");
        } else throw new NotFoundAttribute("Courage with ID " + courage.getId() + "already exists");
    }
}
