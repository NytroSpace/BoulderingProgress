package fi.jyu.ohj2.nico.BoulderProgress.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.ArrayList;

/**
 * CLass for session which nests all the individual routes before adding the complete session as a whole to the main window.
 */
public class Session {
    // unique user identifier
    private final String uuid;
    private final String date;
    // List, which contains all added routes for that session
    private final ObservableList<Route> routes;

    @JsonCreator
    public Session(
            @JsonProperty("uuid") String uuid,
            @JsonProperty("date") String date,
            @JsonProperty("routes") List<Route> routesList
    ) {
        this.uuid = uuid;
        this.date = date;
        // Turn the raw Jackson array list into an observable list
        this.routes = FXCollections.observableArrayList(routesList != null ? routesList : new ArrayList<>());
    }

    // Constructor
    public Session(String uuid, String date) {
        this(uuid, date, new ArrayList<>());
    }

    public String getUuid() { return uuid; }
    public String getDate() { return date; }

    @JsonProperty("routes") // Keeps serialization clean
    public List<Route> getRoutesForJson() { return new ArrayList<>(routes); }

    public ObservableList<Route> getRoutes() { return routes; }

    public long getCompletedCount() {
        return routes.stream().filter(Route::completed).count();
    }

    public long getAttemptsCount() {
        return routes.stream().mapToInt(Route::attempts).sum();
    }
}