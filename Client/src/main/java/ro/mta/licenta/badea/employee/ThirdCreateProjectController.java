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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;
import ro.mta.licenta.badea.Client;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateDeserializer;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateSerializer;
import ro.mta.licenta.badea.models.ProjectModel;
import ro.mta.licenta.badea.models.ResourceModel;
import ro.mta.licenta.badea.models.TaskModel;
import ro.mta.licenta.badea.temporalUse.ProjectTemporalModel;
import ro.mta.licenta.badea.temporalUse.SelectedWorkersIDs;
import ro.mta.licenta.badea.temporalUse.WorkerModel;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ThirdCreateProjectController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private Button addTaskButton;

    @FXML
    private Button allocResourceButton;

    @FXML
    private Button createTaskButton;

    @FXML
    private DatePicker deadlineDateField;

    @FXML
    private Button deleteTaskButton;

    @FXML
    private Spinner<Integer> durationField;

    @FXML
    private ComboBox<String> periodicityField;

    @FXML
    private TableColumn<ResourceModel, String> resourceNameColumn;

    @FXML
    private DatePicker startDateField;

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
    private Label allResourceLabel;

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

    @FXML
    private Pane lastPane;

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
            if (periodicityIntervals.contains(periodicityField.getValue()))
                periodicityField.setStyle("-fx-border-color:none");
            else {
                emptyFields = true;
                periodicityField.setStyle("-fx-border-color:red");
            }
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
        if (deadlineDateField.getValue() == null) {
            emptyFields = true;
            deadlineDateField.setStyle("-fx-border-color:red");
        } else {
            deadlineDateField.setStyle("-fx-border-color:none");
        }


        String name = null;
        String periodicity = null;
        int duration = 0;
        LocalDate startDate;
        LocalDate deadDate;


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
            deadDate = deadlineDateField.getValue();

            if (listaResurse.isEmpty() || assignToComboBox.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Incomplete fields");
                alert.setContentText("You must select every detail to create a task!");
                alert.showAndWait();
            } else {
                TaskModel localTask = new TaskModel();
                localTask.setName(name);
                localTask.setPeriodicity(periodicity);
                localTask.setDuration(duration);
                localTask.setStarttime(startDate);
                localTask.setDeadline(deadDate);

                /**Setter resources list*/
                ArrayList<ResourceModel> localResourcelist = new ArrayList<ResourceModel>();
                for (int i = 0; i < listaResurse.size(); i++) {
                    localResourcelist.add(listaResurse.get(i));
                }
                localTask.setListaResurse(localResourcelist);
                /**Setter general task for normal task**/

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

                String selectedName = assignToComboBox.getValue().toString();
                SelectedWorkersIDs lista = new SelectedWorkersIDs();
                for (int i = 0; i < lista.listaEmployees.size(); i++) {
                    if (selectedName.equals(lista.listaEmployees.get(i).getFullName())) {
                        localTask.setExecutant(lista.listaEmployees.get(i));
                    }
                }

                /**create gson*/
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
                gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
                Gson gson = gsonBuilder.setPrettyPrinting().create();
                String stringJson = gson.toJson(localTask);
                System.out.println("***\n" + stringJson);
                clearInputFields();
            }
        }


    }

    public void finishAction(ActionEvent actionEvent) throws Exception {
        System.out.println("N:" + taskuriNormale.size());

        ProjectModel project = new ProjectModel();
        ProjectTemporalModel tempProject = new ProjectTemporalModel();
        Client client = Client.getInstance();


        project.setCoordonator(client.getCurrentUser());
        project.setNume(tempProject.getNumeProiect());
        project.setDescriere(tempProject.getDetails());
        project.setStarttime(tempProject.getStarttime());
        project.setDeadline(tempProject.getDeadline());
        project.setListaOameni(tempProject.getListaOameni());
        project.setListaEchipe(tempProject.getListaEchipe());
        project.setListaResurseCurente(tempProject.getListaResurse());

        for (int i = 0; i < taskuriNormale.size(); i++) {
            project.addNormalTask(taskuriNormale.get(i));
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        String projectJson = gson.toJson(project);
        System.out.println("***\n" + projectJson);


        JSONObject tosend = new JSONObject(projectJson);
        tosend.put("Type", "Create new Project");


        client.sendText(tosend.toString());


        String receive = client.receiveText();
        JSONObject response = new JSONObject(receive);

        System.out.println(response.toString());
        if (response.get("Final Response").equals("ok")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Project Created!");
            alert.setContentText("All information was inserted into Database!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setContentText("SQL Exception occured!");
            alert.showAndWait();
        }

        tempProject.clearAllList();
    }

    public void allocResourceAction(ActionEvent actionEvent) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/MiniPages/AddResourcesToTask.fxml"));

        SelectedWorkersIDs resourceList = new SelectedWorkersIDs();
        resourceList.clearResourceList();
        listaResurse.clear();


        scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Alloc Resources");
        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.showAndWait();


        for (int i = 0; i < resourceList.returnSizeOfResourceList(); i++) {
            listaResurse.add(resourceList.getResourceBasedOnIndex(i));  /**merge**/

        }

        tableResourcesView.setItems(listaResurse);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ProjectTemporalModel p = new ProjectTemporalModel();
        System.out.println(p.getListaResurse());

        /**Set combo box editable*/
        periodicityField.setEditable(true);


        /**Set buttons**/
        cleanButton.setStyle("button-hover-color: #293241;");
        createTaskButton.setStyle("button-hover-color: #293241;");
        finishButton.setStyle("button-hover-color: #293241;");


        periodicityField.getItems().clear();
        periodicityField.setItems(periodicityIntervals);

        /**Deactivate fields for normal task*/
        tableResourcesView.setEditable(false);
        taskuriParinteComboBox.setEditable(false);

        /**Set Tables*/
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        taskNameColumn.setStyle("-fx-alignment: CENTER");
        resourceNameColumn.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        resourceNameColumn.setStyle("-fx-alignment: CENTER");


        /**Set spinner*/
        SpinnerValueFactory<Integer> spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
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


    public void clearInputFields() {
        this.taskNameField.setText("");
        this.taskNameField.setPromptText("Name");
        this.periodicityField.setValue(null);
        this.periodicityField.getSelectionModel().clearSelection();
        this.periodicityField.setPromptText("Periodicity");
        this.startDateField.setValue(null);
        this.startDateField.setPromptText("Date");
        this.deadlineDateField.setValue(null);
        this.deadlineDateField.setPromptText("Deadline Date");

        listaResurse.clear();
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

    private ObservableList<WorkerModel> workers = FXCollections.observableArrayList();

    ObservableList<String> periodicityIntervals =
            FXCollections.observableArrayList(
                    "No Periodicity", "Daily", "Weekly", "Monthly"
            );


}


