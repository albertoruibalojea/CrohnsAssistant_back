package crohnsassistantapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "poops")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Poop", description = "Representation of the Poop object")
public class Poop {
    @Id
    private String id;
    @NotEmpty(message = "The User field can not be empty")
    @Schema(required = true, example = "test@test.com", implementation = String.class)
    private String user;
    @NotNull(message = "The Timestamp field can not be empty")
    @ArraySchema(schema = @Schema(implementation = Date.class, required = true))
    private Date timestamp;
    @NotNull(message = "The Type field can not be empty. It has to be between 1 and 7")
    @Schema(required = true, example = "2", implementation = Integer.class, defaultValue = "3")
    private Integer type;
    @NotNull(message = "The Weight field can not be empty. It has to be between 1 and 6 (smaller to bigger)")
    @Schema(required = true, example = "2", implementation = Integer.class, defaultValue = "3")
    private Integer weight;
    @NotEmpty(message = "The Color field can not be empty. It has to be one of the Color enum values")
    @Schema(required = true, example = "brown", implementation = String.class, defaultValue = "brown")
    private String color;
    @NotNull(message = "The Urgency field can not be empty")
    @Schema(required = true, example = "true", implementation = Boolean.class, defaultValue = "false")
    private boolean urgency;
    @NotNull(message = "The Painfull field can not be empty")
    @Schema(required = true, example = "true", implementation = Boolean.class, defaultValue = "false")
    private boolean painfull;
    @NotNull(message = "The Blood field can not be empty")
    @Schema(required = true, example = "true", implementation = Boolean.class, defaultValue = "false")
    private boolean blood;
    //@NotEmpty(message = "The Notes field can not be empty")
    @Schema(required = true, example = "Anything you want", implementation = String.class)
    private String notes;

    public Poop() {
        this.id = "";
        this.user = "";
        this.timestamp = new Date();
        this.type = 3;
        this.weight = 3;
        this.color = "brown";
        this.urgency = false;
        this.painfull = false;
        this.blood = false;
        this.notes = "";
    }

    public Poop(String id, String user, Integer type, Integer weight, String color, boolean urgency, boolean painfull, boolean blood, String notes) {
        this.id = id;
        this.user = user;
        this.timestamp = new Date();
        this.type = type;
        this.weight = weight;
        this.color = color;
        this.urgency = urgency;
        this.painfull = painfull;
        this.blood = blood;
        this.notes = notes;
    }

    public Poop(String id, String user, Date timestamp, Integer type, Integer weight, String color, boolean urgency, boolean painfull, boolean blood, String notes) {
        this.id = id;
        this.user = user;
        this.timestamp = timestamp;
        this.type = type;
        this.weight = weight;
        this.color = color;
        this.urgency = urgency;
        this.painfull = painfull;
        this.blood = blood;
        this.notes = notes;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isUrgency() {
        return urgency;
    }

    public void setUrgency(boolean urgency) {
        this.urgency = urgency;
    }

    public boolean isPainfull() {
        return painfull;
    }

    public void setPainfull(boolean painfull) {
        this.painfull = painfull;
    }

    public boolean isBlood() {
        return blood;
    }

    public void setBlood(boolean blood) {
        this.blood = blood;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
