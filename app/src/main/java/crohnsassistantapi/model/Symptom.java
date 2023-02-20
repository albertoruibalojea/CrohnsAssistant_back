package crohnsassistantapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "symptoms")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Symptom {

    //todos deben tener un id autogenerado como pk
    //tb deben tener un id de usuario para saber a quien pertenecen
    //todos deben tener un timestamp para saber cuando se crearon
}
