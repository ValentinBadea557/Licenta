package ro.mta.licenta.badea.employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.mta.licenta.badea.models.GeneralTaskModel;
import ro.mta.licenta.badea.models.ResourceModel;
import ro.mta.licenta.badea.models.TaskModel;
import ro.mta.licenta.badea.temporalUse.SelectedWorkersIDs;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ThirdCreateProjectController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableColumn<GeneralTaskModel, String> allGeneralTaskColumn;

    @FXML
    private TableView<GeneralTaskModel> tableAllGeneralTasks;

    @FXML
    private Button addTaskButton;

    @FXML
    private Button allocResourceButton;

    @FXML
    private Button createTaskButton;

    @FXML
    private DatePicker deadlineDateField;

    @FXML
    private ComboBox<String> deadlineTimeField;

    @FXML
    private Button deleteTaskButton;

    @FXML
    private Spinner<Integer> durationField;

    @FXML
    private RadioButton noRadioButton;

    @FXML
    private ComboBox<String> periodicityField;

    @FXML
    private TableColumn<ResourceModel, String> resourceNameColumn;

    @FXML
    private DatePicker startDateField;

    @FXML
    private ComboBox<String> startTimeField;

    @FXML
    private TableColumn<GeneralTaskModel, String> tableGeneralColumn;

    @FXML
    private TableView<GeneralTaskModel> tableGeneralView;

    @FXML
    private TableView<ResourceModel> tableResourcesView;

    @FXML
    private TableView<TaskModel> tableTaskParinte;

    @FXML
    private TableView<TaskModel> tableTasksView;

    @FXML
    private TableColumn<TaskModel, String> taskNameColumn;

    @FXML
    private TextField taskNameField;

    @FXML
    private TableColumn<TaskModel, String> taskParinteColumn;

    @FXML
    private RadioButton yesRadioButton;

    @FXML
    private Label allResourceLabel;

    @FXML
    private Label selectGeneralTaskLabel;

    @FXML
    private Label taskParinteLabel;

    public void YesRadioAction(ActionEvent actionEvent) {
        allResourceLabel.setVisible(false);
        allocResourceButton.setVisible(false);
        tableResourcesView.setVisible(false);
        selectGeneralTaskLabel.setVisible(false);
        tableGeneralView.setVisible(false);
        taskParinteLabel.setVisible(false);
        tableTaskParinte.setVisible(false);

    }

    public void noRadioAction(ActionEvent actionEvent) {
        allResourceLabel.setVisible(true);
        allocResourceButton.setVisible(true);
        tableResourcesView.setVisible(true);
        selectGeneralTaskLabel.setVisible(true);
        tableGeneralView.setVisible(true);
        taskParinteLabel.setVisible(true);
        tableTaskParinte.setVisible(true);
    }

    public void createTaskAction(ActionEvent actionEvent) {
        /**Check every field*/

        boolean emptyFields = false;
        if (taskNameField.getText() == null || taskNameField.getText().trim().isEmpty()) {
            emptyFields = true;
        }
        if (periodicityField.getValue() == null || periodicityField.getValue().trim().isEmpty()) {
            emptyFields = true;
        }
        if (durationField.getValue() == null) {
            emptyFields = true;
        }
        if (startDateField.getValue() == null) {
            emptyFields = true;
        }
        if (startTimeField.getValue() == null || startTimeField.getValue().trim().isEmpty()) {
            emptyFields = true;
        }
        if (deadlineDateField.getValue() == null) {
            emptyFields = true;
        }
        if (deadlineTimeField.getValue() == null || startTimeField.getValue().trim().isEmpty()) {
            emptyFields = true;
        }

        /**check if the desire task is general or not*/
        boolean generalTask = false;
        if (yesRadioButton.isSelected()) {
            generalTask = true;
        } else if (noRadioButton.isSelected()) {
            generalTask = false;
        }

        String name = null;
        String periodicity = null;
        int duration = 0;
        LocalDate startDate;
        String startTime;
        LocalDateTime finalstarttime = null;
        LocalDate deadDate;
        String deadTime;
        LocalDateTime finaldeadline = null;

        if (emptyFields) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete fields");
            alert.setContentText("You must complete all fields to create a task!");
            alert.showAndWait();
        } else {
            name = taskNameField.getText();
            periodicity = periodicityField.getValue();
            duration = durationField.getValue();

            startDate = startDateField.getValue();
            startTime = startTimeField.getValue();
            finalstarttime = returnFinalDateTimeFormat(startDate, startTime);

            deadDate = deadlineDateField.getValue();
            deadTime = deadlineTimeField.getValue();
            finaldeadline = returnFinalDateTimeFormat(deadDate, deadTime);

            System.out.println("OK");


            if (noRadioButton.isSelected()) {
                if (listaResurse.isEmpty() || tableGeneralView.getSelectionModel().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Incomplete fields");
                    alert.setContentText("You must select every detail to create a task!");
                    alert.showAndWait();
                } else {
                    System.out.println("OK");
                }
            } else {
                GeneralTaskModel general = new GeneralTaskModel();
                general.setName(name);
                general.setPeriodicity(periodicity);
                general.setDuration(duration);
                general.setStarttime(finalstarttime);
                general.setDeadline(finaldeadline);

                taskuriGenerale.add(general);
                tableGeneralView.setItems(taskuriGenerale);
                tableAllGeneralTasks.setItems(taskuriGenerale);

                System.out.println(name+" "+periodicity+" "+duration+" "+finalstarttime+" "+finaldeadline+" ");
                /** Dupa ce fac serverul fac request sa primesc ID-ul**/
            }
        }

        // listaResurse.clear();
    }

    ObservableList<ResourceModel> listaResurse = FXCollections.observableArrayList();

    private ObservableList<TaskModel> taskuriNormale = FXCollections.observableArrayList();
    private ObservableList<GeneralTaskModel> taskuriGenerale = FXCollections.observableArrayList();

    public void allocResourceAction(ActionEvent actionEvent) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/MiniPages/AddResourcesToTask.fxml"));

        SelectedWorkersIDs resourceList = new SelectedWorkersIDs();
        resourceList.clearResourceList();

        scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("test");
        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.showAndWait();


        resourceList.printResourceList();
        for (int i = 0; i < resourceList.returnSizeOfResourceList(); i++) {
            listaResurse.add(resourceList.getResourceBasedOnIndex(i));
        }
        tableResourcesView.setItems(listaResurse);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**Set combo boxes*/
        startTimeField.getItems().clear();
        startTimeField.setItems(hours);
        deadlineTimeField.getItems().clear();
        deadlineTimeField.setItems(hours);

        periodicityField.getItems().clear();
        periodicityField.setItems(periodicityIntervals);

        /**Set group for radio buttons*/
        final ToggleGroup group = new ToggleGroup();
        noRadioButton.setToggleGroup(group);
        noRadioButton.setSelected(false);

        yesRadioButton.setToggleGroup(group);
        yesRadioButton.setSelected(true);

        /**Hide tables for normal tasks*/
        allResourceLabel.setVisible(false);
        allocResourceButton.setVisible(false);
        tableResourcesView.setVisible(false);
        selectGeneralTaskLabel.setVisible(false);
        tableGeneralView.setVisible(false);
        taskParinteLabel.setVisible(false);
        tableTaskParinte.setVisible(false);

        /**Deactivate fields for normal task*/
        tableResourcesView.setEditable(false);
        tableGeneralView.setEditable(false);
        tableTaskParinte.setEditable(false);

        /**Set Tables*/
        allGeneralTaskColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        allGeneralTaskColumn.setStyle("-fx-alignment: CENTER");
        tableGeneralColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableGeneralColumn.setStyle("-fx-alignment: CENTER");
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        taskNameColumn.setStyle("-fx-alignment: CENTER");
        resourceNameColumn.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        resourceNameColumn.setStyle("-fx-alignment: CENTER");
        taskParinteColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        taskParinteColumn.setStyle("-fx-alignment: CENTER");

        /**Set spinner*/
        SpinnerValueFactory<Integer> spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
        spinner.setValue(1);
        durationField.setValueFactory(spinner);
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

    ObservableList<String> periodicityIntervals =
            FXCollections.observableArrayList(
                    "Daily", "Weekly", "Monthly"
            );

    ObservableList<String> hours =
            FXCollections.observableArrayList(
                    "00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30", "05:00", "05:30", "06:00", "06:30",
                    "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
                    "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30",
                    "23:00", "23:30"
            );
}
