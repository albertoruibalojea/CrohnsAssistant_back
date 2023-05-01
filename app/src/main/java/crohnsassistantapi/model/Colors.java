package crohnsassistantapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Colors", description = "Representation of the Colors enum")
public enum Colors {
    @Schema(description = "brown")
    BROWN("brown"),
    @Schema(description = "white")
    WHITE("white"),
    @Schema(description = "black")
    BLACK("black"),
    @Schema(description = "yellow")
    YELLOW("yellow"),
    @Schema(description = "green")
    GREEN("green"),
    @Schema(description = "red")
    RED("red");

    private final String color;

    Colors(String color){
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
