package ro.mta.licenta.badea.employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.json.JSONObject;
import ro.mta.licenta.badea.Client;

import java.net.URL;
import java.util.ResourceBundle;

public class PersonalInforController implements Initializable {


    @FXML
    private TextField addressTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField firstnameTextField;

    @FXML
    private TextField lastnameTextField;

    @FXML
    private Button modifyInformationButton;

    @FXML
    private TextField phoneTextField;

    @FXML
    void modfiyInformationAction(ActionEvent event) throws Exception {
        boolean isempty = false;
        if (addressTextField.getText().isEmpty()) {
            isempty = true;
            addressTextField.setStyle("-fx-border-color:red");
        } else {
            addressTextField.setStyle("-fx-border-color:none");
        }
        if (phoneTextField.getText().isEmpty()) {
            isempty = true;
            phoneTextField.setStyle("-fx-border-color:red");
        } else {
            phoneTextField.setStyle("-fx-border-color:none");
        }
        if (emailTextField.getText().isEmpty()) {
            isempty = true;
            emailTextField.setStyle("-fx-border-color:red");
        } else {
            emailTextField.setStyle("-fx-border-color:none");
        }

        System.out.println(emailTextField.getText()+" "+addressTextField.getText()+" "+phoneTextField.getText());

        Client client=Client.getInstance();

        JSONObject request = new JSONObject();
        request.put("Type","Update Personal Info");
        request.put("IDuser",client.getCurrentUser().getID());
        request.put("Address",addressTextField.getText());
        request.put("Phone",phoneTextField.getText());
        request.put("Email",emailTextField.getText());

        client.sendText(request.toString());
        String response=client.receiveText();
        JSONObject res=new JSONObject(response);
        if(res.get("Response").equals("OK")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Result");
            alert.setContentText("All information was set!");
            alert.showAndWait();

            client.getCurrentUser().setPhone(phoneTextField.getText());
            client.getCurrentUser().setAddress(addressTextField.getText());
            client.getCurrentUser().setEmail(emailTextField.getText());
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("SQL Error");
            alert.setContentText("Some error occured! Try again later!");
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Client client = Client.getInstance();
        firstnameTextField.setText(client.getCurrentUser().getFirstname());
        lastnameTextField.setText(client.getCurrentUser().getLastname());
        addressTextField.setText(client.getCurrentUser().getAddress());
        phoneTextField.setText(client.getCurrentUser().getPhone());
        emailTextField.setText(client.getCurrentUser().getMail());

        firstnameTextField.setEditable(false);
        lastnameTextField.setEditable(false);

    }
}
