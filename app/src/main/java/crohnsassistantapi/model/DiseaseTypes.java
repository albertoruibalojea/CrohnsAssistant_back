package crohnsassistantapi.model;

public enum DiseaseTypes {
    CROHN;
    //add colitis in the long run

    public static DiseaseTypes fromString(String text) {
        for (DiseaseTypes b : DiseaseTypes.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
