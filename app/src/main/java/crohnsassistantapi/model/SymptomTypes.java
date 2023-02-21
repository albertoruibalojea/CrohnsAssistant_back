package crohnsassistantapi.model;

public enum SymptomTypes {
    BLOOD,
    DIARRHEA,
    FATIGUE,
    JOINT_PAIN,
    MUSCLE_PAIN,
    NAUSEA,
    STOMACH_PAIN,
    WEIGHT_LOSS,
    NOT_HUNGRY,
    ANAL_PAIN,
    HEADACHE,
    FEVER,
    BATHROOM_VISITS,
    LESS_WEIGHT,
    APHTHAS,
    PERIANAL_PAIN,
    SKIN_RASH;

    public static SymptomTypes fromString(String text) {
        for (SymptomTypes b : SymptomTypes.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

}
