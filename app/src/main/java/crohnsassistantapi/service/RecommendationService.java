package crohnsassistantapi.service;

import crohnsassistantapi.exceptions.NotFoundAttribute;
import crohnsassistantapi.model.Recommendation;
import crohnsassistantapi.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {
    private final RecommendationRepository recommendations;

    @Autowired
    public RecommendationService(RecommendationRepository recommendations) {
        this.recommendations = recommendations;
    }


    //get all recommendations by category
    public Optional<List<Recommendation>> getByCategory(String id) throws NotFoundAttribute {
        if(!recommendations.findByCategory(id).isEmpty()){
            return Optional.of(recommendations.findByCategory(id));
        } else throw new NotFoundAttribute("The category with ID " + id + " does not exist in database or is empty");
    }

    //get a specific recommendation
    public Optional<Recommendation> get(String id) throws NotFoundAttribute {
        if(recommendations.findById(id).isPresent()){
            return recommendations.findById(id);
        } else throw new NotFoundAttribute("The recommendation with ID " + id + " does not exist in database");
    }
}
