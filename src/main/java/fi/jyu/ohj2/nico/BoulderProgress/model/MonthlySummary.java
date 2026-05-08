package fi.jyu.ohj2.nico.BoulderProgress.model;

public record MonthlySummary(String month, int year, String filePath) {
    @Override
    public String toString() {
        return month + " " + year;
    }
}