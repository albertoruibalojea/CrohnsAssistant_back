package crohnsassistantapi.service;

import crohnsassistantapi.model.Professional;
import crohnsassistantapi.repository.ProfessionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfessionalService {
    private final ProfessionalRepository professionals;

    @Autowired
    public ProfessionalService(ProfessionalRepository professionals) {
        this.professionals = professionals;
    }

    //get one professional
    public Optional<Professional> get(String id) {
        return Optional.of(professionals.findById(id).get());
    }
}
