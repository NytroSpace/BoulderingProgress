package fi.jyu.ohj2.nico.BoulderProgress.controller;

import fi.jyu.ohj2.nico.BoulderProgress.App;
import fi.jyu.ohj2.nico.BoulderProgress.model.Route;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SessionController implements Initializable {

    @FXML
    private Button AddButton2;

    @FXML
    private Button SaveButton;

    @FXML
    private Button RemoveButton;

    @FXML
    private Button CancelButton2;

    /*
     * Route list. If a route gets added via route window, it's saved to this list.
     */
    private final ObservableList<Route> routes = FXCollections.observableArrayList();

    /**
     * Getter for routes from the route window.
     * @return routes from the route window
     */
    public ObservableList<Route> getRoutes()
    {
        return routes;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        AddButton2.setOnAction(e -> openRouteWindow("Add Route"));
        CancelButton2.setOnAction(e -> onCancel2());
    }

    private void openRouteWindow(String windowTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("route-edit.fxml"));
            Parent root = loader.load();

            RouteController controller = loader.getController();
            System.out.println("Controller = " + controller);
            System.out.println("AddButton = " + controller);

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

    @FXML
    private void onCancel2() {
        CancelButton2.getScene().getWindow().hide();
    }

}
