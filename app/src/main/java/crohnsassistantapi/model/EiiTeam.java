package crohnsassistantapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Objects;

@Document(collection = "eiiTeams")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EiiTeam {

    @Id
    private String id;
    private String phone;

    private ArrayList<Professional> professionals;

    public EiiTeam() {
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

    public ArrayList<Professional> getProfessionals() {
        return professionals;
    }

    public void setProfessionals(ArrayList<Professional> professionals) {
        this.professionals = professionals;
    }

    @Override
    public String toString() {
        return "EiiTeam{" +
                "id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", professionals=" + professionals +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EiiTeam eiiTeam = (EiiTeam) o;
        return id.equals(eiiTeam.id) && Objects.equals(phone, eiiTeam.phone) && Objects.equals(professionals, eiiTeam.professionals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone, professionals);
    }
}
