package crohnsassistantapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Professional", description = "Representation of the Professional object")
public class Professional {
    @Id
    @Schema(example = "64064c611ac26b67e0f8680e", implementation = String.class)
    private String id;
    @NotEmpty(message = "The Name field can not be empty")
    @Schema(required = true, example = "Alberto", implementation = String.class)
    private String name;
    @NotEmpty(message = "The Type field can not be empty")
    @Schema(required = true, example = "NURSE", implementation = String.class, allowableValues = { "doctor", "nurse", "dietitian", "psychologist", "researcher", "stomatherapist", "auxiliary_nurse" })
    private String type;

    public Professional() {
        this.id = "";
        this.name = "";
        this.type = "";
    }

    public Professional(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Professional{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professional that = (Professional) o;
        return id.equals(that.id) && name.equals(that.name) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }
}
