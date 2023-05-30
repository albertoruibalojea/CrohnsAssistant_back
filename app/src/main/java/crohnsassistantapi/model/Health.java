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
import java.util.Objects;

@Document(collection = "health")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Health", description = "Representation of the Health object")
public class Health {
    @Id
    @Schema(example = "64064c611ac26b67e0f8680e", implementation = String.class)
    private String id;
    @NotEmpty(message = "The User field can not be empty")
    @Schema(required = true, example = "test@test.com", implementation = String.class)
    private String user;
    @NotNull(message = "The Disease field can not be empty")
    @Schema(required = true, example = "true", implementation = Boolean.class, defaultValue = "false")
    private boolean diseaseActive;
    @NotNull(message = "The Symptomatology field can not be empty")
    @Schema(required = true, example = "true", implementation = Boolean.class, defaultValue = "false")
    private boolean symptomatology;
    @NotEmpty(message = "The Disease field can not be empty")
    @Schema(required = true, example = "CROHN_ILEOCOLITIS", implementation = String.class, defaultValue = "CROHN_ILEOCOLITIS")
    private String type;
    @NotNull(message = "The Timestamp field can not be empty")
    @ArraySchema(schema = @Schema(implementation = Date.class, required = true))
    private Date timestamp;

    public Health() {
        this.id = "";
        this.user = "";
        this.diseaseActive = false;
        this.symptomatology = false;
        this.type = "CROHN_ILEOCOLITIS";
        this.timestamp = new Date();
    }

    public Health(String id, String user, String type) {
        this.id = id;
        this.user = user;
        this.diseaseActive = false;
        this.symptomatology = false;
        this.type = type;
        this.timestamp = new Date();
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

    public boolean isDiseaseActive() {
        return diseaseActive;
    }

    public void setDiseaseActive(boolean diseaseActive) {
        this.diseaseActive = diseaseActive;
    }

    public boolean isSymptomatology() {
        return symptomatology;
    }

    public void setSymptomatology(boolean symptomatology) {
        this.symptomatology = symptomatology;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
