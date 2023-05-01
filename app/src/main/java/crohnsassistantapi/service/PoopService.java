package crohnsassistantapi.service;

import crohnsassistantapi.exceptions.AlreadyExistsAttribute;
import crohnsassistantapi.exceptions.RequiredAttribute;
import crohnsassistantapi.model.Colors;
import crohnsassistantapi.model.Poop;
import crohnsassistantapi.repository.PoopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    //create poop
    public Optional<Poop> create(Poop poop) throws AlreadyExistsAttribute, RequiredAttribute {
        if(poop.getId() != null || poops.findById(poop.getId()).isEmpty()){
            return checkFieldsPoop(poop);
        } else throw new AlreadyExistsAttribute("Poop with ID " + poop.getId() + " already exists");
    }

    //update poop
    public Optional<Poop> update(Poop poop) throws RequiredAttribute, AlreadyExistsAttribute {
        if(poop.getId() != null || poops.findById(poop.getId()).isPresent()){
            return checkFieldsPoop(poop);
        } else throw new AlreadyExistsAttribute("Poop with ID " + poop.getId() + " already exists");
    }

    //delete poop
    public Optional<Poop> delete(String id) throws RequiredAttribute {
        Optional<Poop> poop = poops.findById(id);

        if(poop.isPresent()){
            poops.delete(poop.get());
            return poop;
        } else throw new RequiredAttribute("Poop with ID " + id + " does not exist");
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
