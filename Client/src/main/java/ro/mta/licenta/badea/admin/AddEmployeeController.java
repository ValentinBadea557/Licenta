package ro.mta.licenta.badea.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.json.JSONObject;
import ro.mta.licenta.badea.Client;
import ro.mta.licenta.badea.models.EmployeeModel;

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
        final ToggleGroup group = new ToggleGroup();
        adminNoRadioButton.setToggleGroup(group);
        adminNoRadioButton.setSelected(true);

        adminYesRadioButton.setToggleGroup(group);
        adminYesRadioButton.setSelected(false);


    }

    public void cleanFields(){
        usernameField.clear();
        passwordField.clear();
        firstNameField.clear();
        lastNameField.clear();
        phoneField.clear();
        addressField.clear();
        emailField.clear();
    }

    public void addEmployee(ActionEvent actionEvent) throws Exception {
        /**check empty fields*/
        boolean isempty = false;
        if (usernameField.getText().isEmpty()) {
            isempty = true;
            usernameField.setStyle("-fx-border-color:red");
        } else {
            usernameField.setStyle("-fx-border-color:none");
        }
        if (passwordField.getText().isEmpty()) {
            isempty = true;
            passwordField.setStyle("-fx-border-color:red");
        } else {
            passwordField.setStyle("-fx-border-color:none");
        }
        if (firstNameField.getText().isEmpty()) {
            isempty = true;
            firstNameField.setStyle("-fx-border-color:red");
        } else {
            firstNameField.setStyle("-fx-border-color:none");
        }
        if (lastNameField.getText().isEmpty()) {
            isempty = true;
            lastNameField.setStyle("-fx-border-color:red");
        } else {
            lastNameField.setStyle("-fx-border-color:none");
        }
        if (phoneField.getText().isEmpty()) {
            isempty = true;
            phoneField.setStyle("-fx-border-color:red");
        } else {
            phoneField.setStyle("-fx-border-color:none");
        }
        if (addressField.getText().isEmpty()) {
            isempty = true;
            addressField.setStyle("-fx-border-color:red");
        } else {
            addressField.setStyle("-fx-border-color:none");
        }
        if (emailField.getText().isEmpty()) {
            isempty = true;
            emailField.setStyle("-fx-border-color:red");
        } else {
            emailField.setStyle("-fx-border-color:none");
        }

        if (isempty) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Incomplete fields");
            alert.setContentText("You must complete all fields!!");
            alert.showAndWait();
        } else {
            Client client = Client.getInstance();

            /**Get data*/
            String user = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            int admin = 0;
            if (adminYesRadioButton.isSelected()) {
                admin = 1;
            } else if (adminNoRadioButton.isSelected()) {
                admin = 0;
            }
            String firstname = firstNameField.getText().toString();
            String lastname = lastNameField.getText().toString();
            String phone = phoneField.getText().toString();
            String addr = addressField.getText().toString();
            String mail = emailField.getText().toString();

            EmployeeModel newEmployee = new EmployeeModel();
            newEmployee.setUsername(user);
            newEmployee.setPassword(password);
            newEmployee.setAdmin(admin);
            newEmployee.setFirstname(firstname);
            newEmployee.setLastname(lastname);
            newEmployee.setPhone(phone);
            newEmployee.setAddress(addr);
            newEmployee.setMail(mail);
            newEmployee.setOre_max_munca(8);
            newEmployee.setCompany(client.getCurrentUser().getCompany());

            /**Build gson*/
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.setPrettyPrinting().create();
            String employeeJson = gson.toJson(newEmployee);

            JSONObject tosend=new JSONObject(employeeJson);
            tosend.put("Type","addEmployeeToCompany");
            client.sendText(tosend.toString());

            String jsonReturned = client.receiveText();
            JSONObject json=new JSONObject(jsonReturned);
            String result=json.getString("Response Add User");
            if(result.equals("ok")){
                cleanFields();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Employee added!");
                alert.setContentText("Successful add!");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setContentText(result);
                alert.showAndWait();
            }


        }
    }
}
