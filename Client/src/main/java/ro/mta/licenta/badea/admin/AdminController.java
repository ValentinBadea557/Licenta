package ro.mta.licenta.badea.admin;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private StackPane adminstackpane;

    @FXML
    private Label menu;

    @FXML
    private AnchorPane slider;


    @FXML
    private Label menuback;

    @FXML
    private Button topaddemployee;

    @FXML
    private Button topemployees;

    @FXML
    private Button topprojects;

    @FXML
    private Button topresources;

    @FXML
    private Button logoutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources){



        try {
            Parent fxml= FXMLLoader.load(getClass().getResource("/AdministratorPages/EmployeesPageAdmin.fxml"));
            adminstackpane.getChildren().removeAll();
            adminstackpane.getChildren().setAll(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }


        slider.setTranslateX(0);
        menu.setOnMouseClicked(mouseEvent -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-176);

            slide.setOnFinished((ActionEvent e)->{
                menu.setVisible(false);
                menuback.setVisible(true);
            });
        });


        menuback.setOnMouseClicked(mouseEvent -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-176);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)->{
                menu.setVisible(true);
                menuback.setVisible(false);
            });
        });
    }


    public void showEmployees(ActionEvent actionEvent) throws IOException{
        Parent fxml = FXMLLoader.load(getClass().getResource("/AdministratorPages/EmployeesPageAdmin.fxml"));
        adminstackpane.getChildren().removeAll();
        adminstackpane.getChildren().setAll(fxml);
    }

    public void showProjects(ActionEvent actionEvent)  throws IOException{
        Parent fxml = FXMLLoader.load(getClass().getResource("/AdministratorPages/ProjectsAdmin.fxml"));
        adminstackpane.getChildren().removeAll();
        adminstackpane.getChildren().setAll(fxml);
    }

    public void showAddEmployee(ActionEvent actionEvent)  throws IOException{
        Parent fxml = FXMLLoader.load(getClass().getResource("/AdministratorPages/AddEmployeePageAdmin.fxml"));
        adminstackpane.getChildren().removeAll();
        adminstackpane.getChildren().setAll(fxml);
    }

    public void showResources(ActionEvent actionEvent)  throws IOException{
        Parent fxml = FXMLLoader.load(getClass().getResource("/AdministratorPages/ResourcesPageAdmin.fxml"));
        adminstackpane.getChildren().removeAll();
        adminstackpane.getChildren().setAll(fxml);
    }


    public void logoutAction(ActionEvent actionEvent) {
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
