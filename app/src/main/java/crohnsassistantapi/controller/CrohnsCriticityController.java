package crohnsassistantapi.controller;

import crohnsassistantapi.model.User;
import crohnsassistantapi.service.CrohnsCriticityService;
import crohnsassistantapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("crohnscriticity")
public class CrohnsCriticityController {

    private final CrohnsCriticityService crohnsCriticityService;
    private final UserService userService;

    @Autowired
    public CrohnsCriticityController(CrohnsCriticityService crohnsCriticityService, UserService userService) {
        this.crohnsCriticityService = crohnsCriticityService;
        this.userService = userService;
    }

    @GetMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void get(@PathVariable("user") String email){

        Optional<User> user = userService.get(email);
        crohnsCriticityService.analyze(user.get());
    }
}
