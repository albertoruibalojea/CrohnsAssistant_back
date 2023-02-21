package crohnsassistantapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    private String id;

    private String CROHN_TYPE;

    private String email;
    private String password;
    private String name;
    private String eii_team;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCROHN_TYPE() {
        return CROHN_TYPE;
    }

    public void setCROHN_TYPE(String CROHN_TYPE) {
        this.CROHN_TYPE = CROHN_TYPE;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEii_team() {
        return eii_team;
    }

    public void setEii_team(String eii_team) {
        this.eii_team = eii_team;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", CROHN_TYPE='" + CROHN_TYPE + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", eii_team='" + eii_team + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && CROHN_TYPE.equals(user.CROHN_TYPE) && email.equals(user.email) && password.equals(user.password) && Objects.equals(name, user.name) && Objects.equals(eii_team, user.eii_team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, CROHN_TYPE, email, password, name, eii_team);
    }
}
