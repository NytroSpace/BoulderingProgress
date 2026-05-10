package fi.jyu.ohj2.nico.BoulderProgress.controller;

import fi.jyu.ohj2.nico.BoulderProgress.App;
import fi.jyu.ohj2.nico.BoulderProgress.model.MonthlyDataset;
import fi.jyu.ohj2.nico.BoulderProgress.model.Session;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.table.TableColumn;
import java.awt.desktop.QuitEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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

    private MonthlyDataset monthlyDataset = new MonthlyDataset(
            FXCollections.observableArrayList(),
            2026,
            1
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AddButton.setOnAction(e -> openSessionWindow("Add Session"));
        ModifyButton.setOnAction(e -> openSessionWindow("Modify Session"));
        ExitButton.setOnAction(e -> closeApplication());
    }

    private void openSessionWindow(String windowTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("session-edit.fxml"));
            Parent root = loader.load(); // The controller is created here

            SessionController controller = loader.getController();

            /*
            If we're not modifying an existing session, we can safely assume we're making a new one.
            In that case, we create a new session and set it as the controller's active session
             */
            if (!windowTitle.equals("Modify Session")) {
                controller.setSession(new Session("1", "1")); // Doesn't randomize UUID yet, nor check the date.
            }
            else {
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
                Session createdSession = controller.getSession();

                monthlyDataset.getSessions().add(createdSession);
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
