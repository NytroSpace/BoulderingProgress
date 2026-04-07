package fi.jyu.ohj2.nico.BoulderProgress.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import javax.swing.table.TableColumn;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button addButton;

    @FXML
    private Button modButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button exitButton;

    @FXML
    private TableColumn order;

    @FXML
    private TableColumn date;

    @FXML
    private TableColumn hardestGrade;

    @FXML
    private TableColumn totalClimbs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Write initialization code here
    }
}
