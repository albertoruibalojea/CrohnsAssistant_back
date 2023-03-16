package crohnsassistantapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "User", description = "Representation of the User object")
public class User {
    @Id
    @NotBlank(message = "The email field can not be empty")
    @Email
    @Schema(required = true, example = "test@test.com")
    private String email;
    @Schema(required = true, example = "PATTERN_ILEOCOLITIS")
    @NotBlank(message = "The Crohn Type field can not be empty")
    private String CROHN_TYPE;
    @Schema(required = true, example = "*************")
    @NotBlank(message = "The password field can not be empty")
    private String password;
    @Schema(required = true, example = "Alberto")
    private String name;
    @Schema(example = "64064c611ac26b67e0f86811")
    private String eii_team;
    @Schema(example = "3")
    @NotBlank(message = "The Days to Analyze field can not be empty")
    private Integer daysToAnalyze;
    @Schema(example = "ROLE_USER")
    private List<String> roles;

    public User(String email, String CROHN_TYPE, String password, String name, String eii_team, Integer daysToAnalyze, List<String> roles) {
        this.email = email;
        this.CROHN_TYPE = CROHN_TYPE;
        this.password = password;
        this.name = name;
        this.eii_team = eii_team;
        this.daysToAnalyze = daysToAnalyze;
        this.roles = roles;
    }

    public User() {
        this.email = "";
        this.CROHN_TYPE = "";
        this.password = "";
        this.name = "";
        this.eii_team = "";
        this.daysToAnalyze = 3;
        this.roles = new ArrayList<>();
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

    public Integer getDaysToAnalyze() {
        return daysToAnalyze;
    }

    public void setDaysToAnalyze(Integer daysToAnalyze) {
        this.daysToAnalyze = daysToAnalyze;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
