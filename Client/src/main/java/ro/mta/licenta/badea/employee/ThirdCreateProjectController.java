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


    public void addTaskAction(ActionEvent actionEvent) {
    }

    public void deleteTaskAction(ActionEvent actionEvent) {
    }

    public void YesRadioAction(ActionEvent actionEvent) {
        allocResourceButton.setDisable(false);
        tableResourcesView.setEditable(true);
        tableGeneralView.setEditable(true);
        tableTaskParinte.setEditable(true);
    }

    public void noRadioAction(ActionEvent actionEvent) {
        allocResourceButton.setDisable(true);
        tableResourcesView.setEditable(false);
        tableGeneralView.setEditable(false);
        tableTaskParinte.setEditable(false);
    }

    public void createTaskAction(ActionEvent actionEvent) {
        String name=taskNameField.getText();
        String periodicity=periodicityField.getValue();
        int duration=durationField.getValue();
        LocalDate startDate=startDateField.getValue();
        String startTime=startTimeField.getValue();
        
        LocalDateTime finalstarttime=returnFinalDateTimeFormat(startDate,startTime);

        LocalDate dearDate=deadlineDateField.getValue();
        String dearTime=deadlineTimeField.getValue();
        LocalDateTime finaldeadline=returnFinalDateTimeFormat(dearDate,dearTime);
        
        boolean generalTask = false;
        if(yesRadioButton.isSelected()){
            generalTask=true;
        }else if(noRadioButton.isSelected()){
            generalTask=false;
        }
        
        System.out.println(name+"\n"+periodicity+"\n"+duration+"\n"+finalstarttime+"\n"+finaldeadline+"\n"+String.valueOf(generalTask));
        
        
    }

    public void allocResourceAction(ActionEvent actionEvent) throws Exception{
        root = FXMLLoader.load(getClass().getResource("/MiniPages/AddResourcesToTask.fxml"));

        scene = new Scene(root);
        Stage primaryStage=new Stage();
        primaryStage.setTitle("test");
        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.showAndWait();
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
        final ToggleGroup group=new ToggleGroup();
        noRadioButton.setToggleGroup(group);
        noRadioButton.setSelected(true);

        yesRadioButton.setToggleGroup(group);
        yesRadioButton.setSelected(false);

        /**Deactivate fields for normal task*/
        allocResourceButton.setDisable(true);
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

    public LocalDateTime returnFinalDateTimeFormat(LocalDate date, String time){
        /**get infomartion from LocalDate*/
        int year= date.getYear();
        int month=date.getMonth().getValue();
        int day=date.getDayOfMonth();

        /**get hour and minute from combobox time*/
        String[] timeTokenizer=time.split(":");

        int hour=Integer.valueOf(timeTokenizer[0]);
        int minutes=Integer.valueOf(timeTokenizer[1]);
        int seconds=0;

        /**return final format that match with SQL format*/
        LocalDateTime finalDateTime = LocalDateTime.of(year, month, day, hour, minutes, seconds);

        return finalDateTime;
    }
    
    ObservableList<String> periodicityIntervals =
            FXCollections.observableArrayList(
                    "Daily","Weekly","Monthly"
            );

    ObservableList<String> hours =
            FXCollections.observableArrayList(
                    "00:00","00:30","01:00","01:30","02:00","02:30","03:00","03:30","04:00","04:30","05:00","05:30","06:00","06:30",
                    "07:00","07:30","08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30",
                    "15:00","15:30","16:00","16:30","17:00","17:30","18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:30","22:00","22:30",
                    "23:00","23:30"
            );
}
