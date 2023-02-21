package crohnsassistantapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Objects;

@Document(collection = "courages")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Courage {
    @Id
    private String id;

    private String user;

    private String level;

    private ArrayList<String> activities;

    public Courage() {
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public ArrayList<String> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<String> activities) {
        this.activities = activities;
    }

    @Override
    public String toString() {
        return "Courage{" +
                "id='" + id + '\'' +
                ", user='" + level + '\'' +
                ", level='" + level + '\'' +
                ", activities=" + activities +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Courage courage = (Courage) o;
        return id.equals(courage.id) && user.equals(courage.user) && level.equals(courage.level) && Objects.equals(activities, courage.activities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, level, activities);
    }
}
