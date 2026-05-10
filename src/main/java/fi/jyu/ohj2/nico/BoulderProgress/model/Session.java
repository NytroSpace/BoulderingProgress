package fi.jyu.ohj2.nico.BoulderProgress.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Session {
    private final String uuid;
    private final String date;

    @JsonProperty("routes")
    private final ObservableList<Route> routes = FXCollections.observableArrayList();

    public Session(
            @JsonProperty("uuid") String uuid,
            @JsonProperty("date") String date
    ) {
        this.uuid = uuid;
        this.date = date;
    }

    public ObservableList<Route> getRoutes() {return routes;}

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
