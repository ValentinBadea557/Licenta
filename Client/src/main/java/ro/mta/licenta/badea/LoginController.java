package ro.mta.licenta.badea;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    @FXML
    private Button loginbutton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerbutton;

    @FXML
    private TextField usernameField;

    @FXML
    private Label wronglogin;


    public void userlogin(ActionEvent actionEvent) {

        checkLogin(actionEvent);
    }

    private void checkLogin(ActionEvent actionEvent) {
        String user=usernameField.getText().toString();
        String pass=passwordField.getText().toString();

        if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty()){
            wronglogin.setText("You didn't complete all fields!");
        }else{
            wronglogin.setText("");
        }

        if(user.equals("user")){
            try {
                root = FXMLLoader.load(getClass().getResource("/EmployeePages/HomePageEmployee.fxml"));
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                root = FXMLLoader.load(getClass().getResource("/AdministratorPages/AdminPage.fxml"));
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void userregister(ActionEvent actionEvent) {

        try {
            root = FXMLLoader.load(getClass().getResource("/RegisterPage.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
