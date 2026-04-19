package fi.jyu.ohj2.nico.BoulderProgress.model;

/// <summary>
/// All the fields of the route.
/// </summary>
public record Route(String grade, int attempts, WallType wallType, boolean completed, java.util.UUID UUID) {

    @Override
    public String toString() {
        return grade + "|" + wallType + "|" + attempts + completed;
    }
}



