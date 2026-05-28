package fi.jyu.ohj2.nico.BoulderProgress.model;


/// <summary>
/// Specify all the types of wall that the dropdown button in the UI can show.
/// </summary>
public enum WallType {
    SLAB_OR_VERTICAL("Slab/Vertical"),
    OVERHANG("Overhang"),
    ROOF("Roof");

    // The label is required so the enum can be easily converted into a string and displayed in the TableColumn for WallType
    private final String label;

    // Constructor
    WallType(String label) {this.label = label;}

    @Override
    public String toString() {return label;}
}

