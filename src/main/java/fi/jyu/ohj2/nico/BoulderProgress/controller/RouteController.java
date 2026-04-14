package fi.jyu.ohj2.nico.BoulderProgress.controller;

import fi.jyu.ohj2.nico.BoulderProgress.model.Route;
import fi.jyu.ohj2.nico.BoulderProgress.model.WallType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RouteController implements Initializable {

    /*
     * Initializing the windows comboBox
     */
    @FXML
    private ComboBox<WallType> WallTypeBox;

    @FXML
    private Button AddRoute;

    @FXML
    private Button Cancel;

    @FXML
    private TextField TextGrade;

    @FXML
    private TextField TextAttempts;

    private ObservableList<Route> routes;

    // Lisätty: Session-kontrolleri kutsuu tätä ennen ikkunan avaamista
    public void setRoutes(ObservableList<Route> routes) {
        this.routes = routes;
    }

    private boolean validateRoute() {

        TextGrade.setStyle("");
        TextAttempts.setStyle("");

        String grade = TextGrade.getText();
        String attempts = TextAttempts.getText();

        if (grade.isBlank()) {
            // Highlight as red if grade is blank
            TextGrade.setStyle("-fx-border-color: red; -fx-background-color: #ffcccc;");
            return false;
        }

        if (attempts.isBlank()) {
            // Highlight as red if attempts is blank
            TextAttempts.setStyle("-fx-border-color: red; -fx-background-color: #ffcccc;");
            return false;
        }
        return true;
    }

    /*
     * If cancel is pressed, closes the route window.
     */
    @FXML
    private void onCancel() {
        Cancel.getScene().getWindow().hide();
    }

    @FXML
    private void onAdd() {
        if(!validateRoute()) {
            return;
        }

        Route route = new Route(
                TextGrade.getText(),
                TextAttempts.getText(),
                WallTypeBox.getValue()
        );

        routes.add(route);

        TextGrade.clear();
        TextAttempts.clear();
    }

    /**
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        WallTypeBox.setItems(FXCollections.observableArrayList(WallType.values()));

    }
}
