package crohnsassistantapi.service;

import crohnsassistantapi.exceptions.AlreadyExistsAttribute;
import crohnsassistantapi.exceptions.NotFoundAttribute;
import crohnsassistantapi.exceptions.RequiredAttribute;
import crohnsassistantapi.model.Colors;
import crohnsassistantapi.model.Poop;
import crohnsassistantapi.model.Symptom;
import crohnsassistantapi.repository.PoopRepository;
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
public class PoopService {

    private final PoopRepository poops;
    private final MongoTemplate mongo;

    @Autowired
    public PoopService(PoopRepository poops, MongoTemplate mongo) {
        this.poops = poops;
        this.mongo = mongo;
    }

    //get methods
    public Optional<Poop> get(String id) throws NotFoundAttribute {
        if(poops.findById(id).isPresent()){
            return poops.findById(id);
        } else throw new NotFoundAttribute("Poop with ID " + id + " does not exist");
    }

    //get poops from a specific date to today for a specific user
    public Optional<Page<Poop>> get(String email, Date start, int page, int size, Sort sort) {
        Pageable request = PageRequest.of(page, size, sort);
        Page<Poop> result;

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
                    mongo.find(query, Poop.class),
                    request,
                    () -> mongo.count(query, Poop.class)
            );
        } else {
            Example<Poop> filter = Example.of(new Poop());

            result = poops.findAll(filter, request);
        }

        if(result.isEmpty())
            return Optional.empty();
        /*else result.map(symptom ->{
            return Optional.of(result);
        });*/

        return Optional.of(result);
    }

    //get poops from a specific period of time for a specific user
    public Optional<Page<Poop>> get(String email, Date start, Date end, int page, int size, Sort sort) {
        Pageable request = PageRequest.of(page, size, sort);
        Page<Poop> result;

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
                    mongo.find(query, Poop.class),
                    request,
                    () -> mongo.count(query, Poop.class)
            );
        } else {
            Example<Poop> filter = Example.of(new Poop());

            result = poops.findAll(filter, request);
        }

        if(result.isEmpty())
            return Optional.empty();
        /*else result.map(symptom ->{
            return Optional.of(result);
        });*/

        return Optional.of(result);
    }


    //create poop
    public Optional<Poop> create(Poop poop) throws AlreadyExistsAttribute, RequiredAttribute {
        if(poop.getId() != null || poops.findById(poop.getId()).isEmpty()){
            return checkFieldsPoop(poop);
        } else throw new AlreadyExistsAttribute("Poop with ID " + poop.getId() + " already exists");
    }

    //update poop
    public Optional<Poop> update(Poop poop) throws RequiredAttribute, NotFoundAttribute {
        if(poop.getId() != null || poops.findById(poop.getId()).isPresent()){
            return checkFieldsPoop(poop);
        } else throw new NotFoundAttribute("Poop with ID " + poop.getId() + " does not exist");
    }

    //delete poop
    public Optional<Poop> delete(String id) throws NotFoundAttribute {
        Optional<Poop> poop = poops.findById(id);

        if(poop.isPresent()){
            poops.delete(poop.get());
            return poop;
        } else throw new NotFoundAttribute("Poop with ID " + id + " does not exist");
    }


    private Optional<Poop> checkFieldsPoop(Poop poop) throws RequiredAttribute {
        if(!poop.getUser().isEmpty() && poop.getTimestamp() != null){
            if(poop.getType() >= 1 && poop.getType() <= 7){
                if(poop.getWeight() >= 1 && poop.getWeight() <= 6){
                    try {
                        Colors.valueOf(poop.getColor());
                        //we are not checking the booleans because they are false by default
                        return Optional.of(poops.save(poop));
                    } catch (Exception e) {
                        throw new RequiredAttribute("Color is required and must be one of the Color enum");
                    }

                } else throw new RequiredAttribute("Weight is required and must be between 1 and 6");
            } else throw new RequiredAttribute("Type is required and must be between 0 and 7");
        } else throw new RequiredAttribute("User and timestamp are required");
    }
}
