package fi.jyu.ohj2.nico.BoulderProgress.controller;

import fi.jyu.ohj2.nico.BoulderProgress.model.Route;
import fi.jyu.ohj2.nico.BoulderProgress.model.WallType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

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
    private CheckBox IsCompleted;

    @FXML
    private TextField TextGrade;

    @FXML
    private TextField TextAttempts;

    private ObservableList<Route> routes;

    // Session controller calls this before opening the window.
    public void setRoutes(ObservableList<Route> routes) {
        this.routes = routes;
    }

    private boolean validateRoute() {

        TextGrade.setStyle("");
        TextAttempts.setStyle("");

        String grade = TextGrade.getText();
        // in 2 parts, first fetched as text then converts to int
        int attempts = Integer.parseInt(TextAttempts.getText());

        if (!gradeCheck(grade)) {
            TextGrade.setStyle("-fx-border-color: red; -fx-background-color: #ffcccc;");
            return false;
        }

        // Highlights the field in red color if attempts are empty or less than 1
        if (attempts < 1) {
            // Highlight as red if attempts is blank
            TextAttempts.setStyle("-fx-border-color: red; -fx-background-color: #ffcccc;");
            return false;
        }

        return true;
    }

    public boolean gradeCheck(String grade) {

        if (grade.isEmpty() || grade.length() > 3) return false;

        char n = grade.charAt(0);

        if (n < '3' || n > '9') return false;

        if (n <= '5') {
            if (grade.length() == 1) return true;
            if (grade.length() == 2 && grade.charAt(1) == '+') return true;
            return false;
        }

        if (n <= '9') {
            if (grade.length() < 2) return false;

            char l = grade.charAt(1);

            if (l != 'a' && l != 'b' && l != 'c') return false;

            if (grade.length() == 2) return true;
            if (grade.charAt(2) == '+') return true;
        }

        return false;

    }


    @FXML
    private void onAdd() {
        if(!validateRoute()) {
          return;
        }

        String grade = TextGrade.getText();
        int attempts = Integer.parseInt(TextAttempts.getText());
        WallType wall = WallTypeBox.getValue();
        boolean completed = IsCompleted.isSelected();

        Route route = new Route(
                grade,
                attempts,
                wall,
                completed,
                UUID.randomUUID()
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
        Cancel.setOnAction(e -> onCancel());
        AddRoute.setOnAction(e -> onAdd());

    }

    /*
     * If cancel is pressed, closes the route window.
     */
    @FXML
    private void onCancel() {
        Cancel.getScene().getWindow().hide();
    }
}
