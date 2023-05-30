package crohnsassistantapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DiseaseTypes", description = "IBDs supported by the API at the moment")
public enum DiseaseTypes {

    @Schema(description = "Crohn's disease")
    CROHN("crohn");
    //add colitis in the long run

    private final String type;

    DiseaseTypes(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static DiseaseTypes fromString(String text) {
        for (DiseaseTypes b : DiseaseTypes.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
