package fi.jyu.ohj2.nico.BoulderProgress.controller;

import fi.jyu.ohj2.nico.BoulderProgress.App;
import fi.jyu.ohj2.nico.BoulderProgress.model.Session;
import javafx.application.Platform;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AddButton.setOnAction(e -> openSessionWindow("Add Session"));
        ModifyButton.setOnAction(e -> openSessionWindow("Modify Session"));
        ExitButton.setOnAction(e -> closeApplication());
    }

    private void openSessionWindow(String windowTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("session-edit.fxml"));
            Parent root = loader.load();

            SessionController controller = loader.getController();
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);

            stage.setTitle(windowTitle);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeApplication() {
        Platform.exit();
        System.exit(0);
    }
}
