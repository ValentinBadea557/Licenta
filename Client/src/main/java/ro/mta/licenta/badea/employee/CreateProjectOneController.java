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
import javafx.scene.layout.Region;
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
    private Label infoLabelField;

    @FXML
    private Region regionShape;

    private ObservableList<WorkerModel> workers = FXCollections.observableArrayList();

    @FXML
    void addCoworkersAction(ActionEvent actionEvent) throws Exception {
        ProjectTemporalModel p = new ProjectTemporalModel();
        boolean isempty = false;
        if (startDateField.getValue() == null ) {
            isempty = true;
            startDateField.setStyle("-fx-border-color:red");
        } else {
            startDateField.setStyle("-fx-border-color:none");
        }
        if (deadlineDateField.getValue() == null) {
            isempty = true;
            deadlineDateField.setStyle("-fx-border-color:red");
        } else {
            deadlineDateField.setStyle("-fx-border-color:none");
        }

        if(!isempty){
            p.setStarttime(startDateField.getValue());
            p.setDeadline(deadlineDateField.getValue());

            root = FXMLLoader.load(getClass().getResource("/MiniPages/AddPeoplePage.fxml"));
            SelectedWorkersIDs lista = new SelectedWorkersIDs();

            lista.finalList.clear();
            lista.listaIDs.clear();
            lista.listaEmployees.clear();
            workers.clear();

            scene = new Scene(root);
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Add coworker");
            primaryStage.setScene(scene);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.showAndWait();
            /** Wait for the second stage to close*/

            /** Copy the observable list*/
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

            for (int i = 0; i < lista.getSizeOfEmployeesList(); i++) {
                project.addEmployee(lista.getListaEmployees().get(i));
            }
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
        infoLabelField.setWrapText(true);

        descriptionField.setWrapText(true);

        regionShape.setStyle("-fx-border-radius:10px;" +
                "-fx-background-radius:10px; " +
                "-fx-background-color: #98c1d9; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 3px; ");

        nextPageButton.setStyle("button-hover-color: #98c1d9; ");

        ProjectTemporalModel project = new ProjectTemporalModel();
        project.clearAllList();

        setDatePicker();
    }

    public void setDatePicker(){
        startDateField.setValue(LocalDate.now());
        deadlineDateField.setValue(LocalDate.now().plusDays(1));

        deadlineDateField.setDayCellFactory(d ->
                new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isBefore(startDateField.getValue()) || item.isEqual(startDateField.getValue()));

                    }
                });

        startDateField.valueProperty().addListener((ov, oldValue, newValue) -> {

            if (newValue.isAfter(deadlineDateField.getValue())){
                deadlineDateField.setValue(newValue.plusDays(1));
            }
        });
    }

    public void nextPageAction(ActionEvent actionEvent) throws Exception {
        boolean isempty = false;


        if (projectNameField.getText().isEmpty()) {
            isempty = true;
            projectNameField.setStyle("-fx-border-color:red");
        } else {
            projectNameField.setStyle("-fx-border-color:none");
        }

        if (descriptionField.getText().isEmpty()) {
            isempty = true;
            descriptionField.setStyle("-fx-border-color:red");
        } else {
            descriptionField.setStyle("-fx-border-color:none");
        }

        if (startDateField.getValue() == null) {
            isempty = true;
            startDateField.setStyle("-fx-border-color:red");
        } else {
            startDateField.setStyle("-fx-border-color:none");
        }

        if (deadlineDateField.getValue() == null) {
            isempty = true;
            deadlineDateField.setStyle("-fx-border-color:red");
        } else {
            deadlineDateField.setStyle("-fx-border-color:none");
        }

        if (listCoworkers.getItems().isEmpty()) {
            isempty = true;
            listCoworkers.setStyle("-fx-border-color:red");
        } else {
            listCoworkers.setStyle("-fx-border-color:none");
        }

        if (!isempty) {
            infoLabelField.setText("");

            String name = projectNameField.getText();
            String description = descriptionField.getText();
            LocalDate startDate = startDateField.getValue();
            LocalDate deadDate = deadlineDateField.getValue();

//            LocalDateTime finalStartTime, finalDeadline;
//            finalStartTime = returnFinalDateTimeFormat(startDate, startTime);
//            finalDeadline = returnFinalDateTimeFormat(deadDate, deadTime);


            ProjectTemporalModel newProject = new ProjectTemporalModel();
            newProject.setNumeProiect(name);
            newProject.setStarttime(startDate);
            newProject.setDeadline(deadDate);
            newProject.setDetails(description);


            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.setPrettyPrinting().create();
            paneMaster.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("/EmployeePages/SecondCreateProjectPane.fxml")));

        } else {

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
        int seconds = 1;

        /**return final format that match with SQL format*/
        LocalDateTime finalDateTime = LocalDateTime.of(year, month, day, hour, minutes, seconds);

        return finalDateTime;
    }
}
