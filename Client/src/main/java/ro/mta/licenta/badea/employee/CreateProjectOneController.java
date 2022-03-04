package ro.mta.licenta.badea.employee;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.mta.licenta.badea.temporalUse.ProjectTemporalModel;
import ro.mta.licenta.badea.temporalUse.SelectedWorkersIDs;
import ro.mta.licenta.badea.temporalUse.WorkerModel;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateProjectOneController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Pane paneMaster;

    @FXML
    private Button addCoworkerButton;

    @FXML
    private DatePicker deadlineDateField;

    @FXML
    private ComboBox<String> deadlineTimeField;

    @FXML
    private Button deleteCoworkerButton;

    @FXML
    private TextArea descriptionField;

    @FXML
    private ListView<String> listCoworkers;

    @FXML
    private Button nextPageButton;

    @FXML
    private TextField projectNameField;

    @FXML
    private DatePicker startDateField;

    @FXML
    private ComboBox<String> startTimeField;

    @FXML
    private Label infoLabelField;

    private ObservableList<WorkerModel> workers = FXCollections.observableArrayList();

    @FXML
    void addCoworkersAction(ActionEvent actionEvent) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/MiniPages/AddPeoplePage.fxml"));

        scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Add coworker");
        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.showAndWait();
        /** Wait for the second stage to close*/

        /** Copy the observable list*/
        SelectedWorkersIDs lista = new SelectedWorkersIDs();
        for (int i = 0; i < lista.finalList.size(); i++) {
            workers.add(lista.finalList.get(i));
        }

        /**Create a second list only with Names to populate the list view*/
        ObservableList<String> listaNume = FXCollections.observableArrayList();
        for (int i = 0; i < workers.size(); i++) {
            listaNume.add(workers.get(i).getFullName());
        }
        listCoworkers.setItems(listaNume);

        ProjectTemporalModel project = new ProjectTemporalModel();
        for(int i=0;i<lista.getSizeOfEmployeesList();i++){
            project.addEmployee(lista.getListaEmployees().get(i));
        }


    }

    @FXML
    void deleteCoworkersAction(ActionEvent event) {
        String selected = listCoworkers.getSelectionModel().getSelectedItem();
        listCoworkers.getItems().remove(selected);

        /**TO DO remove from object list*/
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startTimeField.getItems().clear();
        deadlineTimeField.getItems().clear();
        startTimeField.setItems(hours);
        deadlineTimeField.setItems(hours);
        descriptionField.setWrapText(true);
    }

    ObservableList<String> hours =
            FXCollections.observableArrayList(
                    "00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30", "05:00", "05:30", "06:00", "06:30",
                    "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
                    "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30",
                    "23:00", "23:30"
            );

    public void nextPageAction(ActionEvent actionEvent) throws Exception {
        boolean isempty = false;

//
//        if (projectNameField.getText().isEmpty()) {
//            isempty = true;
//            projectNameField.setStyle("-fx-border-color:red");
//        } else {
//            projectNameField.setStyle("-fx-border-color:none");
//        }
//
//        if (descriptionField.getText().isEmpty()) {
//            isempty = true;
//            descriptionField.setStyle("-fx-border-color:red");
//        } else {
//            descriptionField.setStyle("-fx-border-color:none");
//        }
//
//        if (startDateField.getValue() == null) {
//            isempty = true;
//            startDateField.setStyle("-fx-border-color:red");
//        } else {
//            startDateField.setStyle("-fx-border-color:none");
//        }
//
//        if (deadlineDateField.getValue() == null) {
//            isempty = true;
//            deadlineDateField.setStyle("-fx-border-color:red");
//        } else {
//            deadlineDateField.setStyle("-fx-border-color:none");
//        }
//
//        if (startTimeField.getValue() == null) {
//            isempty = true;
//            startTimeField.setStyle("-fx-border-color:red");
//        } else {
//            startTimeField.setStyle("-fx-border-color:none");
//        }
//
//
//
//        if (deadlineTimeField.getValue() == null) {
//            isempty = true;
//            deadlineTimeField.setStyle("-fx-border-color:red");
//        } else {
//            deadlineTimeField.setStyle("-fx-border-color:none");
//        }

        if (listCoworkers.getItems().isEmpty()) {
            isempty = true;
            listCoworkers.setStyle("-fx-border-color:red");
        } else {
            listCoworkers.setStyle("-fx-border-color:none");
        }

        if (!isempty) {
//            infoLabelField.setText("");
//
//            String name = projectNameField.getText();
//            String description = descriptionField.getText();
//            LocalDate startDate = startDateField.getValue();
//            LocalDate deadDate = deadlineDateField.getValue();
//            String startTime = startTimeField.getValue();
//            String deadTime = deadlineTimeField.getValue();
//
//
//            LocalDateTime finalStartTime, finalDeadline;
//            finalStartTime = returnFinalDateTimeFormat(startDate, startTime);
//            finalDeadline = returnFinalDateTimeFormat(deadDate, deadTime);
//
//
//            ProjectTemporalModel newProject = new ProjectTemporalModel();
//            newProject.setNumeProiect(name);
//            newProject.setStarttime(finalStartTime);
//            newProject.setDeadline(finalDeadline);
//            newProject.setDetails(description);
//
//
//            GsonBuilder gsonBuilder = new GsonBuilder();
//            Gson gson = gsonBuilder.setPrettyPrinting().create();
            paneMaster.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("/EmployeePages/SecondCreateProjectPane.fxml")));

        }else{
            infoLabelField.setText("You must complete all fields to move forward!");
        }




    }

    public LocalDateTime returnFinalDateTimeFormat(LocalDate date, String time) {
        /**get infomartion from LocalDate*/
        int year = date.getYear();
        int month = date.getMonth().getValue();
        int day = date.getDayOfMonth();

        /**get hour and minute from combobox time*/
        String[] timeTokenizer = time.split(":");

        int hour = Integer.valueOf(timeTokenizer[0]);
        int minutes = Integer.valueOf(timeTokenizer[1]);
        int seconds = 0;

        /**return final format that match with SQL format*/
        LocalDateTime finalDateTime = LocalDateTime.of(year, month, day, hour, minutes, seconds);

        return finalDateTime;
    }
}
