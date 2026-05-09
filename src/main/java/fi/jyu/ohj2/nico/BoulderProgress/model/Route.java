package fi.jyu.ohj2.nico.BoulderProgress.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/// <summary>
/// All the fields of the route.
/// </summary>
public record Route(
        @JsonProperty("grade") String grade,
        @JsonProperty("attempts") int attempts,
        @JsonProperty("wall-type") WallType wallType,
        @JsonProperty("completed") boolean completed,
        @JsonProperty("uuid") java.util.UUID uuid
) {

    @Override
    public String toString() {
        return grade + "|" + wallType + "|" + attempts + completed;
    }
}



