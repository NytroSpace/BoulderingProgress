import fi.jyu.ohj2.nico.BoulderProgress.model.MonthlyDataset;
import fi.jyu.ohj2.nico.BoulderProgress.model.Session;
import fi.jyu.ohj2.nico.BoulderProgress.model.Route;
import fi.jyu.ohj2.nico.BoulderProgress.model.WallType;
import fi.jyu.ohj2.nico.BoulderProgress.persistance.JsonDataService;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataServiceTest {
    JsonDataService service = new JsonDataService();

    @TempDir
    Path temp;

    @Test
    public void saveAndLoadMonthlyDataset() throws IOException {
        Path testPath = temp.resolve("MonthlyDataset.json");

        MonthlyDataset dataset = new MonthlyDataset(FXCollections.observableArrayList(), 2026, 1);
        dataset.getSessions().add(new Session(
                "epic",
                "January",
                List.of(
                        new Route(
                                "3+",
                                2,
                                WallType.ROOF,
                                false
                        )
                )
        ));

        service.saveMonthlyDataset(dataset, testPath);
        MonthlyDataset loadedDataset = service.loadMonthlyDataset(testPath);

        assertNotNull(loadedDataset, "Not null.");
        assertEquals(dataset.getYear(), loadedDataset.getYear(), "Years should match.");
        assertEquals(dataset.getSessions().size(), loadedDataset.getSessions().size(), "Session count should match.");
    }
}
