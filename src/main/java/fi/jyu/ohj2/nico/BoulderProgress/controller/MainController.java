package fi.jyu.ohj2.nico.BoulderProgress.controller;

import fi.jyu.ohj2.nico.BoulderProgress.model.Session;
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
        AddButton.setOnAction(e -> openNewSession("Add Session"));
    }


    private void openNewSession(String windowTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fi/jyu/ohj2/nico/BoulderProgress/session-edit.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(windowTitle);

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading window");
        }
    }
}
