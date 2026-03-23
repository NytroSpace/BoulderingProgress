package fi.jyu.ohj2.nico.BoulderProgress.model;

import java.util.ArrayList;

public record Session(ArrayList<Route> routes, String date, String UUID) {}
