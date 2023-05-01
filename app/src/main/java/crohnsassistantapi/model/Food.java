package crohnsassistantapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "foods")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Food", description = "Representation of the Food object")
public class Food {
    @Id
    private String id;
    @NotEmpty(message = "The Name field can not be empty")
    @Schema(required = true, example = "Egg", implementation = String.class)
    private String name;
    @NotEmpty(message = "The User field can not be empty")
    @Schema(required = true, example = "test@test.com", implementation = String.class)
    private String user;
    @NotNull(message = "The Timestamp field can not be empty")
    @ArraySchema(schema = @Schema(implementation = Date.class, required = true))
    private Date timestamp;
    @NotNull(message = "The Forbidden field can not be empty")
    @ArraySchema(schema = @Schema(implementation = Boolean.class, required = true, defaultValue = "false"))
    private boolean forbidden;

    public Food() {
        this.id = "";
        this.name = "";
        this.user = "";
        this.timestamp = new Date();
        this.forbidden = false;
    }

    public Food(String id, String user, String name) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.timestamp = new Date();
        this.forbidden = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isForbidden() {
        return forbidden;
    }

    public void setForbidden(boolean forbidden) {
        this.forbidden = forbidden;
    }

}
