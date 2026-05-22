package fi.jyu.ohj2.nico.BoulderProgress.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class MonthlyDataset {
    // We hide this from Jackson because Jackson doesn't know what an ObservableList is because it's fucking stupid
    @JsonIgnore
    private ObservableList<Session> sessions;

    private final int year;
    private final int month;

    @JsonProperty("schema-version")
    private final int schemaVersion = 1;

    public MonthlyDataset(ObservableList<Session> sessions, int year, int month) {
        this.sessions = sessions;
        this.year = year;
        this.month = month;
    }

    @JsonCreator
    public MonthlyDataset(
            @JsonProperty("sessions") List<Session> sessionList,
            @JsonProperty("year") int year,
            @JsonProperty("month") int month) {
        this.sessions = FXCollections.observableArrayList(sessionList != null ? sessionList : new ArrayList<>());
        this.year = year;
        this.month = month;
    }

    // --- GETTERS ---

    @JsonIgnore // Jackson should ignore the "Observable" version
    public ObservableList<Session> getSessions() {
        return sessions;
    }

    @JsonProperty("sessions")
    public List<Session> getSessionsForJson() {
        return new ArrayList<>(sessions);
    }

    @JsonProperty("year")
    public int getYear() { return year; }

    @JsonProperty("month")
    public int getMonth() { return month; }

    @JsonProperty("schema-version")
    public int getSchemaVersion() { return schemaVersion; }
}