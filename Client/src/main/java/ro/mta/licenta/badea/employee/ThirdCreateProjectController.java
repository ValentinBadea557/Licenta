package ro.mta.licenta.badea.employee;

import com.google.gson.*;
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
import org.json.JSONObject;
import ro.mta.licenta.badea.Client;
import ro.mta.licenta.badea.models.GeneralTaskModel;
import ro.mta.licenta.badea.models.ProjectModel;
import ro.mta.licenta.badea.models.ResourceModel;
import ro.mta.licenta.badea.models.TaskModel;
import ro.mta.licenta.badea.temporalUse.ProjectTemporalModel;
import ro.mta.licenta.badea.temporalUse.SelectedWorkersIDs;
import ro.mta.licenta.badea.temporalUse.WorkerModel;

import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
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
    private TableView<TaskModel> tableTasksView;

    @FXML
    private TableColumn<TaskModel, String> taskNameColumn;

    @FXML
    private TextField taskNameField;

    @FXML
    private ComboBox<String> taskuriParinteComboBox;

    @FXML
    private RadioButton yesRadioButton;

    @FXML
    private Label allResourceLabel;

    @FXML
    private Label selectGeneralTaskLabel;

    @FXML
    private Label taskParinteLabel;

    @FXML
    private ComboBox<String> assignToComboBox;

    @FXML
    private Button finishButton;

    @FXML
    private Label assignToLabel;

    @FXML
    private Button cleanButton;

    public void YesRadioAction(ActionEvent actionEvent) {
        allResourceLabel.setVisible(false);
        allocResourceButton.setVisible(false);
        tableResourcesView.setVisible(false);
        selectGeneralTaskLabel.setVisible(false);
        tableGeneralView.setVisible(false);
        taskParinteLabel.setVisible(false);
        taskuriParinteComboBox.setVisible(false);
        assignToComboBox.setVisible(false);
        assignToLabel.setVisible(false);
    }

    public void noRadioAction(ActionEvent actionEvent) {
        allResourceLabel.setVisible(true);
        allocResourceButton.setVisible(true);
        tableResourcesView.setVisible(true);
        selectGeneralTaskLabel.setVisible(true);
        tableGeneralView.setVisible(true);
        taskParinteLabel.setVisible(true);
        taskuriParinteComboBox.setVisible(true);
        assignToComboBox.setVisible(true);
        assignToLabel.setVisible(true);

    }

    public void createTaskAction(ActionEvent actionEvent) {
        /**Check every field*/

        boolean emptyFields = false;
        if (taskNameField.getText() == null || taskNameField.getText().trim().isEmpty()) {
            emptyFields = true;
            taskNameField.setStyle("-fx-border-color:red");
        } else {
            taskNameField.setStyle("-fx-border-color:none");
        }
        if (periodicityField.getValue() == null || periodicityField.getValue().trim().isEmpty()) {
            emptyFields = true;
            periodicityField.setStyle("-fx-border-color:red");
        } else {
            periodicityField.setStyle("-fx-border-color:none");
        }
        if (durationField.getValue() == null) {
            emptyFields = true;
            durationField.setStyle("-fx-border-color:red");
        } else {
            durationField.setStyle("-fx-border-color:none");
        }
        if (startDateField.getValue() == null) {
            emptyFields = true;
            startDateField.setStyle("-fx-border-color:red");
        } else {
            startDateField.setStyle("-fx-border-color:none");
        }
        if (startTimeField.getValue() == null) {
            emptyFields = true;
            startTimeField.setStyle("-fx-border-color:red");
        } else {
            startTimeField.setStyle("-fx-border-color:none");
        }
        if (deadlineDateField.getValue() == null) {
            emptyFields = true;
            deadlineDateField.setStyle("-fx-border-color:red");
        } else {
            deadlineDateField.setStyle("-fx-border-color:none");
        }
        if (deadlineTimeField.getValue() == null) {
            emptyFields = true;
            deadlineTimeField.setStyle("-fx-border-color:red");
        } else {
            deadlineTimeField.setStyle("-fx-border-color:none");
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


            if (noRadioButton.isSelected()) {
                if (listaResurse.isEmpty() || tableGeneralView.getSelectionModel().isEmpty() || assignToComboBox.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Incomplete fields");
                    alert.setContentText("You must select every detail to create a task!");
                    alert.showAndWait();
                } else {
                    TaskModel localTask = new TaskModel();
                    localTask.setName(name);
                    localTask.setPeriodicity(periodicity);
                    localTask.setDuration(duration);
                    localTask.setStarttime(finalstarttime);
                    localTask.setDeadline(finaldeadline);

                    /**Setter resources list*/
                    ArrayList<ResourceModel> localResourcelist = new ArrayList<ResourceModel>();
                    for (int i = 0; i < listaResurse.size(); i++) {
                        localResourcelist.add(listaResurse.get(i));
                    }
                    localTask.setListaResurse(localResourcelist);
                    /**Setter general task for normal task**/

                    localTask.setTaskGeneral(tableGeneralView.getSelectionModel().getSelectedItem());
                    localTask.printTaskInformation();

                    /**Add in local List and also in table views*/
                    taskuriNormale.add(localTask);
                    listaNumeTaskuri.add(localTask.getName());

                    tableTasksView.setItems(taskuriNormale);
                    taskuriParinteComboBox.setItems(listaNumeTaskuri);

                    if (!taskuriParinteComboBox.getSelectionModel().isEmpty() || taskuriParinteComboBox.getValue() != null) {
                        /**Set parent task*/
                        TaskModel parent = new TaskModel();
                        for (int i = 0; i < taskuriNormale.size(); i++) {
                            String selectedItem = taskuriParinteComboBox.getValue().toString();
                            if (selectedItem.equals(taskuriNormale.get(i).getName())) {
                                parent = taskuriNormale.get(i);
                                localTask.setParinte(parent);
                            }
                        }
                    }

                    String selectedName=assignToComboBox.getValue().toString();
                    SelectedWorkersIDs lista=new SelectedWorkersIDs();
                    for(int i=0;i<lista.listaEmployees.size();i++){
                        if(selectedName.equals(lista.listaEmployees.get(i).getFullName())){
                            localTask.setExecutant(lista.listaEmployees.get(i));
                        }
                    }

                    /**create gson*/
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
                    gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
                    Gson gson = gsonBuilder.setPrettyPrinting().create();
                    String stringJson = gson.toJson(localTask);
                    System.out.println("***\n" + stringJson);
                    clearInputFields();
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

                System.out.println("TASK GENERAL\n");
                System.out.println(name + " " + periodicity + " " + duration + " " + finalstarttime + " " + finaldeadline + " ");

                /**Set gson to serialize localDateTime members**/
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
                gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
                Gson gson = gsonBuilder.setPrettyPrinting().create();


                String stringJson = gson.toJson(general);
                System.out.println("***\n" + stringJson);
                GeneralTaskModel aux = gson.fromJson(stringJson, GeneralTaskModel.class);
                System.out.println("Nume: " + aux.getName() + " Start:" + aux.getStarttime() + " Dead:" + aux.getDeadline());
                clearInputFields();
            }

        }


    }

    public void finishAction(ActionEvent actionEvent) throws Exception {
        System.out.println("N:"+taskuriNormale.size()+" g:"+taskuriGenerale.size());

        ProjectModel project = new ProjectModel();
        ProjectTemporalModel tempProject=new ProjectTemporalModel();
        Client client= Client.getInstance();


        project.setCoordonator(client.getCurrentUser());
        project.setNume(tempProject.getNumeProiect());
        project.setDescriere(tempProject.getDetails());
        project.setStarttime(tempProject.getStarttime());
        project.setDeadline(tempProject.getDeadline());
        project.setListaOameni(tempProject.getListaOameni());
        project.setListaEchipe(tempProject.getListaEchipe());
        for(int i=0;i<taskuriNormale.size();i++){
            project.addNormalTask(taskuriNormale.get(i));
        }
        for(int i=0;i<taskuriGenerale.size();i++){
            project.addGeneralTask(taskuriGenerale.get(i));
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        String projectJson = gson.toJson(project);
        System.out.println("***\n" + projectJson);

        JSONObject tosend=new JSONObject(projectJson);
        tosend.put("Type","Create new Project");
        client.sendText(tosend.toString());

    }

    public void allocResourceAction(ActionEvent actionEvent) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/MiniPages/AddResourcesToTask.fxml"));

        SelectedWorkersIDs resourceList = new SelectedWorkersIDs();
        resourceList.clearResourceList();
        listaResurse.clear();

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
        System.out.println("Size1:" + listaResurse.size());
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
        taskuriParinteComboBox.setVisible(false);
        assignToComboBox.setVisible(false);
        assignToLabel.setVisible(false);

        /**Deactivate fields for normal task*/
        tableResourcesView.setEditable(false);
        tableGeneralView.setEditable(false);
        taskuriParinteComboBox.setEditable(false);

        /**Set Tables*/
        allGeneralTaskColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        allGeneralTaskColumn.setStyle("-fx-alignment: CENTER");
        tableGeneralColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableGeneralColumn.setStyle("-fx-alignment: CENTER");
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        taskNameColumn.setStyle("-fx-alignment: CENTER");
        resourceNameColumn.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        resourceNameColumn.setStyle("-fx-alignment: CENTER");


        /**Set spinner*/
        SpinnerValueFactory<Integer> spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 24);
        spinner.setValue(1);
        durationField.setValueFactory(spinner);

        /**Set combo box assign to*/
        SelectedWorkersIDs lista = new SelectedWorkersIDs();
        ObservableList<String> listaNumeEmployees = FXCollections.observableArrayList();
        for (int i = 0; i < lista.finalList.size(); i++) {
            workers.add(lista.finalList.get(i));
            listaNumeEmployees.add(workers.get(i).getFullName());
        }
        assignToComboBox.setItems(listaNumeEmployees);
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

    public void clearInputFields() {
        this.taskNameField.setText("");
        this.taskNameField.setPromptText("Name");
        this.periodicityField.setValue(null);
        this.periodicityField.getSelectionModel().clearSelection();
        this.periodicityField.setPromptText("Periodicity");
        this.startDateField.setValue(null);
        this.startDateField.setPromptText("Date");
        this.startTimeField.setValue(null);
        this.startTimeField.getSelectionModel().clearSelection();
        this.startTimeField.setPromptText("Start time");
        this.deadlineDateField.setValue(null);
        this.deadlineDateField.setPromptText("Deadline Date");
        this.deadlineTimeField.setValue(null);
        this.deadlineTimeField.setPromptText("Deadline Time");
        this.deadlineTimeField.getSelectionModel().clearSelection();
        listaResurse.clear();
        if (noRadioButton.isSelected()) {
            tableResourcesView.setItems(listaResurse);
        }
        taskuriParinteComboBox.setValue(null);
        taskuriParinteComboBox.setPromptText("Precedessor");

        assignToComboBox.setValue(null);
        assignToComboBox.setPromptText("Select an employee");
    }

    public void cleanAction(ActionEvent actionEvent) {
        clearInputFields();
    }

    private ObservableList<String> listaNumeTaskuri = FXCollections.observableArrayList();

    private ObservableList<ResourceModel> listaResurse = FXCollections.observableArrayList();

    private ObservableList<TaskModel> taskuriNormale = FXCollections.observableArrayList();

    private ObservableList<GeneralTaskModel> taskuriGenerale = FXCollections.observableArrayList();

    private ObservableList<WorkerModel> workers = FXCollections.observableArrayList();

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


class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss");

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(formatter.format(localDateTime));
    }
}

class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(),
                DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss").withLocale(Locale.ENGLISH));
    }
}