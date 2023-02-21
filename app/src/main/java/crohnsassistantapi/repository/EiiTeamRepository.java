package crohnsassistantapi.repository;

import crohnsassistantapi.model.EiiTeam;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EiiTeamRepository extends MongoRepository<EiiTeam, String> {
}
