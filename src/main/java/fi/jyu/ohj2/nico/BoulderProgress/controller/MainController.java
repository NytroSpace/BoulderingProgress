package fi.jyu.ohj2.nico.BoulderProgress.controller;

import fi.jyu.ohj2.nico.BoulderProgress.App;
import fi.jyu.ohj2.nico.BoulderProgress.model.JsonDataService;
import fi.jyu.ohj2.nico.BoulderProgress.model.MonthlyDataset;
import fi.jyu.ohj2.nico.BoulderProgress.model.Session;
import fi.jyu.ohj2.nico.BoulderProgress.model.Route;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import java.util.ResourceBundle;
import java.util.Comparator;
import java.util.UUID;

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
    JsonDataService jsonDataService = new JsonDataService();
    private static final Path SAVE_FILE_CURRENT = Path.of("data/climbs/current/climbs.json");
    private static final Path SAVE_FILE_ARCHIVE = Path.of("data/climbs/archived");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AddButton.setOnAction(e -> openSessionWindow("Add Session"));
        ModifyButton.setOnAction(e -> openSessionWindow("Modify Session"));
        ExitButton.setOnAction(e -> closeApplication());

        RemoveButton.setOnAction(e -> removeSelectedSession());

        colNro.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(MonthlyTable.getItems().indexOf(p.getValue()) + 1)
        );

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        colHardest.setCellValueFactory(p -> {
            String hardest = p.getValue().getRoutes().stream()
                    .filter(fi.jyu.ohj2.nico.BoulderProgress.model.Route::completed)
                    .map(fi.jyu.ohj2.nico.BoulderProgress.model.Route::grade)
                    .max(String::compareTo)
                    .orElse("--");
            return new ReadOnlyObjectWrapper<>(hardest);
        });

        colMode.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>("--"));

        colTotal.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getAttemptsCount())
        );

        try {
            monthlyDataset = jsonDataService.loadMonthlyDataset(SAVE_FILE_CURRENT);

            if (monthlyDataset.getYear() != LocalDate.now().getYear() || monthlyDataset.getMonth() != LocalDate.now().getMonthValue()) {
                System.out.println("Loaded data is old. Archiving and starting new dataset.");
                try {
                    archiveDataset(SAVE_FILE_ARCHIVE, monthlyDataset);
                    createNewDataset(SAVE_FILE_CURRENT);
                } catch (IOException e) {
                    System.err.println("Critical error during monthly rollover: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("No valid save found. Creating new dataset.");
            try {
                createNewDataset(SAVE_FILE_CURRENT);
            } catch (IOException ex) {
                monthlyDataset = new MonthlyDataset(
                        FXCollections.observableArrayList(),
                        LocalDate.now().getYear(),
                        LocalDate.now().getMonthValue()
                );
                System.err.println("Could not write to disk. Changes will not be saved.");
            }
        }

        MonthlyTable.setItems(monthlyDataset.getSessions());
        updateStatistics();
    }

    /**
     * Recalculates and updates the UI text for all monthly statistics.
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
        this.monthlyDataset = new MonthlyDataset(
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
        Session selectedSession = MonthlyTable.getSelectionModel().getSelectedItem();

        if (selectedSession == null) {
            System.out.println("No session selected for removal.");
            return;
        }

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
        LocalDate now = LocalDate.now();

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("session-edit.fxml"));
            Parent root = loader.load();
            SessionController controller = loader.getController();

            if (windowTitle.equals("Add Session")) {
                String uuid = UUID.randomUUID().toString();

                controller.setSession(new Session(uuid, Integer.toString(now.getDayOfMonth())));
            } else if (windowTitle.equals("Modify Session")) {
                Session selectedSession = MonthlyTable.getSelectionModel().getSelectedItem();
                if (selectedSession != null) {
                    controller.setSession(selectedSession);
                }
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(windowTitle);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

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