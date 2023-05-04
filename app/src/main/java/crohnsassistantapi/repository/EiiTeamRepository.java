package crohnsassistantapi.repository;

import crohnsassistantapi.model.EiiTeam;
import crohnsassistantapi.model.Professional;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EiiTeamRepository extends MongoRepository<EiiTeam, String> {

}
