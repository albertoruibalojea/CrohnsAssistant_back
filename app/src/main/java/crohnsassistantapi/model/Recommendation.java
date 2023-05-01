package crohnsassistantapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Document(collection = "recommendations")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Recommendation", description = "Representation of the Recommendation object")
public class Recommendation {
    @Id
    private String id;
    @NotEmpty(message = "The Title field can not be empty")
    @Schema(required = true, example = "Brotes. Signos y síntomas", implementation = String.class)
    private String title;
    @NotEmpty(message = "The Url field can not be empty")
    @Schema(required = true, example = "https://educainflamatoria.com/enfermedad-crohn/brotes-signos-y-sintomas/", implementation = String.class)
    private String url;
    @NotEmpty(message = "The Category field can not be empty")
    @Schema(required = true, example = "INF", implementation = String.class)
    private String category;

    public Recommendation() {
        this.id = "";
        this.title = "";
        this.url = "";
        this.category = "";
    }

    public Recommendation(String id, String title, String url, String category) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", category='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recommendation that = (Recommendation) o;
        return id.equals(that.id) && title.equals(that.title) && url.equals(that.url) && category.equals(that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, url, category);
    }
}
