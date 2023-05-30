package crohnsassistantapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SymptomTypes", description = "Representation of the SymptomTypes enum")
public enum SymptomTypes {
    @Schema(description = "blood")
    BLOOD (4, "blood"),
    @Schema(description = "diarrhea")
    DIARRHEA (5, "diarrhea"),
    @Schema(description = "fatigue")
    FATIGUE (3, "fatigue"),
    @Schema(description = "joint_pain")
    JOINT_PAIN (1, "joint_pain"),
    @Schema(description = "muscle_pain")
    MUSCLE_PAIN (2, "muscle_pain"),
    @Schema(description = "nausea")
    NAUSEA (3, "nausea"),
    @Schema(description = "abdominal_pain")
    ABDOMINAL_PAIN (5, "abdominal_pain"),
    @Schema(description = "weight_loss")
    WEIGHT_LOSS (2, "weight_loss"),
    @Schema(description = "not_hungry")
    NOT_HUNGRY (3, "not_hungry"),
    @Schema(description = "anal_pain")
    ANAL_PAIN (4, "anal_pain"),
    @Schema(description = "headache")
    HEADACHE (1, "headache"),
    @Schema(description = "fever")
    FEVER (3, "fever"),
    @Schema(description = "bathroom_visits")
    BATHROOM_VISITS (4, "bathroom_visits"),
    @Schema(description = "aphtas")
    APHTHAS (1, "aphtas"),
    @Schema(description = "perianal_pain")
    PERIANAL_PAIN (3, "perianal_pain"),
    @Schema(description = "skin_rash")
    SKIN_RASH (2, "skin_rash");

    private final int punctuation;
    private final String name;

    private SymptomTypes(int punctuation, String name){
        this.punctuation = punctuation;
        this.name = name;
    }


    public int getPunctuation() {
        return punctuation;
    }

    public String getName() {
        return name;
    }

    public static SymptomTypes fromString(String text) {
        for (SymptomTypes b : SymptomTypes.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

}
