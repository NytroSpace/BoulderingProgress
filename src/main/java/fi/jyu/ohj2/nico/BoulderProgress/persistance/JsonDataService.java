package fi.jyu.ohj2.nico.BoulderProgress.persistance;

import fi.jyu.ohj2.nico.BoulderProgress.model.MonthlyDataset;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Path;

public class JsonDataService {
    private final ObjectMapper mapper;

    public JsonDataService() {
        this.mapper = new ObjectMapper();
    }

    public void saveMonthlyDataset(MonthlyDataset log, Path path) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), log); // This will save the JSON file with formatting so it's nicer to look at during debugging
    }

    public MonthlyDataset loadMonthlyDataset(Path path) {
        return mapper.readValue(path.toFile(), MonthlyDataset.class); // Load the JSON data into objects
    }
}
