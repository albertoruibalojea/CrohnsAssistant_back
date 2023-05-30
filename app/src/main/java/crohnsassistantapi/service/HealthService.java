package crohnsassistantapi.service;

import crohnsassistantapi.exceptions.AlreadyExistsAttribute;
import crohnsassistantapi.exceptions.NotFoundAttribute;
import crohnsassistantapi.exceptions.RequiredAttribute;
import crohnsassistantapi.model.CrohnTypes;
import crohnsassistantapi.model.Health;
import crohnsassistantapi.repository.HealthRepository;
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
public class HealthService {

    private final HealthRepository healths;

    private final MongoTemplate mongo;

    @Autowired
    public HealthService(HealthRepository healths, MongoTemplate mongo) {
        this.healths = healths;
        this.mongo = mongo;
    }


    //get health from a specific date to today for a specific user
    public Optional<Page<Health>> get(String email, Date start, int page, int size, Sort sort) {
        Pageable request = PageRequest.of(page, size, sort);
        Page<Health> result;

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
                    mongo.find(query, Health.class),
                    request,
                    () -> mongo.count(query, Health.class)
            );
        } else {
            Example<Health> filter = Example.of(new Health());

            result = healths.findAll(filter, request);
        }

        if (result.isEmpty())
            return Optional.empty();
        /*else result.map(health ->{
            return Optional.of(result);
        });*/

        return Optional.of(result);
    }

    //get health from a specific period of time for a specific user
    public Optional<Page<Health>> get(String email, Date start, Date end, int page, int size, Sort sort) {
        Pageable request = PageRequest.of(page, size, sort);
        Page<Health> result;

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
                    mongo.find(query, Health.class),
                    request,
                    () -> mongo.count(query, Health.class)
            );
        } else {
            Example<Health> filter = Example.of(new Health());

            result = healths.findAll(filter, request);
        }

        if (result.isEmpty())
            return Optional.empty();
        /*else result.map(health ->{
            return Optional.of(result);
        });*/

        return Optional.of(result);
    }

    //get health by id
    public Optional<Health> get(String id) throws NotFoundAttribute {
        if (healths.findById(id).isPresent()) {
            return healths.findById(id);
        } else throw new NotFoundAttribute("Health with ID " + id + " does not exist in database");
    }

    //get health by email and a specific date
    public Optional<Health> get(String email, Date timestamp) {
        List<Criteria> criterios = new ArrayList<>();
        Query query = new Query();

        if (!email.isEmpty()) {
            criterios.add(Criteria.where("user").in(email));
        }
        if (!timestamp.toString().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

            Calendar cal = sdf.getCalendar();
            Date d = new Date(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));

            //timestamp must be the date between the timestamp and today
            criterios.add(Criteria.where("timestamp").gte(d));
        }

        query.addCriteria(new Criteria().andOperator(criterios.toArray(new Criteria[criterios.size()])));

        return Optional.of(mongo.findOne(query, Health.class));
    }

    public Optional<Health> create(Health health) throws AlreadyExistsAttribute, RequiredAttribute {
        //check if food already exists
        if (health.getId() != null && healths.findById(health.getId()).isEmpty()) {
            return checkFieldsHealth(health, true);
        } else throw new AlreadyExistsAttribute("Health with ID" + health.getId() + " already exists in database");
    }

    public Optional<Health> update(Health health) throws RequiredAttribute, NotFoundAttribute {
        if (health.getId() != null && healths.findById(health.getId()).isPresent()) {
            return checkFieldsHealth(health, false);
        } else throw new NotFoundAttribute("Health with ID " + health.getId() + " does not exist");
    }

    public Optional<Health> delete(String id) throws NotFoundAttribute {
        Optional<Health> health = healths.findById(id);

        if (health.isPresent()) {
            healths.delete(health.get());
            return health;
        } else throw new NotFoundAttribute("Health with ID " + id + " does not exist");
    }


    private Optional<Health> checkFieldsHealth(Health health, boolean flag) throws RequiredAttribute {
        if (health.getUser() != null && !health.getUser().isEmpty()) {
            if (health.getTimestamp() != null) {
                if (health.getType() != null && !health.getType().isEmpty()) {
                    //Check if type is correct
                    if(CrohnTypes.fromString(health.getType()) != null){
                        //flag indicates whether the action is create (true) or update (false)
                        if (flag) {
                            health.setDiseaseActive(false);
                            health.setSymptomatology(false);
                        }
                        return Optional.of(healths.save(health));
                    } else throw new RequiredAttribute("Type of disease does not correspond to the correct ones");
                } else throw new RequiredAttribute("Type of disease is required");
            } else throw new RequiredAttribute("Timestamp is required");
        } else throw new RequiredAttribute("User is required");
    }

}
