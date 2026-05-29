import fi.jyu.ohj2.nico.BoulderProgress.model.MonthlyDataset;
import fi.jyu.ohj2.nico.BoulderProgress.model.Route;
import fi.jyu.ohj2.nico.BoulderProgress.model.Session;
import fi.jyu.ohj2.nico.BoulderProgress.model.WallType;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MonthlyDatasetTest {

    @Test
    void testAddingSessionToDataset() {
        MonthlyDataset dataset = new MonthlyDataset(FXCollections.observableArrayList(), 2026, 1);
        List<Route> routes = List.of(new Route("3+", 12, WallType.OVERHANG, true));
        Session testSession = new Session("awdwa", "April", routes);

        dataset.getSessions().add(testSession);

        assertEquals(1, dataset.getSessions().size());
        assertEquals("3+", dataset.getSessions().getFirst().getRoutes().getFirst().grade());
    }

    @Test
    void testClearingSessionsFromDataset() {
        MonthlyDataset dataset = new MonthlyDataset(FXCollections.observableArrayList(), 2026, 1);
        dataset.getSessions().add(new Session("awdwa", "April", new ArrayList<>()));
        assertEquals(1, dataset.getSessions().size());

        dataset.getSessions().clear();

        assertEquals(0, dataset.getSessions().size());
    }
}