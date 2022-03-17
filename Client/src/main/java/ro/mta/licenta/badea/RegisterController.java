package ro.mta.licenta.badea;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import org.json.JSONObject;
import ro.mta.licenta.badea.models.CompanyModel;
import ro.mta.licenta.badea.models.EmployeeModel;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
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
    void registerAction(ActionEvent event) throws Exception {
        boolean isempty=false;
        if(usernameField.getText().isEmpty()){
            isempty=true;
            usernameField.setStyle("-fx-border-color:red");
        }else{
            usernameField.setStyle("-fx-border-color:none");
        }
        if(passwordField.getText().isEmpty()){
            isempty=true;
            passwordField.setStyle("-fx-border-color:red");
        }else{
            passwordField.setStyle("-fx-border-color:none");
        }
        if(companyNameField.getText().isEmpty()){
            isempty=true;
            companyNameField.setStyle("-fx-border-color:red");
        }else{
            companyNameField.setStyle("-fx-border-color:none");
        }
        if(firstNameField.getText().isEmpty()){
            isempty=true;
            firstNameField.setStyle("-fx-border-color:red");
        }else{
            firstNameField.setStyle("-fx-border-color:none");
        }
        if(lastNameField.getText().isEmpty()){
            isempty=true;
            lastNameField.setStyle("-fx-border-color:red");
        }else{
            lastNameField.setStyle("-fx-border-color:none");
        }
        if(phoneField.getText().isEmpty()){
            isempty=true;
            phoneField.setStyle("-fx-border-color:red");
        }else{
            phoneField.setStyle("-fx-border-color:none");
        }
        if(addressField.getText().isEmpty()){
            isempty=true;
            addressField.setStyle("-fx-border-color:red");
        }else{
            addressField.setStyle("-fx-border-color:none");
        }
        if(emailField.getText().isEmpty()){
            isempty=true;
            emailField.setStyle("-fx-border-color:red");
        }else{
            emailField.setStyle("-fx-border-color:none");
        }

        if(isempty==true){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete fields");
            alert.setContentText("All fields are required to register!");
            alert.showAndWait();
        }else{
            String username=usernameField.getText().toString();
            String password=passwordField.getText().toString();
            String companyName=companyNameField.getText().toString();
            String firstName=firstNameField.getText().toString();
            String lastName=lastNameField.getText().toString();
            String addr=addressField.getText().toString();
            String phone=phoneField.getText().toString();
            String mail=emailField.getText().toString();


            EmployeeModel employee=new EmployeeModel();
            employee.setUsername(username);
            employee.setPassword(password);
            employee.setCompany(new CompanyModel(companyName));
            employee.setFirstname(firstName);
            employee.setLastname(lastName);
            employee.setAddress(addr);
            employee.setPhone(phone);
            employee.setMail(mail);


            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.setPrettyPrinting().create();
            String jsonEmployee=gson.toJson(employee);


            JSONObject json=new JSONObject(jsonEmployee);
            json.put("Type","Register");

            Client client=Client.getInstance();
            client.sendText(json.toString());

            String received=client.receiveText();
            JSONObject json2=new JSONObject(received);
            String result=json2.getString("Result Register");
            if(result.equals("ok")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Company added!");
                alert.setContentText("Everything is ok! You can login now as administrator!");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setContentText(result);
                alert.showAndWait();
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backtologinbutton.setStyle("button-hover-color: #293241;");
        registerButton.setStyle("button-hover-color: #293241;");
    }
}
