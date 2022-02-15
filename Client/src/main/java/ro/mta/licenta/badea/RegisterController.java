package ro.mta.licenta.badea;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    private Stage stage;
    private Scene scene;
    private Parent root;


    public void backtologin(ActionEvent actionEvent) {
        try {
            root = FXMLLoader.load(getClass().getResource("/LoginPage.fxml"));
            stage=(Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene=new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
