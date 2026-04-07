package fi.jyu.ohj2.nico.BoulderProgress.controller;

import fi.jyu.ohj2.nico.BoulderProgress.model.WallType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

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
    private void onCancel() {
        Cancel.getScene().getWindow().hide();
    }

    @FXML
    private void onAdd() {
        
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
