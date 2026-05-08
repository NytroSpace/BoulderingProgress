package fi.jyu.ohj2.nico.BoulderProgress.model;

import java.util.ArrayList;

public class Session {
    private final ArrayList<Route> routes;
    private final String date;
    private final String uuid;

    public Session(ArrayList<Route> routes, String date, String uuid) {
        this.routes = routes;
        this.date = date;
        this.uuid = uuid;
    }

    /// <summary>
    /// Returns the number of completed routes.
    /// </summary>
    /// <return>
    /// Number of completed routes as a long type.
    /// </return>
    public long getCompletedCount() {
        return routes.stream().filter(Route::completed).count();
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
