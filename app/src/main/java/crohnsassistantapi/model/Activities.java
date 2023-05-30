package crohnsassistantapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Activities", description = "Representation of the Activities enum")
public enum Activities {
    @Schema(description = "family")
    FAMILY("family"),
    @Schema(description = "friends")
    FRIENDS("friends"),
    @Schema(description = "travel")
    TRAVEL("travel"),
    @Schema(description = "date")
    DATE("date"),
    @Schema(description = "sleep")
    SLEEP("sleep"),
    @Schema(description = "sport")
    SPORT("sport"),
    @Schema(description = "outdoors")
    OUTDOORS("outdoors"),
    @Schema(description = "shopping")
    SHOPPING("shopping"),
    @Schema(description = "reading")
    READING("reading"),
    @Schema(description = "games")
    GAMES("games"),
    @Schema(description = "movies")
    MOVIES("movies"),
    @Schema(description = "music")
    MUSIC("music"),
    @Schema(description = "cleaning")
    CLEANING("cleaning"),
    @Schema(description = "eat")
    EAT("eat"),
    @Schema(description = "rest")
    REST("rest"),
    @Schema(description = "selfcare")
    SELFCARE("selfcare");

    private final String activity;

    Activities(String activity) {
        this.activity = activity;
    }

    private String getActivity(){
        return activity;
    }
}
