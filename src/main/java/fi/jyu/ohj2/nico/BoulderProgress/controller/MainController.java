package fi.jyu.ohj2.nico.BoulderProgress.controller;

import fi.jyu.ohj2.nico.BoulderProgress.App;
import fi.jyu.ohj2.nico.BoulderProgress.persistance.JsonDataService;
import fi.jyu.ohj2.nico.BoulderProgress.model.MonthlyDataset;
import fi.jyu.ohj2.nico.BoulderProgress.model.Session;
import fi.jyu.ohj2.nico.BoulderProgress.model.Route;
import fi.jyu.ohj2.nico.BoulderProgress.model.MonthlySummary;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

public class MainController implements Initializable {
    @FXML
    private Button AddButton;
    @FXML
    private Button ModifyButton;
    @FXML
    private Button RemoveButton;
    @FXML
    private Button ExitButton;

    @FXML
    private Button ShowActiveButton;
    @FXML
    private TableView<MonthlySummary> Archive;
    @FXML
    private TableColumn<MonthlySummary, String> colArchiveDate;

    @FXML
    private TableView<Session> MonthlyTable;
    @FXML
    private TableColumn<Session, Number> colNro;
    @FXML
    private TableColumn<Session, String> colDate;
    @FXML
    private TableColumn<Session, String> colHardest;
    @FXML
    private TableColumn<Session, String> colMode;
    @FXML
    private TableColumn<Session, Number> colTotal;

    @FXML
    private Label lblTotalClimbs;
    @FXML
    private Label lblLatestSession;
    @FXML
    private Label lblHardestGrade;
    @FXML
    private Label lblTimesClimbed;
    @FXML
    private Label lblTotalCompleted;

    private MonthlyDataset monthlyDataset;
    // We keep track of whether we are looking at an archive so we can disable editing
    private boolean isViewingArchive = false;

    private final ObservableList<MonthlySummary> archiveSummaries = FXCollections.observableArrayList();
    JsonDataService jsonDataService = new JsonDataService();
    private static final Path SAVE_FILE_CURRENT = Path.of("data/climbs/current/climbs.json");
    private static final Path SAVE_FILE_ARCHIVE = Path.of("data/climbs/archived");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //region - Setup action handlers
        AddButton.setOnAction(e -> openSessionWindow("Add Session"));
        ModifyButton.setOnAction(e -> openSessionWindow("Modify Session"));
        ExitButton.setOnAction(e -> closeApplication());
        RemoveButton.setOnAction(e -> removeSelectedSession());
        ShowActiveButton.setOnAction(e -> loadActiveDataset());
        //endregion

        //region - Setup Main Table columns
        colNro.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(MonthlyTable.getItems().indexOf(p.getValue()) + 1)
        );
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colHardest.setCellValueFactory(p -> {
            String hardest = p.getValue().getRoutes().stream()
                    .filter(Route::completed)
                    .map(Route::grade)
                    .max(String::compareTo)
                    .orElse("--");
            return new ReadOnlyObjectWrapper<>(hardest);
        });
        colMode.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>("--"));
        colTotal.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getAttemptsCount()));
        //endregion

        // Setup Archive Table columns and selection listener
        colArchiveDate.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().toString()));
        Archive.setItems(archiveSummaries);

        Archive.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadArchivedDataset(newSelection);
            }
        });

        loadActiveDataset();

        populateArchiveList();
    }


    /*
    Loads the current active dataset into the main view
     */
    private void loadActiveDataset() {
        try {
            monthlyDataset = jsonDataService.loadMonthlyDataset(SAVE_FILE_CURRENT);

            if (monthlyDataset.getYear() != LocalDate.now().getYear() || monthlyDataset.getMonth() != LocalDate.now().getMonthValue()) {
                System.out.println("Old data. Archiving and creating a new dataset.");
                try {
                    archiveDataset(SAVE_FILE_ARCHIVE, monthlyDataset);
                    createNewDataset(SAVE_FILE_CURRENT);
                    populateArchiveList(); // Refresh archive list
                } catch (IOException e) {
                    System.err.println("Error during refresh: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("No valid save found. Creating new dataset."); // Fresh user
            try {
                createNewDataset(SAVE_FILE_CURRENT);
            } catch (IOException ex) {
                monthlyDataset = new MonthlyDataset(
                        FXCollections.observableArrayList(),
                        LocalDate.now().getYear(),
                        LocalDate.now().getMonthValue()
                );
            }
        }

        isViewingArchive = false;
        toggleActionButtons(true); // Allow changes to current month
        MonthlyTable.setItems(monthlyDataset.getSessions());
        Archive.getSelectionModel().clearSelection(); // Clear visual archive selection
        updateStatistics();
    }

    /*
    Loads an archived file into the Main Table based on the selected summary path
     */
    private void loadArchivedDataset(MonthlySummary summary) {
        try {
            Path path = Path.of(summary.filePath());
            monthlyDataset = jsonDataService.loadMonthlyDataset(path);
            isViewingArchive = true;
            toggleActionButtons(false); // Disable editing buttons for safety
            MonthlyTable.setItems(monthlyDataset.getSessions());
            updateStatistics();
        } catch (Exception e) {
            System.err.println("Failed to load archive: " + e.getMessage());
        }
    }

    /*
    Scans the archive folder for filenames matched like "YYYY_MM.json" and transforms them into summaries
     */
    private void populateArchiveList() {
        archiveSummaries.clear();
        if (!Files.exists(SAVE_FILE_ARCHIVE)) return; // Empty folder

        try (Stream<Path> stream = Files.list(SAVE_FILE_ARCHIVE)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".json"))
                    .forEach(path -> {
                        String name = path.getFileName().toString().replace(".json", "");
                        String[] parts = name.split("_");
                        if (parts.length == 2) {
                            try {
                                int year = Integer.parseInt(parts[0]);
                                int monthVal = Integer.parseInt(parts[1]);
                                String monthName = Month.of(monthVal).getDisplayName(TextStyle.FULL, Locale.ENGLISH);

                                archiveSummaries.add(new MonthlySummary(monthName, year, path.toString()));
                            } catch (Exception e) {
                                // Skip if wrong name format
                            }
                        }
                    });

            // Sort archives newest first
            archiveSummaries.sort((a, b) -> {
                if (a.year() != b.year()) return Integer.compare(b.year(), a.year());
                // Simple conversion to sort months backwards
                return Integer.compare(Month.valueOf(a.month().toUpperCase()).getValue(), Month.valueOf(b.month().toUpperCase()).getValue());
            });

        } catch (IOException e) {
            System.err.println("Failed to read archive directory: " + e.getMessage());
        }
    }

    /*
    Used for disabling editing when viewing an archive
     */
    private void toggleActionButtons(boolean enabled) {
        AddButton.setDisable(!enabled);
        ModifyButton.setDisable(!enabled);
        RemoveButton.setDisable(!enabled);
    }

    /*
    Recalculates and updates the UI text for all monthly statistics.
     */
    private void updateStatistics() {
        if (monthlyDataset == null || monthlyDataset.getSessions().isEmpty()) {
            lblTotalClimbs.setText("0");
            lblLatestSession.setText("--");
            lblHardestGrade.setText("--");
            lblTimesClimbed.setText("0");
            lblTotalCompleted.setText("0");
            return;
        }

        long totalClimbs = monthlyDataset.getSessions().stream()
                .mapToLong(Session::getAttemptsCount)
                .sum();
        lblTotalClimbs.setText(String.valueOf(totalClimbs));

        long totalCompleted = monthlyDataset.getSessions().stream()
                .mapToLong(Session::getCompletedCount)
                .sum();
        lblTotalCompleted.setText(String.valueOf(totalCompleted));

        int timesClimbed = monthlyDataset.getSessions().size();
        lblTimesClimbed.setText(String.valueOf(timesClimbed));

        String latestDate = monthlyDataset.getSessions().stream()
                .map(Session::getDate)
                .max(String::compareTo)
                .orElse("--");
        lblLatestSession.setText(latestDate);

        String hardestGrade = monthlyDataset.getSessions().stream()
                .flatMap(s -> s.getRoutes().stream())
                .filter(Route::completed)
                .map(Route::grade)
                .max(Comparator.naturalOrder())
                .orElse("--");
        lblHardestGrade.setText(hardestGrade);
    }

    private void createNewDataset(Path path) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        LocalDate now = LocalDate.now();
        this.monthlyDataset = new MonthlyDataset( // Inject metadata
                FXCollections.observableArrayList(),
                now.getYear(),
                now.getMonthValue()
        );
        jsonDataService.saveMonthlyDataset(monthlyDataset, path);
    }

    private void archiveDataset(Path archiveDir, MonthlyDataset datasetToArchive) throws IOException {
        Files.createDirectories(archiveDir);
        String newFileName = String.format("%s_%s.json", datasetToArchive.getYear(), datasetToArchive.getMonth());
        Path savePath = archiveDir.resolve(newFileName);
        jsonDataService.saveMonthlyDataset(datasetToArchive, savePath);
        System.out.println("Successfully archived old dataset to: " + savePath);
    }

    private void removeSelectedSession() {
        if (isViewingArchive) return; // Prevent changing old history records

        Session selectedSession = MonthlyTable.getSelectionModel().getSelectedItem();
        if (selectedSession == null) return;

        monthlyDataset.getSessions().remove(selectedSession);
        updateStatistics();

        try {
            jsonDataService.saveMonthlyDataset(monthlyDataset, SAVE_FILE_CURRENT);
            System.out.println("Session successfully removed and saved to disk.");
        } catch (IOException e) {
            System.err.println("Failed to save dataset after removing session: " + e.getMessage());
        }
    }

    private void openSessionWindow(String windowTitle) {
        if (isViewingArchive) return; // Guard against writing to old files

        LocalDate now = LocalDate.now();

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("session-edit.fxml"));
            Parent root = loader.load();
            SessionController controller = loader.getController();

            if (windowTitle.equals("Add Session")) {
                String uuid = UUID.randomUUID().toString(); // Generate a random UUID
                controller.setSession(new Session(uuid, Integer.toString(now.getDayOfMonth())));
            } else if (windowTitle.equals("Modify Session")) {
                Session selectedSession = MonthlyTable.getSelectionModel().getSelectedItem();
                if (selectedSession == null) {
                    return;
                }
                controller.setSession(selectedSession);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(windowTitle);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            /*
            Saving logic
             */
            if (controller.isConfirmedSave()) {
                Session handledSession = controller.getSession();

                if (!windowTitle.equals("Modify Session")) {
                    monthlyDataset.getSessions().add(handledSession);
                } else {
                    MonthlyTable.refresh();
                }

                updateStatistics();

                try {
                    jsonDataService.saveMonthlyDataset(monthlyDataset, SAVE_FILE_CURRENT);
                    System.out.println("Dataset successfully saved after modification.");
                } catch (IOException e) {
                    System.err.println("Failed to auto-save dataset: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeApplication() {
        Platform.exit();
        System.exit(0);
    }
}