package crohnsassistantapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Objects;

@Document(collection = "eiiTeams")
@Schema(name = "EiiTeam", description = "Representation of the EiiTeam object")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EiiTeam {

    @Id
    private String id;
    @Schema(example = "986800000", implementation = String.class)
    private String phone;
    @NotBlank(message = "The Name field can not be empty")
    @Schema(required = true, example = "Hospital Universitario de Pontevedra", implementation = String.class)
    private String name;
    @NotBlank(message = "The Professionals field can not be empty")
    @ArraySchema(schema = @Schema(implementation = Professional.class, required = true))
    private ArrayList<Professional> professionals;

    public EiiTeam() {
        this.id = "";
        this.phone = "";
        this.name = "";
        this.professionals = new ArrayList<>();
    }

    public EiiTeam(String id, String phone, String name, ArrayList<Professional> professionals) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        if(!professionals.isEmpty()){
            this.professionals = professionals;
        }else this.professionals = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Professional> getProfessionals() {
        return professionals;
    }

    public void setProfessionals(ArrayList<Professional> professionals) {
        this.professionals = professionals;
    }

}
