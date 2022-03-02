package ro.mta.licenta.badea;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.control.*;
import org.json.JSONObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ro.mta.licenta.badea.models.EmployeeModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

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

    @FXML
    private Region rectangleShape;

    public void userlogin(ActionEvent actionEvent) throws Exception {

        checkLogin(actionEvent);
    }

    private void checkLogin(ActionEvent actionEvent) throws Exception {
        String user = usernameField.getText().toString();
        String pass = passwordField.getText().toString();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        boolean isempty = false;
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            wronglogin.setText("You didn't complete all fields!");
            isempty = true;
        } else {
            wronglogin.setText("");
        }

        if (!isempty) {
            JSONObject loginRequest = new JSONObject();
            loginRequest.put("Type", "Login");
            loginRequest.put("Username", user);
            loginRequest.put("Password", pass);

            Client client = Client.getInstance();
            client.sendText(loginRequest.toString());

            String received=client.receiveText();
            JSONObject recv=new JSONObject(received);

            if(recv.has("Response Login")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setContentText("The account does not exist in datababse!");
                alert.showAndWait();
            }else{

                client.setCurrentUser(gson.fromJson(received, EmployeeModel.class));
                if(client.getCurrentUser().getAdmin()==1){
                    root = FXMLLoader.load(getClass().getResource("/AdministratorPages/AdminPage.fxml"));
                    stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }else if(client.getCurrentUser().getAdmin()==0){
                    root = FXMLLoader.load(getClass().getResource("/EmployeePages/HomePageEmployee.fxml"));
                    stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        }


//        if(user.equals("user")){
//            try {
//                root = FXMLLoader.load(getClass().getResource("/EmployeePages/HomePageEmployee.fxml"));
//                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
//                scene = new Scene(root);
//                stage.setScene(scene);
//                stage.show();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }else {
//            try {
//                root = FXMLLoader.load(getClass().getResource("/AdministratorPages/AdminPage.fxml"));
//                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
//                scene = new Scene(root);
//                stage.setScene(scene);
//                stage.show();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        rectangleShape.setStyle("-fx-border-radius:10px;" +
                "-fx-background-radius:10px; " +
                "-fx-background-color: #f2f2f2;");
    }
}
