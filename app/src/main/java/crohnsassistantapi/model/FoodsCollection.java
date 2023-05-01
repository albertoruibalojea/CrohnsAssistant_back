package crohnsassistantapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Document(collection = "foodsCollection")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "FoodsCollection", description = "Representation of the FoodsCollection object")
public class FoodsCollection {

    @Id
    private String id;
    @NotEmpty(message = "The Name field can not be empty")
    @Schema(required = true, example = "Egg")
    private String name;

    public FoodsCollection() {
        this.id = "";
        this.name = "";
    }

    public FoodsCollection(String id, String name){
        this.id = id;
        this.name = name;
    }

    //Getters and Setters
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

}
