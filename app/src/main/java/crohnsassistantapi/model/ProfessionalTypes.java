package crohnsassistantapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProfessionalTypes", description = "Representation of the ProfessionalTypes enum")
public enum ProfessionalTypes {
    @Schema(description = "doctor")
    DOCTOR("doctor"),
    @Schema(description = "nurse")
    NURSE("nurse"),
    @Schema(description = "dietitian")
    DIETITIAN("dietitian"),
    @Schema(description = "psychologist")
    PSYCHOLOGIST("psychologist"),
    @Schema(description = "researcher")
    RESEARCHER("researcher"),
    @Schema(description = "stomatherapist")
    STOMATHERAPIST("stomatherapist"),
    @Schema(description = "auxiliary_nurse")
    AUXILIARY_NURSE("auxiliary_nurse");


    private final String type;

    ProfessionalTypes(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
