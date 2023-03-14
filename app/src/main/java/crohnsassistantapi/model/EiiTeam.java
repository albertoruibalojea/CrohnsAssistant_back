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
    private String name;
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

    @Override
    public String toString() {
        return "EiiTeam{" +
                "id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", professionals=" + professionals +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EiiTeam eiiTeam = (EiiTeam) o;
        return id.equals(eiiTeam.id) && Objects.equals(phone, eiiTeam.phone) && Objects.equals(name, eiiTeam.name) && Objects.equals(professionals, eiiTeam.professionals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone, name, professionals);
    }
}
