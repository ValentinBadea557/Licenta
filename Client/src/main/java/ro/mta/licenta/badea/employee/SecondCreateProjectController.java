package ro.mta.licenta.badea.employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SecondCreateProjectController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button backLeftButton;

    @FXML
    private Pane paneMaster;


    @FXML
    private StackPane stackPaneView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Parent fxml= FXMLLoader.load(getClass().getResource("/MiniPages/AddRolesToWorkersPage.fxml"));
            stackPaneView.getChildren().removeAll();
            stackPaneView.getChildren().setAll(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backLeftAction(ActionEvent actionEvent) throws Exception{
        paneMaster.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("/EmployeePages/CreateProjectPane.fxml")));

    }
}
