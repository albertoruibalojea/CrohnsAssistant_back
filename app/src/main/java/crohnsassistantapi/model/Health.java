package crohnsassistantapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private Date timestamp;

    public Health() {
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

    @Override
    public String toString() {
        return "Health{" +
                "id='" + id + '\'' +
                ", user='" + user + '\'' +
                ", crohnActive=" + crohnActive +
                ", symptomatology=" + symptomatology +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Health health = (Health) o;
        return crohnActive == health.crohnActive && symptomatology == health.symptomatology && id.equals(health.id) && user.equals(health.user) && timestamp.equals(health.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, crohnActive, symptomatology, timestamp);
    }
}
