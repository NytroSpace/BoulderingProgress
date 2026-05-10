package fi.jyu.ohj2.nico.BoulderProgress.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.collections.ObservableList;
import java.util.ArrayList;

public class MonthlyDataset {
    private final ObservableList<Session> sessions; // Make the session list an observable list so the UI can detect changes to it
    private final int year;
    private final int month;

    @JsonProperty("schema-version") // We use JsonProperty for formatting the JSON file so it's nicer to look at for debugging purposes
    private final int schemaVersion = 1;

    public MonthlyDataset(@JsonProperty("sessions") ObservableList<Session> sessions, @JsonProperty("year") int year, @JsonProperty("month") int month) {
        this.sessions = sessions;
        this.year = year;
        this.month = month;
    }

    // GETTERS because our fields are private and jackson needs to see them
    public ObservableList<Session> getSessions() { return sessions; }
    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getSchemaVersion() { return schemaVersion; }
}
