package crohnsassistantapi.service;

import crohnsassistantapi.exceptions.NotFoundAttribute;
import crohnsassistantapi.exceptions.RequiredAttribute;
import crohnsassistantapi.model.EiiTeam;
import crohnsassistantapi.model.Professional;
import crohnsassistantapi.model.ProfessionalTypes;
import crohnsassistantapi.repository.EiiTeamRepository;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EiiTeamService {
    private final EiiTeamRepository eiiTeams;
    private final MongoTemplate mongoTemplate;

    public EiiTeamService(EiiTeamRepository eiiTeams, MongoTemplate mongoTemplate) {
        this.eiiTeams = eiiTeams;
        this.mongoTemplate = mongoTemplate;
    }

    //get one eiiTeam
    public Optional<EiiTeam> get(String id) throws NotFoundAttribute {
        if(eiiTeams.findById(id).isPresent()){
            return Optional.of(eiiTeams.findById(id).get());
        } else throw new NotFoundAttribute("ID not present in database.");
    }

    //get list of professionals by eiiTeam
    public Optional<List<Professional>> getProfessionalsByEiiTeam(String eiiTeam) throws NotFoundAttribute {
        if(!eiiTeams.findProfessionalsByEiiTeam(eiiTeam).isEmpty()){
            return Optional.of(eiiTeams.findProfessionalsByEiiTeam(eiiTeam));
        } else throw new NotFoundAttribute("EiiTeam is not present in database.");
    }

    public EiiTeam getHospital(String eiiTeamId) throws RequiredAttribute {
        // Obtain info from each EiiTeam from MongoDB
        Document document = mongoTemplate.findById(eiiTeamId, Document.class, "eiiteams");

        // Create EiiTEam and assign attribute "name"
        EiiTeam eiiTeam = new EiiTeam();
        assert document != null;
        eiiTeam.setName(document.getString("name"));

        // Obtain professionals from EiiTeam and add them to the professionals´ list
        List<Document> professionalsDocs = document.getList("professionals", Document.class);
        ArrayList<Professional> professionals = new ArrayList<>();
        for (Document professionalDoc : professionalsDocs) {
            String name = professionalDoc.getString("name");
            String type = professionalDoc.getString("type");
            Professional professional = new Professional(null, name, type);
            if (type.equals(ProfessionalTypes.valueOf("doctor").toString().toLowerCase())) {
                professional.setType(ProfessionalTypes.valueOf("doctor").toString().toLowerCase());
                professionals.add(professional);
            } else if (type.equals(ProfessionalTypes.valueOf("nurse").toString().toLowerCase())){
                professional.setType(ProfessionalTypes.valueOf("nurse").toString().toLowerCase());
                professionals.add(professional);
            } else if (type.equals(ProfessionalTypes.valueOf("dietitian").toString().toLowerCase())){
                professional.setType(ProfessionalTypes.valueOf("dietitian").toString().toLowerCase());
                professionals.add(professional);
            } else if (type.equals(ProfessionalTypes.valueOf("psychologist").toString().toLowerCase())){
                professional.setType(ProfessionalTypes.valueOf("psychologist").toString().toLowerCase());
                professionals.add(professional);
            } else if (type.equals(ProfessionalTypes.valueOf("researcher").toString().toLowerCase())){
                professional.setType(ProfessionalTypes.valueOf("researcher").toString().toLowerCase());
                professionals.add(professional);
            } else if (type.equals(ProfessionalTypes.valueOf("stomatherapist").toString().toLowerCase())){
                professional.setType(ProfessionalTypes.valueOf("stomatherapist").toString().toLowerCase());
                professionals.add(professional);
            } else if (type.equals(ProfessionalTypes.valueOf("auxiliary_nurse").toString().toLowerCase())){
                professional.setType(ProfessionalTypes.valueOf("auxiliary_nurse").toString().toLowerCase());
                professionals.add(professional);
            } else throw new RequiredAttribute("Professional type is needed");
        }

        // Set professionals list to the EiiTeam
        eiiTeam.setProfessionals(professionals);

        return eiiTeam;

    }

}
