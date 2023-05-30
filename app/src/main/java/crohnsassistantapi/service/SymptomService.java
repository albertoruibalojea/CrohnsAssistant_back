package crohnsassistantapi.service;

import crohnsassistantapi.exceptions.AlreadyExistsAttribute;
import crohnsassistantapi.exceptions.ModifiedAttribute;
import crohnsassistantapi.exceptions.NotFoundAttribute;
import crohnsassistantapi.exceptions.RequiredAttribute;
import crohnsassistantapi.model.Symptom;
import crohnsassistantapi.model.SymptomTypes;
import crohnsassistantapi.repository.SymptomRepository;
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
public class SymptomService {
    private final SymptomRepository symptoms;
    private final MongoTemplate mongo;

    @Autowired
    public SymptomService(SymptomRepository symptoms, MongoTemplate mongo) {
        this.symptoms = symptoms;
        this.mongo = mongo;
    }

    //get symptoms from a specific date to today for a specific user
    public Optional<Page<Symptom>> get(String email, Date start, int page, int size, Sort sort) {
        Pageable request = PageRequest.of(page, size, sort);
        Page<Symptom> result;

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
                    mongo.find(query, Symptom.class),
                    request,
                    () -> mongo.count(query, Symptom.class)
            );
        } else {
            Example<Symptom> filter = Example.of(new Symptom());

            result = symptoms.findAll(filter, request);
        }

        if(result.isEmpty())
            return Optional.empty();
        /*else result.map(symptom ->{
            return Optional.of(result);
        });*/

        return Optional.of(result);
    }

    //get symptoms from a specific period of time for a specific user
    public Optional<Page<Symptom>> get(String email, Date start, Date end, int page, int size, Sort sort) {
        Pageable request = PageRequest.of(page, size, sort);
        Page<Symptom> result;

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
                    mongo.find(query, Symptom.class),
                    request,
                    () -> mongo.count(query, Symptom.class)
            );
        } else {
            Example<Symptom> filter = Example.of(new Symptom());

            result = symptoms.findAll(filter, request);
        }

        if(result.isEmpty())
            return Optional.empty();
        /*else result.map(symptom ->{
            return Optional.of(result);
        });*/

        return Optional.of(result);
    }

    //get all symptoms for a specific user
    public Optional<Page<Symptom>> get(String email, String symptom){
        Pageable request = PageRequest.of(0, 20, null);
        Page<Symptom> result;

        if (email != null && symptom != null) {
            List<Criteria> criterios = new ArrayList<>();
            Query query = new Query();

            if (!email.isEmpty()) {
                criterios.add(Criteria.where("user").in(email));
                criterios.add(Criteria.where("name").in(symptom));
            }

            query.addCriteria(new Criteria().andOperator(criterios.toArray(new Criteria[criterios.size()])));

            result = PageableExecutionUtils.getPage(
                    mongo.find(query, Symptom.class),
                    request,
                    () -> mongo.count(query, Symptom.class)
            );
        } else {
            Example<Symptom> filter = Example.of(new Symptom());

            result = symptoms.findAll(filter, request);
        }

        if(result.isEmpty())
            return Optional.empty();
        /*else result.map(symptom ->{
            return Optional.of(result);
        });*/

        return Optional.of(result);
    }

    //get one symptom by ID
    public Optional<Symptom> get(String id) throws NotFoundAttribute {
        if(symptoms.findById(id).isPresent()){
            return symptoms.findById(id);
        } else throw new NotFoundAttribute("The symptom with ID " + id + " does not exist in database");
    }


    //create a new symptom
    public Optional<Symptom> create(Symptom symptom) throws ModifiedAttribute, RequiredAttribute, AlreadyExistsAttribute {
        //check if symptom already exists
        if (symptom.getId() != null && symptoms.findById(symptom.getId()).isEmpty()) {
            return checkFieldsSymptom(symptom);
        } else throw new AlreadyExistsAttribute("Symptom with ID" + symptom.getId() + " already exists in database");
    }

    //update a symptom
    public Optional<Symptom> update(Symptom symptom) throws NotFoundAttribute, RequiredAttribute, ModifiedAttribute {
        if (symptom.getId() != null && symptoms.findById(symptom.getId()).isPresent()) {
            return checkFieldsSymptom(symptom);
        } else throw new NotFoundAttribute("Symptom with ID " + symptom.getId() + " does not exist");
    }

    //delete a symptom
    public Optional<Symptom> delete(String id) throws NotFoundAttribute {
        Optional<Symptom> symptom = symptoms.findById(id);

        if (symptom.isPresent()) {
            symptoms.delete(symptom.get());
            return symptom;
        } else throw new NotFoundAttribute("Symptom with ID " + id + " does not exist");
    }


    private Optional<Symptom> checkFieldsSymptom(Symptom symptom) throws ModifiedAttribute, RequiredAttribute {
        if (symptom.getUser() != null && !symptom.getUser().isEmpty()) {
            if (symptom.getTimestamp() != null) {
                if (symptom.getName() != null && !symptom.getName().isEmpty()) {
                    //we must compare if the symptom name is valid (it must be one of the SymptomTypes enum values)
                    if(SymptomTypes.fromString(symptom.getName()) != null){
                        symptom.setName(Objects.requireNonNull(SymptomTypes.fromString(symptom.getName())).getName());
                        return Optional.of(symptoms.save(symptom));
                    } else throw new ModifiedAttribute("Symptom name is not one of the valid ones");
                } else throw new RequiredAttribute("Symptom name is required");
            } else throw new RequiredAttribute("Timestamp is required");
        } else throw new RequiredAttribute("User is required");
    }
}
