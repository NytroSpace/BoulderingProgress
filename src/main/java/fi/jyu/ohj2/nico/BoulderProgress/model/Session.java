package fi.jyu.ohj2.nico.BoulderProgress.model;

import java.util.ArrayList;

public record Session(ArrayList<Route> routes, String date, String UUID) {
    /// <summary>
    /// Returns the number of completed routes.
    /// </summary>
    /// <return>
    /// Number of completed routes as a long type.
    /// </return>
    public long getCompletedCount() {
        return routes.stream().filter(Route::isCompleted).count();
    }


    /// <summary>
    /// Returns the number of attempts made.
    /// </summary>
    /// <return>
    /// Number of attempts as a long type.
    public long getAttemptsCount() {
        return routes.stream().mapToInt(Route::attempts).sum();
    }
}
