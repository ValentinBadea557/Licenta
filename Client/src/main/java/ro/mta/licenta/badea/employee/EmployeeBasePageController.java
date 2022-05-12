package ro.mta.licenta.badea.employee;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;
import ro.mta.licenta.badea.Client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeBasePageController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private StackPane employeeStackPane;

    @FXML
    private Button createProjectButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Label menu;

    @FXML
    private Label menuback;

    @FXML
    private Button myCompanyButton;

    @FXML
    private Button personalInfoButton;

    @FXML
    private VBox slider;

    @FXML
    private Button topCreateProjectButton;

    @FXML
    private Button topHomeButton;

    @FXML
    private Button topPersonalInfoButton;

    @FXML
    private Button activeProjectsButton;

    @FXML
    private Button topActiveProjectsButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        topHomeButton.setStyle(" -fx-border-color: #660099;\n" +
                "    -fx-border-width: 0px 0px 3px 0px;");
        topCreateProjectButton.setStyle("-fx-border-color: none");
        topPersonalInfoButton.setStyle("-fx-border-color: none");


        /** Set home page*/
        try {

            Parent fxml = FXMLLoader.load(getClass().getResource("/EmployeePages/CalendarPane.fxml"));
            employeeStackPane.getChildren().removeAll();
            employeeStackPane.getChildren().setAll(fxml);


        } catch (IOException e) {
            e.printStackTrace();
        }

        /** Set events for menu*/
        slider.setTranslateX(0);
        menu.setOnMouseClicked(mouseEvent -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-200);

            slide.setOnFinished((ActionEvent e) -> {
                menu.setVisible(false);
                menuback.setVisible(true);
            });
        });

        menuback.setOnMouseClicked(mouseEvent -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-200);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e) -> {
                menu.setVisible(true);
                menuback.setVisible(false);
            });
        });
    }


    public void logoutAction(ActionEvent actionEvent) throws Exception {

        Client client = Client.getInstance();
        JSONObject jsonlogout = new JSONObject();
        jsonlogout.put("Type", "Logout");
        client.sendText(jsonlogout.toString());
        String response = client.receiveText();
        System.out.println(response);

        JSONObject responselogout = new JSONObject(response);
        if (responselogout.get("Logout Response").toString().equals("ok")) {

            root = FXMLLoader.load(getClass().getResource("/LoginPage.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
    }

    public void homeAction(ActionEvent actionEvent) throws IOException {
        topHomeButton.setStyle(" -fx-border-color: #660099;\n" +
                "    -fx-border-width: 0px 0px 3px 0px;");
        topActiveProjectsButton.setStyle("-fx-border-color: none");
        topCreateProjectButton.setStyle("-fx-border-color: none");
        topPersonalInfoButton.setStyle("-fx-border-color: none");

        Parent fxml = FXMLLoader.load(getClass().getResource("/EmployeePages/CalendarPane.fxml"));
        employeeStackPane.getChildren().removeAll();
        employeeStackPane.getChildren().setAll(fxml);

    }

    public void activeProjectAction(ActionEvent actionEvent) throws IOException {
        topActiveProjectsButton.setStyle(" -fx-border-color: #660099;\n" +
                "    -fx-border-width: 0px 0px 3px 0px;");
        topHomeButton.setStyle("-fx-border-color: none");
        topCreateProjectButton.setStyle("-fx-border-color: none");
        topPersonalInfoButton.setStyle("-fx-border-color: none");

        Parent fxml = FXMLLoader.load(getClass().getResource("/EmployeePages/ViewProjects.fxml"));
        employeeStackPane.getChildren().removeAll();
        employeeStackPane.getChildren().setAll(fxml);

    }

    public void createProjectAction(ActionEvent actionEvent) throws IOException {
        topCreateProjectButton.setStyle(" -fx-border-color: #660099;\n" +
                "    -fx-border-width: 0px 0px 3px 0px;");
        topHomeButton.setStyle("-fx-border-color: none");
        topActiveProjectsButton.setStyle("-fx-border-color: none");
        topPersonalInfoButton.setStyle("-fx-border-color: none");

        Parent fxml = FXMLLoader.load(getClass().getResource("/EmployeePages/CreateProjectPane.fxml"));
        employeeStackPane.getChildren().removeAll();
        employeeStackPane.getChildren().setAll(fxml);

    }

    public void personalInfoAction(ActionEvent actionEvent) throws IOException {
    }


}
