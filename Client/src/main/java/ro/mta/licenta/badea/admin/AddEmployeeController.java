package ro.mta.licenta.badea.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEmployeeController implements Initializable {
    @FXML
    private TextField addressField;

    @FXML
    private RadioButton adminNoRadioButton;

    @FXML
    private RadioButton adminYesRadioButton;

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
    private TextField usernameField;

    @FXML
    private Button addEmployeeButon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**set radio buttons*/
        final ToggleGroup group=new ToggleGroup();
        adminNoRadioButton.setToggleGroup(group);
        adminNoRadioButton.setSelected(true);

        adminYesRadioButton.setToggleGroup(group);
        adminYesRadioButton.setSelected(false);


    }

    public void addEmployee(ActionEvent actionEvent) {
        /**check empty fields*/
        boolean isempty=false;
        if(usernameField.getText().isEmpty())
            isempty=true;
        if(passwordField.getText().isEmpty())
            isempty=true;
        if(firstNameField.getText().isEmpty())
            isempty=true;
        if(lastNameField.getText().isEmpty())
            isempty=true;
        if(phoneField.getText().isEmpty())
            isempty=true;
        if(addressField.getText().isEmpty())
            isempty=true;
        if(emailField.getText().isEmpty())
            isempty=true;

        if(isempty){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Incomplete fields");
            alert.setContentText("You must complete all fields!!");
            alert.showAndWait();
        }else{
            System.out.println("ok");
        }
    }
}
