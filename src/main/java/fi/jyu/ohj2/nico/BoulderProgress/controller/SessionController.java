package fi.jyu.ohj2.nico.BoulderProgress.controller;

import fi.jyu.ohj2.nico.BoulderProgress.App;
import fi.jyu.ohj2.nico.BoulderProgress.model.Route;
import fi.jyu.ohj2.nico.BoulderProgress.model.Session;
import fi.jyu.ohj2.nico.BoulderProgress.model.WallType;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    @FXML
    private TableView<Route> routesTable;

    @FXML private TableColumn<Route, String> gradeColumn;
    @FXML private TableColumn<Route, String> wallTypeColumn;
    @FXML private TableColumn<Route, Integer> attemptsColumn;

    // The routes list is used to contain all the routes for displaying in the routesTable and also for saving
    private final ObservableList<Route> routes = FXCollections.observableArrayList();
    private Session session;
    private boolean confirmedSave = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // region -- formatting
        /*
        Used for formatting the different cells inside the routesTable
         */
        gradeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().grade())
        );

        wallTypeColumn.setCellValueFactory(cellData -> {
            WallType type = cellData.getValue().wallType();

            return new SimpleStringProperty(type != null ? type.toString() : "");
        });

        attemptsColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().attempts())
        );
        // endregion

        AddButton2.setOnAction(e -> openRouteWindow("Add Route"));
        CancelButton2.setOnAction(e -> onClose());
        SaveButton.setOnAction(e -> onSave());
        RemoveButton.setOnAction(e -> removeSelectedRoute());

        // Used for updating the styles of the rows by adding a row controller
        routesTable.setRowFactory(tv -> new TableRow<Route>() {
            @Override
            protected void updateItem(Route item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                } else if (item.completed()) {
                    setStyle("-fx-background-color: #c8e6c9;"); // Make the row light green if it's completed
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void removeSelectedRoute() {
        Route selectedRoute = routesTable.getSelectionModel().getSelectedItem();

        if (selectedRoute == null) return;

        routes.remove(selectedRoute);
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

            /*
            After the route window is closed, we can check if something was saved. If so, we use the getter to get it.
            Then we add that route to an Observable list within the current session.
             */
            if (controller.isSaveConfirmed()) {
                Route newRoute = controller.getRoute();

                routes.add(newRoute);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void setSession(Session session) {
        this.session = session;

        routes.setAll(session.getRoutes());

        routesTable.setItems(routes);
    }

    @FXML
    private void onClose() {
        CancelButton2.getScene().getWindow().hide();
    }

    @FXML
    private void onSave() {
        this.session.getRoutes().clear(); // Clear all routes first, which avoids duplication and allows removal

        this.session.getRoutes().addAll(routes); // Add all the routes back

        confirmedSave = true;

        onClose();
    }

    public boolean isConfirmedSave() {return confirmedSave;}
    public Session getSession() {return this.session;}
}
