package crohnsassistantapi.model;

public enum SymptomTypes {
    BLOOD (4),
    DIARRHEA (5),
    FATIGUE (3),
    JOINT_PAIN (1),
    MUSCLE_PAIN (2),
    NAUSEA (3),
    ABDOMINAL_PAIN (5),
    WEIGHT_LOSS (2),
    NOT_HUNGRY (3),
    ANAL_PAIN (4),
    HEADACHE (1),
    FEVER (3),
    BATHROOM_VISITS (4),
    APHTHAS (1),
    PERIANAL_PAIN (3),
    SKIN_RASH (2);

    private final int punctuation;

    private SymptomTypes(int punctuation){
        this.punctuation = punctuation;
    }


    public int getPunctuation() {
        return punctuation;
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
