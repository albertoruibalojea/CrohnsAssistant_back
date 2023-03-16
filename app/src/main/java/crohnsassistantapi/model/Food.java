package crohnsassistantapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

@Document(collection = "foods")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Food {
    @Id
    private String id;
    private String name;
    private String user;
    private Date timestamp;
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

    @Override
    public String toString() {
        return "Food{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", user='" + user + '\'' +
                ", timestamp=" + timestamp +
                ", forbidden=" + forbidden +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return forbidden == food.forbidden && Objects.equals(id, food.id) && name.equals(food.name) && user.equals(food.user) && timestamp.equals(food.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, user, timestamp, forbidden);
    }
}
