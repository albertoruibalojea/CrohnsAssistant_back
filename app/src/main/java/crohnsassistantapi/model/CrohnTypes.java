package crohnsassistantapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CrohnTypes", description = "Crohn's disease enum")
public enum CrohnTypes {
    @Schema(description = "CROHN_ILEOCOLITIS type and associated symptoms and punctuation")
    CROHN_ILEOCOLITIS("CROHN_ILEOCOLITIS", SymptomTypes.DIARRHEA.getPunctuation() + SymptomTypes.ABDOMINAL_PAIN.getPunctuation() +
            SymptomTypes.FATIGUE.getPunctuation() + SymptomTypes.WEIGHT_LOSS.getPunctuation() + SymptomTypes.BLOOD.getPunctuation() +
            SymptomTypes.NOT_HUNGRY.getPunctuation() + SymptomTypes.JOINT_PAIN.getPunctuation() + SymptomTypes.HEADACHE.getPunctuation()
            + SymptomTypes.SKIN_RASH.getPunctuation()),
    @Schema(description = "CROHN_ILEITIS type and associated symptoms and punctuation")
    CROHN_ILEITIS("CROHN_ILEITIS", SymptomTypes.DIARRHEA.getPunctuation() + SymptomTypes.ABDOMINAL_PAIN.getPunctuation() +
            SymptomTypes.FATIGUE.getPunctuation() + SymptomTypes.FEVER.getPunctuation() + SymptomTypes.WEIGHT_LOSS.getPunctuation() +
            SymptomTypes.APHTHAS.getPunctuation() + SymptomTypes.SKIN_RASH.getPunctuation()),
    @Schema(description = "CROHN_COLITIS type and associated symptoms and punctuation")
    CROHN_COLITIS("CROHN_COLITIS", SymptomTypes.DIARRHEA.getPunctuation() + SymptomTypes.ABDOMINAL_PAIN.getPunctuation() +
            SymptomTypes.FEVER.getPunctuation() + SymptomTypes.NAUSEA.getPunctuation() + SymptomTypes.NOT_HUNGRY.getPunctuation() +
            SymptomTypes.WEIGHT_LOSS.getPunctuation() + SymptomTypes.APHTHAS.getPunctuation() + SymptomTypes.SKIN_RASH.getPunctuation()),
    @Schema(description = "CROHN_UPPER_TRACT type and associated symptoms and punctuation")
    CROHN_UPPER_TRACT("CROHN_UPPER_TRACT", SymptomTypes.ABDOMINAL_PAIN.getPunctuation() + SymptomTypes.FEVER.getPunctuation() +
            SymptomTypes.NAUSEA.getPunctuation() + SymptomTypes.NOT_HUNGRY.getPunctuation() + SymptomTypes.WEIGHT_LOSS.getPunctuation() +
            SymptomTypes.APHTHAS.getPunctuation() + SymptomTypes.SKIN_RASH.getPunctuation()),
    @Schema(description = "CROHN_PERIANAL type and associated symptoms and punctuation")
    CROHN_PERIANAL("CROHN_PERIANAL", SymptomTypes.DIARRHEA.getPunctuation() + SymptomTypes.ANAL_PAIN.getPunctuation() +
            SymptomTypes.BLOOD.getPunctuation() + SymptomTypes.PERIANAL_PAIN.getPunctuation() + SymptomTypes.SKIN_RASH.getPunctuation());

    private final String type;
    private final Integer threshold; // calculated from the total sum of the symptom values of the type

    private CrohnTypes(String type, Integer threshold){
        this.type = type;
        this.threshold = threshold;
    }


    public String getType() {
        return type;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public static CrohnTypes fromString(String text) {
        for (CrohnTypes b : CrohnTypes.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
