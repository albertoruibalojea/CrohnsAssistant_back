package crohnsassistantapi.service;

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

    public Optional<Symptom> get(String id) {
        return symptoms.findById(id);
    }

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

    public Optional<Symptom> create(Symptom symptom) {
        if (symptom.getId() != null && symptoms.findById(symptom.getId()).isPresent()) {
            throw new IllegalArgumentException("Symptom already exists");
        } else {
            if (symptom.getUser() != null && !symptom.getUser().isEmpty()) {
                if (symptom.getTimestamp() != null) {
                    if (symptom.getName() != null && !symptom.getName().isEmpty()) {
                        //we must compare if the symptom name is valid (it must be one of the SymptomTypes enum values)
                        if(SymptomTypes.fromString(symptom.getName()) != null){
                            return Optional.of(symptoms.save(symptom));
                        }
                        else throw new IllegalArgumentException("Name is not valid");
                    } else throw new IllegalArgumentException("Name is empty");
                } else throw new IllegalArgumentException("Timestamp is empty");
            } else throw new IllegalArgumentException("User is empty");
        }
    }

    public Optional<Symptom> update(Symptom symptom) {
        if (symptom.getId() != null && symptoms.findById(symptom.getId()).isPresent()) {
            if (symptom.getUser() != null && !symptom.getUser().isEmpty()) {
                if (symptom.getTimestamp() != null) {
                    if (symptom.getName() != null && !symptom.getName().isEmpty()) {
                        //we must compare if the symptom name is valid (it must be one of the SymptomTypes enum values)
                        if(SymptomTypes.fromString(symptom.getName()) != null){
                            return Optional.of(symptoms.save(symptom));
                        }
                        else throw new IllegalArgumentException("Name is not valid");
                    } else throw new IllegalArgumentException("Name is empty");
                } else throw new IllegalArgumentException("Timestamp is empty");
            } else throw new IllegalArgumentException("User is empty");
        } else throw new IllegalArgumentException("Symptom doesn´t exist");
    }

    public Optional<Symptom> delete(String id) {
        Optional<Symptom> symptom = symptoms.findById(id);
        if (symptom.isPresent()) {
            symptoms.delete(symptom.get());
            return symptom;
        } else throw new IllegalArgumentException("Symptom doesn´t exist");
    }

}
