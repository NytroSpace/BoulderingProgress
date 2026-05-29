package fi.jyu.ohj2.nico.BoulderProgress.model;

/// <summary>
/// This record is made so that MainController doesn't need to keep a huge list of MonthlyDatasets
/// the user might never even look at.
///
/// Instead, we save the month and year for display purposes and the path for IF the user wants to view an old month.
/// </summary>
public record MonthlySummary(String month, int year, String filePath) {
    @Override
    public String toString() {
        return month + " " + year;
    }
}