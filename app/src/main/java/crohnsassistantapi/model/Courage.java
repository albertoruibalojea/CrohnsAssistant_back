package crohnsassistantapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Document(collection = "courages")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Courage", description = "Representation of the Courage object")
public class Courage {
    @Id
    private String id;
    @NotEmpty(message = "The User field can not be empty")
    @Schema(required = true, example = "test@test.com", implementation = String.class)
    private String user;
    @NotNull(message = "The Level field can not be empty")
    @Schema(required = true, example = "2", implementation = Integer.class)
    private Integer level;
    @NotBlank(message = "The Activities field can not be empty")
    @ArraySchema(schema = @Schema(implementation = String.class, required = true, example = "Beach"))
    private ArrayList<String> activities;


    public Courage() {
        this.id = "";
        this.level = 2;
        this.user = "";
        this.activities = new ArrayList<>();
    }

    public Courage(String id, Integer level, String user, ArrayList<String> activities) {
        this.id = id;
        this.level = level;
        this.user = user;
        if(!activities.isEmpty()){
            this.activities = activities;
        } else this.activities = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public ArrayList<String> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<String> activities) {
        this.activities = activities;
    }

}
