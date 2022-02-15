package ro.mta.licenta.badea;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public TextField username;
    public PasswordField password;
    public Button loginbutton;
    public Label wronglogin;

    public void userlogin(ActionEvent actionEvent) {

        checkLogin(actionEvent);
    }

    private void checkLogin(ActionEvent actionEvent) {
        String user=username.getText().toString();
        String pass=password.getText().toString();

        if(username.getText().isEmpty() || password.getText().isEmpty()){
            wronglogin.setText("You didn't complete all fields!");
        }else{
            wronglogin.setText("");
        }

        try {
            root = FXMLLoader.load(getClass().getResource("/AdministratorPages/AdminPage.fxml"));
            stage=(Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene=new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void userregister(ActionEvent actionEvent) {
        try {
            root = FXMLLoader.load(getClass().getResource("/RegisterPage.fxml"));
            stage=(Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene=new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
