package fi.jyu.ohj2.nico.BoulderProgress.controller;

import fi.jyu.ohj2.nico.BoulderProgress.model.Route;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class SessionController implements Initializable {

    /*
     * Route list. If a route gets added via route window, it's saved to this list.
     */
    @FXML
    private ListView<String> routeList;

    /*
     * Route list. If a route gets added via route window, it's saved to this list.
     */
    private ObservableList<Route> routes = FXCollections.observableArrayList();

    /**
     * Getter for routes from the route window.
     * @return routes from the route window
     */
    public ObservableList<String> getRoutes()
    {
        return routes;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        routeList.setItems(routes);
    }
}
