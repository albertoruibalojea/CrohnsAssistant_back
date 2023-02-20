package crohnsassistantapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "poops")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Poop {
}
