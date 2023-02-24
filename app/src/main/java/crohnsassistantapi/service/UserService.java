package crohnsassistantapi.service;

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

    public Optional<User> get(String email) {
        return users.findById(email);
    }

    public Optional<User> create(User user){
        if (users.findById(user.getEmail()).isPresent()){
            throw new IllegalArgumentException("User already exists");
        } else{
            if(!user.getEmail().isEmpty() || (user.getEmail() != null)) {
                if(!user.getName().isEmpty()){
                    if(!user.getPassword().isEmpty()){
                        if(!user.getCROHN_TYPE().isEmpty()){
                            return Optional.of(users.save(user));
                        }
                        else throw new IllegalArgumentException("CROHN_TYPE is empty");
                    }
                    else throw new IllegalArgumentException("Password is empty");
                }
                else throw new IllegalArgumentException("Name is empty");
            }
            else throw new IllegalArgumentException("User already exists");

            //eiiteam not contemplated because of not being mandatory
        }
    }

    public Optional<User> update(User user){
        if (users.findById(user.getEmail()).isPresent()){
            if(!user.getEmail().isEmpty() || (user.getEmail() != null)) {
                if(!user.getName().isEmpty()){
                    if(!user.getPassword().isEmpty()){
                        if(!user.getCROHN_TYPE().isEmpty()){
                            return Optional.of(users.save(user));
                        }
                        else throw new IllegalArgumentException("CROHN_TYPE is empty");
                    }
                    else throw new IllegalArgumentException("Password is empty");
                }
                else throw new IllegalArgumentException("Name is empty");
            }
            else throw new IllegalArgumentException("User already exists");

            //eiiteam not contemplated because of not being mandatory
        }
        else throw new IllegalArgumentException("User doesn´t exist");
    }

    public Optional<User> delete(String email) {
        Optional<User> user = users.findById(email);
        if(user.isPresent()){
            users.delete(user.get());
            return user;
        }
        else throw new IllegalArgumentException("User doesn´t exist");
    }

    /*public Optional<User> modify(String email, User user) {

    }*/

}
