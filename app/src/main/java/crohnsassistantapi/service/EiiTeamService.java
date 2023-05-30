package crohnsassistantapi.service;

import crohnsassistantapi.exceptions.NotFoundAttribute;
import crohnsassistantapi.exceptions.RequiredAttribute;
import crohnsassistantapi.model.EiiTeam;
import crohnsassistantapi.model.Professional;
import crohnsassistantapi.model.ProfessionalTypes;
import crohnsassistantapi.repository.EiiTeamRepository;
import crohnsassistantapi.repository.ProfessionalRepository;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EiiTeamService {
    private final EiiTeamRepository eiiTeams;
    private final ProfessionalRepository professionals;
    private final MongoTemplate mongoTemplate;

    public EiiTeamService(EiiTeamRepository eiiTeams, ProfessionalRepository professionals, MongoTemplate mongoTemplate) {
        this.eiiTeams = eiiTeams;
        this.professionals = professionals;
        this.mongoTemplate = mongoTemplate;
    }

    //get one eiiTeam
    public Optional<EiiTeam> getEiiTeam(String id) throws RequiredAttribute, NotFoundAttribute {
        if(eiiTeams.findById(id).isPresent()){
            // Obtain info from each EiiTeam from MongoDB
            Document document = mongoTemplate.findById(id, Document.class, "eiiteams");

            // Create EiiTEam and assign attribute "name"
            EiiTeam eiiTeam = new EiiTeam();
            assert document != null;
            eiiTeam.setName(document.getString("name"));

            // Obtain professionals from EiiTeam and add them to the professionals´ list
            List<Document> professionalsDocs = document.getList("professionals", Document.class);
            ArrayList<Professional> professionals = new ArrayList<>();
            for (Document professionalDoc : professionalsDocs) {
                String name = professionalDoc.getString("name");
                String type = professionalDoc.getString("type").toLowerCase();
                Professional professional = new Professional(null, name, type);
                if (type.equals(ProfessionalTypes.DOCTOR.getType())) {
                    professional.setType(ProfessionalTypes.DOCTOR.getType());
                    professionals.add(professional);
                } else if (type.equals(ProfessionalTypes.NURSE.getType())) {
                    professional.setType(ProfessionalTypes.NURSE.getType());
                    professionals.add(professional);
                } else if (type.equals(ProfessionalTypes.DIETITIAN.getType())) {
                    professional.setType(ProfessionalTypes.DIETITIAN.getType());
                    professionals.add(professional);
                } else if (type.equals(ProfessionalTypes.PSYCHOLOGIST.getType())){
                    professional.setType(ProfessionalTypes.PSYCHOLOGIST.getType());
                    professionals.add(professional);
                } else if (type.equals(ProfessionalTypes.RESEARCHER.getType())){
                    professional.setType(ProfessionalTypes.RESEARCHER.getType());
                    professionals.add(professional);
                } else if (type.equals(ProfessionalTypes.STOMATHERAPIST.getType())){
                    professional.setType(ProfessionalTypes.STOMATHERAPIST.getType());
                    professionals.add(professional);
                } else if (type.equals(ProfessionalTypes.AUXILIARY_NURSE.getType())){
                    professional.setType(ProfessionalTypes.AUXILIARY_NURSE.getType());
                    professionals.add(professional);
                } else throw new RequiredAttribute("Professional type is needed");
            }

            // Set professionals list to the EiiTeam
            eiiTeam.setProfessionals(professionals);

            return Optional.of(eiiTeam);
        } else throw new NotFoundAttribute("The ID" + id + " is not present in the database for any EiiTeam.");
    }

}
