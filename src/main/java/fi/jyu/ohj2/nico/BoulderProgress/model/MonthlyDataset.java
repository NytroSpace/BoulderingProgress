package fi.jyu.ohj2.nico.BoulderProgress.model;

import java.util.ArrayList;

public class MonthlyDataset {
    private final ArrayList<Session> sessions;
    private final int year;
    private final int month;

    MonthlyDataset(ArrayList<Session> sessions, int year, int month) {
        this.sessions = sessions;
        this.year = year;
        this.month = month;
    }
}
