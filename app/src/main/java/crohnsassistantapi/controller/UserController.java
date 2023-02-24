package crohnsassistantapi.controller;

import crohnsassistantapi.model.User;
import crohnsassistantapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService users;

    @Autowired
    public UserController(UserService users) {
        this.users = users;
    }

    @GetMapping(
            path = "{email}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<User> get(@PathVariable("email") String email) {
        return ResponseEntity.of(users.get(email));
    }

    @PostMapping(
            //path="{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<User> create(@RequestBody User user){
        return ResponseEntity.of(users.create(user));
    }

    @PutMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<User> update(@RequestBody User user) {
        return ResponseEntity.of(users.update(user));
    }

    @DeleteMapping(
            path = "{email}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<User> delete(@PathVariable("email") String email) {
        return ResponseEntity.of(users.delete(email));
    }
}
