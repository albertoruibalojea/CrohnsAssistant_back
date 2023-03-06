package crohnsassistantapi.controller;

import crohnsassistantapi.model.EiiTeam;
import crohnsassistantapi.model.Professional;
import crohnsassistantapi.service.EiiTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("eiiteam")
public class EiiTEamController {
    private final EiiTeamService eiiTeamService;

    @Autowired
    public EiiTEamController(EiiTeamService eiiTeamService) {
        this.eiiTeamService = eiiTeamService;
    }

    @GetMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Optional<EiiTeam> get(String id) {
        return eiiTeamService.get(id);
    }

    @GetMapping(
            path = "{id}/professionals",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Optional<List<Professional>> getByEiiTeam(String id) {
        return eiiTeamService.getProfessionalsByEiiTeam(id);
    }
}
