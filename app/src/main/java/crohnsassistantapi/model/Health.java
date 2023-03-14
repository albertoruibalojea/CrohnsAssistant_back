package crohnsassistantapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import crohnsassistantapi.service.UserService;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

@Document(collection = "health")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Health {
    @Id
    private String id;
    private String user;
    private boolean crohnActive;
    private boolean symptomatology;
    private String type;
    private Date timestamp;

    public Health() {
        this.id = "";
        this.user = "";
        this.crohnActive = false;
        this.symptomatology = false;
        this.type = "";
        this.timestamp = new Date();
    }

    public Health(String id, String user, String type) {
        this.id = id;
        this.user = user;
        this.crohnActive = false;
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

    public boolean isCrohnActive() {
        return crohnActive;
    }

    public void setCrohnActive(boolean crohnActive) {
        this.crohnActive = crohnActive;
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


    @Override
    public String toString() {
        return "Health{" +
                "id='" + id + '\'' +
                ", user='" + user + '\'' +
                ", crohnActive=" + crohnActive +
                ", symptomatology=" + symptomatology +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Health health = (Health) o;
        return crohnActive == health.crohnActive && symptomatology == health.symptomatology && Objects.equals(id, health.id) && user.equals(health.user) && type.equals(health.type) && timestamp.equals(health.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, crohnActive, symptomatology, type, timestamp);
    }
}
