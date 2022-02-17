package ro.mta.licenta.badea;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField addressField;

    @FXML
    private Button backtologinbutton;

    @FXML
    private TextField companyNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneField;

    @FXML
    private Button registerButton;

    @FXML
    private TextField usernameField;

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

    @FXML
    void registerAction(ActionEvent event) {
        boolean isempty=false;
        if(usernameField.getText().isEmpty()){
            isempty=true;
        }
        if(passwordField.getText().isEmpty()){
            isempty=true;
        }
        if(companyNameField.getText().isEmpty()){
            isempty=true;
        }
        if(firstNameField.getText().isEmpty()){
            isempty=true;
        }
        if(lastNameField.getText().isEmpty()){
            isempty=true;
        }
        if(phoneField.getText().isEmpty()){
            isempty=true;
        }
        if(addressField.getText().isEmpty()){
            isempty=true;
        }
        if(emailField.getText().isEmpty()){
            isempty=true;
        }

        if(isempty==true){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete fields");
            alert.setContentText("All fields are required to register!");
            alert.showAndWait();
        }else{
            System.out.println("totul completat");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
