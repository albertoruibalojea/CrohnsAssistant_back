package crohnsassistantapi.service;

import crohnsassistantapi.exceptions.NotFoundAttribute;
import crohnsassistantapi.exceptions.RequiredAttribute;
import crohnsassistantapi.model.User;
import crohnsassistantapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository users;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.users = userRepository;
    }

    public Optional<User> get(String email) throws NotFoundAttribute {
        if (users.findById(email).isPresent()){
            return users.findById(email);
        } else throw new NotFoundAttribute("User does not exist");

    }

    public Optional<User> create(User user) throws RequiredAttribute {
        if (users.findById(user.getEmail()).isPresent()){
            throw new IllegalArgumentException("User already exists");
        } else{
            if(!user.getEmail().isEmpty() || (user.getEmail() != null)) {
                if(!user.getName().isEmpty()){
                    if(!user.getPassword().isEmpty()){
                        if(!user.getCROHN_TYPE().isEmpty()){
                            return Optional.of(users.save(user));
                        } else throw new RequiredAttribute("CROHN_TYPE is empty");
                    } else throw new RequiredAttribute("Password is empty");
                } else throw new RequiredAttribute("Name is empty");
            } else throw new RequiredAttribute("Email is empty");

            //eiiteam not contemplated because of not being mandatory
        }
    }

    public Optional<User> update(User user) throws RequiredAttribute, NotFoundAttribute {
        if (users.findById(user.getEmail()).isPresent()){
            if(!user.getEmail().isEmpty() || (user.getEmail() != null)) {
                if(!user.getName().isEmpty()){
                    if(!user.getPassword().isEmpty()){
                        if(!user.getCROHN_TYPE().isEmpty()){
                            return Optional.of(users.save(user));
                        } else throw new RequiredAttribute("CROHN_TYPE is empty");
                    } else throw new RequiredAttribute("Password is empty");
                } else throw new RequiredAttribute("Name is empty");
            } else throw new RequiredAttribute("Email is empty");

            //eiiteam not contemplated because of not being mandatory
        } else throw new NotFoundAttribute("User doesn´t exist");
    }

    public Optional<User> delete(String email) throws NotFoundAttribute {
        Optional<User> user = users.findById(email);

        if(user.isPresent()){
            users.delete(user.get());
            return user;
        } else throw new NotFoundAttribute("User doesn´t exist");
    }

}
