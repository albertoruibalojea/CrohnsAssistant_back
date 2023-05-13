package crohnsassistantapi.service;

import crohnsassistantapi.exceptions.AlreadyExistsAttribute;
import crohnsassistantapi.exceptions.NotFoundAttribute;
import crohnsassistantapi.exceptions.RequiredAttribute;
import crohnsassistantapi.model.User;
import crohnsassistantapi.repository.EiiTeamRepository;
import crohnsassistantapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository users;
    private final EiiTeamRepository eiiTeam;

    @Autowired
    public UserService(UserRepository userRepository, EiiTeamRepository eiiTeam) {
        this.users = userRepository;
        this.eiiTeam = eiiTeam;
    }

    public Optional<User> get(String email) throws NotFoundAttribute {
        if (users.findById(email).isPresent()){
            return users.findById(email);
        } else throw new NotFoundAttribute("User with email " + email + " does not exist");

    }

    public Optional<User> create(User user) throws AlreadyExistsAttribute, RequiredAttribute, NotFoundAttribute {
        if (users.findById(user.getEmail()).isEmpty()){
            return checkFieldsUser(user);
        } else throw new AlreadyExistsAttribute("User with email " + user.getEmail() + " already exists");
    }

    public Optional<User> update(User user) throws RequiredAttribute, NotFoundAttribute {
        if (users.findById(user.getEmail()).isPresent()){
            return checkFieldsUser(user);
        } else throw new NotFoundAttribute("User with email " + user.getEmail() + " does not exist");
    }

    public Optional<User> delete(String email) throws NotFoundAttribute {
        Optional<User> user = users.findById(email);

        if(user.isPresent()){
            users.delete(user.get());
            return user;
        } else throw new NotFoundAttribute("User with email " + email + " does not exist");
    }


    private Optional<User> checkFieldsUser(User user) throws RequiredAttribute, NotFoundAttribute {
        if(!user.getEmail().isEmpty() || (user.getEmail() != null)) {
            if(!user.getName().isEmpty()){
                if(!user.getPassword().isEmpty()){
                    if(!user.getCROHN_TYPE().isEmpty()){
                        if(user.getDisease().equalsIgnoreCase("CROHN")){
                            if(user.getDaysToAnalyze() == null){
                                user.setDaysToAnalyze(3);
                            }
                            if(user.getRoles().isEmpty()){
                                List<String> roles = new ArrayList<>();
                                roles.add("ROLE_USER");
                                user.setRoles(roles);
                            } else if(user.getRoles().contains("ROLE_USER") || user.getRoles().contains("ROLE_ADMIN")){
                                if(!user.getEii_team().isEmpty()){
                                    if(eiiTeam.findById(user.getEii_team()).isPresent()){
                                        return Optional.of(users.save(user));
                                    } else throw new NotFoundAttribute("EiiTeam " + user.getEii_team() + " does not exist in the database");
                                }
                            }
                        } else throw new RequiredAttribute("CROHN is the only disease supported at the moment.");
                    } else throw new RequiredAttribute("CROHN_TYPE is empty");
                } else throw new RequiredAttribute("Password is empty");
            } else throw new RequiredAttribute("Name is empty");
        } else throw new RequiredAttribute("Email is empty");

        return Optional.empty();
    }

}
