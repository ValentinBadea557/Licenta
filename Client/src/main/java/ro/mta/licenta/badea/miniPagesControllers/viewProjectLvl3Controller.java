package ro.mta.licenta.badea.miniPagesControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;
import ro.mta.licenta.badea.Client;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateDeserializer;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateSerializer;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateTimeDeserializer;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateTimeSerializer;
import ro.mta.licenta.badea.ThreadToRunLoading;
import ro.mta.licenta.badea.models.*;
import ro.mta.licenta.badea.temporalUse.SelectedWorkersIDs;
import ro.mta.licenta.badea.temporalUse.SenderText;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class viewProjectLvl3Controller implements Initializable {
    private HashMap<Integer, Integer> positionMap = new HashMap<Integer, Integer>();
    /**
     * ID task,Rand
     */
    private ProjectModel projectLocal;
    private ResourceModel selectedResource;
    private LocalDate previousDate;

    @FXML
    private Label addressLabel;

    @FXML
    private TextArea descriptionResourceText;

    @FXML
    private TableColumn<EmployeeModel, String> firstNameColumn;

    @FXML
    private Region regionSelectedResource;

    @FXML
    private Label firstNameLabel;

    @FXML
    private GridPane gridScheduling;

    @FXML
    private GridPane gridAllocResource;

    @FXML
    private TableColumn<EmployeeModel, Integer> idPeopleColumn;

    @FXML
    private TableColumn<ResourceModel, Integer> idResourceColumn;

    @FXML
    private TableColumn<EmployeeModel, String> lastNameColumn;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label mailLabel;

    @FXML
    private Button modifyButton;

    @FXML
    private TableColumn<ResourceModel, String> nameResourceColumn;

    @FXML
    private Label phoneLabel;

    @FXML
    private TableColumn<ResourceModel, Integer> quantityResourceColumn;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    private Region regionBackground;

    @FXML
    private Label resourceNameLabel;

    @FXML
    private TableView<ResourceModel> resourcesTable;

    @FXML
    private TableColumn<ResourceModel, Boolean> shareableResourceColumn;

    @FXML
    private Label shareableResourceLabel;

    @FXML
    private TableView<EmployeeModel> tablePeople;

    @FXML
    private TreeView<String> teamsTreeView;

    @FXML
    private Label usernameLabel;

    @FXML
    private Region regionResources;

    @FXML
    private TableView<ResourceModel> resourceAllocationTable;

    @FXML
    private TableColumn<ResourceModel, String> nameResAllocColumn;

    @FXML
    private TableColumn<ResourceModel, Integer> idResAllocColumn;

    @FXML
    private Label resourceAllocNameLabel;

    @FXML
    private ScrollPane allocResScrollPane;

    @FXML
    private TableView<TaskModel> taskTablePeoplePage;

    @FXML
    private TableColumn<TaskModel, String> taskNamePeoplePage;

    @FXML
    private TableColumn<TaskModel, Integer> idTaskPeoplePage;

    @FXML
    private TableColumn<TaskRealModel, LocalDate> dayColumnTaskNotSch;

    @FXML
    private TableColumn<TaskRealModel, Integer> idColumnTaskNotSch;

    @FXML
    private TableColumn<TaskRealModel, String> nameColumnTaskNotSch;

    @FXML
    private TableView<TaskRealModel> tableTaskNotScheduled;

    @FXML
    private DatePicker dateForResourceAlloc;

    @FXML
    private Label dateResAllocLabel;

    @FXML
    private Label selectedResourceLabel;

    @FXML
    private DatePicker createNewTaskDeadline;

    @FXML
    private Spinner<Integer> createNewTaskDuration;

    @FXML
    private TextField createNewTaskName;

    @FXML
    private ComboBox<String> createNewTaskPeriodicity;

    @FXML
    private DatePicker createNewTaskStart;

    @FXML
    private TableView<ResourceModel> resourcesToNewTaskTable;

    @FXML
    private TableColumn<ResourceModel, String> resourceNameNewTask;

    @FXML
    private TableColumn<ResourceModel, Integer> resourceQuantityNewTask;

    @FXML
    private Button allocResourceNewTaskButton;

    @FXML
    private ComboBox<String> assignToNewTaskComboBox;

    @FXML
    private ComboBox<String> predecesorNewTaskComboBox;

    @FXML
    private Button createNewTaskButton;

    @FXML
    private Label labelNewTaskMessage;

    @FXML
    private Label overviewLeader;

    @FXML
    private Label overviewNrDays;

    @FXML
    private Label overviewNrEmployees;

    @FXML
    private Label overviewNrResources;

    @FXML
    private Label overviewNrTasks;

    @FXML
    private Button clearTaskButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearTaskButton.setStyle("button-hover-color: #293241; " );
        createNewTaskButton.setStyle("button-hover-color: #293241; " );

        /**Get all information about current project**/
        SenderText data = new SenderText();
        int id = Integer.parseInt(data.getData());

        Client client = Client.getInstance();
        JSONObject tosend = new JSONObject();

        tosend.put("Type", "Get Project");
        tosend.put("IDproject", id);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        ProjectModel project = new ProjectModel();
        try {
            client.sendText(tosend.toString());

            Parent root = FXMLLoader.load(getClass().getResource("/MiniPages/LoadingPage.fxml"));

            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();

            primaryStage.initStyle(StageStyle.UNDECORATED);

            primaryStage.setScene(scene);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.show();

            String response = client.receiveText();

            project = gson.fromJson(response, ProjectModel.class);
            this.projectLocal = project;

            primaryStage.close();

            System.out.println(gson.toJson(projectLocal));
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**Start setting pages**/
        /**Overview Tab*/
        setOverviewPage();

        /**Task Scheduling Tab*/
        setTableTaskNotScheduled();
        setSchedulingTable();

        /**Set Add New Task Tab*/
        setFieldForNewTaskTab();

        /**Set modify Resource Tab*/
        setResourceModifyTab();

        /**Resource Allocation Tab*/
        setViewResourceAllocationTab();

        /**Teams Tab*/
        setTeamsTab();

        /**People Tab*/
        setPeopleTab();


        /**Check Positions**/
        fillWithZeroWhenResourceIsNotUsed();
        calculateCompletionTime();

    }


    public void setTeamsTab() {
        ImageView img = new ImageView("/Images/employees.png");
        img.setFitWidth(20);
        img.setFitHeight(20);
        TreeItem<String> rootItem = new TreeItem<>("Teams", img);

        ArrayList<TeamModel> listaEchipe = projectLocal.getListaEchipe();
        for (int i = 0; i < listaEchipe.size(); i++) {
            TreeItem<String> teamName = new TreeItem<>(listaEchipe.get(i).getName());

            ArrayList<EmployeeModel> listaEmployees = listaEchipe.get(i).getListaEmployees();
            for (int j = 0; j < listaEmployees.size(); j++) {
                TreeItem<String> emp = new TreeItem<>(listaEmployees.get(j).getFullName());
                teamName.getChildren().add(emp);
            }

            rootItem.getChildren().add(teamName);
        }

        teamsTreeView.setRoot(rootItem);
    }

    public void setOverviewPage() {
        overviewLeader.setText(projectLocal.getCoordonatorFullName());
        overviewNrEmployees.setText(String.valueOf(projectLocal.getListaOameni().size()));
        overviewNrTasks.setText(String.valueOf(projectLocal.getListaTaskuriReale().size()));
        overviewNrResources.setText(String.valueOf(projectLocal.getListaResurseCurente().size()));
        long totaldays = ChronoUnit.DAYS.between(projectLocal.getStarttime(), projectLocal.getDeadline());
        overviewNrDays.setText(String.valueOf(totaldays));

    }

    public void setPeopleTab() {
        setTablePeople();
        setTasksTablePeoplePage();

        tablePeople.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                usernameLabel.setText(newSelection.getUsername());
                firstNameLabel.setText(newSelection.getFirstname());
                lastNameLabel.setText(newSelection.getLastname());
                addressLabel.setText(newSelection.getAddress());
                phoneLabel.setText(newSelection.getPhone());
                mailLabel.setText(newSelection.getMail());
                setObservableListOfTaskForUser(newSelection);
                taskTablePeoplePage.setItems(listaTaskuriForUser);
            }
        });
    }

    public void setResourceModifyTab() {
        setResourceTable();

        SpinnerValueFactory<Integer> spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999);
        descriptionResourceText.setWrapText(true);
        descriptionResourceText.setEditable(false);

        resourcesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                this.selectedResource = newSelection;
                resourceNameLabel.setText(newSelection.getDenumire());
                descriptionResourceText.setText(newSelection.getDescriere());
                if (newSelection.isShareable()) {
                    shareableResourceLabel.setText("True");
                } else {
                    shareableResourceLabel.setText("False");
                }
                spinner.setValue(newSelection.getCantitate());
                quantitySpinner.setValueFactory(spinner);

            }
        });
    }

    public void setViewResourceAllocationTab() {
        setResourceAllocTable();
        selectedResourceLabel.setVisible(false);
        dateResAllocLabel.setVisible(false);
        dateForResourceAlloc.setVisible(false);
        regionSelectedResource.setVisible(false);

        dateForResourceAlloc.setValue(projectLocal.getStarttime());
        dateForResourceAlloc.setDayCellFactory(d ->
                new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(projectLocal.getDeadline()) || item.isBefore(projectLocal
                                .getStarttime()));
                    }
                });

        resourceAllocationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedResourceLabel.setVisible(true);
                dateResAllocLabel.setVisible(true);
                dateForResourceAlloc.setVisible(true);
                regionSelectedResource.setVisible(true);

                resourceAllocNameLabel.setText(newSelection.getDenumire());

                setResourceAllocGrid(newSelection, dateForResourceAlloc.getValue());


                dateForResourceAlloc.valueProperty().addListener(new ChangeListener<LocalDate>() {
                    @Override
                    public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                        if (!setResourceAllocGrid(newSelection, t1)) {
                            dateForResourceAlloc.setValue(previousDate);
                        } else {
                            previousDate = dateForResourceAlloc.getValue();
                        }
                    }
                });

            }
        });

    }

    public void setObservableListOfTaskForUser(EmployeeModel user) {
        listaTaskuriForUser.clear();
        ArrayList<TaskModel> taskuriLocal = projectLocal.getListaTaskuri();


        for (int i = 0; i < taskuriLocal.size(); i++) {
            if (taskuriLocal.get(i).getExecutant().getID() == user.getID()) {
                listaTaskuriForUser.add(taskuriLocal.get(i));
            }
        }
    }

    public void setTasksTablePeoplePage() {
        idTaskPeoplePage.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idTaskPeoplePage.setStyle("-fx-alignment: CENTER ; ");
        taskNamePeoplePage.setCellValueFactory(new PropertyValueFactory<>("name"));
        taskNamePeoplePage.setStyle("-fx-alignment: CENTER ; ");
    }

    public void setResourceTable() {
        idResourceColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idResourceColumn.setStyle("-fx-alignment: CENTER ; ");
        nameResourceColumn.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        nameResourceColumn.setStyle("-fx-alignment: CENTER ; ");
        quantityResourceColumn.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        quantityResourceColumn.setStyle("-fx-alignment: CENTER ; ");
        shareableResourceColumn.setCellValueFactory(new PropertyValueFactory<>("shareable"));
        shareableResourceColumn.setStyle("-fx-alignment: CENTER ; ");

        ObservableList<ResourceModel> listaResurse = FXCollections.observableArrayList();

        for (int i = 0; i < projectLocal.getListaResurseCurente().size(); i++) {
            listaResurse.add(projectLocal.getListaResurseCurente().get(i));
        }

        resourcesTable.setItems(listaResurse);

    }

    public void setResourceAllocTable() {
        idResAllocColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idResAllocColumn.setStyle("-fx-alignment: CENTER ; ");
        nameResAllocColumn.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        nameResAllocColumn.setStyle("-fx-alignment: CENTER ; ");
        ObservableList<ResourceModel> listaResurse = FXCollections.observableArrayList();

        for (int i = 0; i < projectLocal.getListaResurseCurente().size(); i++) {
            listaResurse.add(projectLocal.getListaResurseCurente().get(i));
        }

        resourceAllocationTable.setItems(listaResurse);
    }

    public boolean setResourceAllocGrid(ResourceModel rs, LocalDate day) {

        ArrayList<TaskRealModel> listaTaskuriRealePerDay = new ArrayList<>();
        System.out.println("Current day :" + day + " size project: " + projectLocal.getListaTaskuriReale().size());
        for (TaskRealModel task : projectLocal.getListaTaskuriReale()) {
            if (task.getDay().equals(day) && task.getStartTime() >= 0 && task.getQuantityOfResourceRequest(rs.getId()) > 0) {
                listaTaskuriRealePerDay.add(task);
            }
        }
        System.out.println("Size day:" + listaTaskuriRealePerDay.size());


        if (listaTaskuriRealePerDay.size() != 0) {
            GridPane grid = new GridPane();
            grid.setHgap(5);
            grid.setVgap(5);
            int currentRow = 0;
            int currentColumn = 1;
            int hour = 8;

            Button empty = new Button("Quantity\\Timeline");
            empty.setStyle("-fx-background-color:#e6b800; " +
                    "-fx-text-fill:black;");
            empty.setMaxWidth(Double.MAX_VALUE);
            empty.setMinWidth(Control.USE_PREF_SIZE);
            grid.add(empty, 0, 0);
            for (int i = 0; i < 24; i++) {
                Button button = new Button(hour + "-" + (hour + 1));
                hour++;
                if (hour == 24) {
                    hour = 0;
                }
                button.setStyle("-fx-background-color:#e6b800; " +
                        "-fx-text-fill:black;");
                button.setMaxWidth(Double.MAX_VALUE);
                button.setMinWidth(Control.USE_PREF_SIZE);
                button.setMaxHeight(Double.MAX_VALUE);
                grid.add(button, currentColumn, currentRow);

                currentColumn++;
            }

            currentRow = 1;
            currentColumn = 0;
            int max = rs.getCantitate();
            for (int i = 0; i < max; i++) {
                Button quantity = new Button(String.valueOf(max - i));
                quantity.setStyle("-fx-background-color:#e6b800; " +
                        "-fx-text-fill:black;");
                quantity.setMaxWidth(Double.MAX_VALUE);
                quantity.setMinWidth(Control.USE_PREF_SIZE);
                quantity.setMaxHeight(Double.MAX_VALUE);
                grid.add(quantity, currentColumn, currentRow);
                currentRow++;

            }

            positionMap.clear();
            calculatePositions(rs, listaTaskuriRealePerDay);

            for (Integer key : positionMap.keySet()) {
                System.out.println("key : " + key + " value : " + positionMap.get(key));
            }

            System.out.println("Size:" + listaTaskuriRealePerDay.size());
            for (TaskRealModel task : listaTaskuriRealePerDay) {
                Button taskBtn = new Button(task.getName());
                taskBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Task Information");
                            alert.setHeaderText("Task Information");
                            TaskRealModel parent = new TaskRealModel();
                            for (TaskRealModel taskreal : listaTaskuriRealePerDay) {
                                if (task.getParentID() == taskreal.getID())
                                    parent = taskreal;
                            }
                            alert.setContentText("Name : "+task.getName()+"\n" +
                                    "Duration : "+task.getDuration()+"\n" +
                                    "Start Point : " +task.getStartTime()+"\n" +
                                    "Executor : " + task.getOriginTask().getExecutant().getFullName()+"\n" +
                                    "Parent : "+parent.getName());

                            alert.showAndWait();

                        }

                    }
                });
                taskBtn.setStyle("-fx-background-color:#E0FBFC;" +
                        "-fx-border-color:black");
                taskBtn.setMaxWidth(Double.MAX_VALUE);
                taskBtn.setMinWidth(Control.USE_PREF_SIZE);
                taskBtn.setMaxHeight(Double.MAX_VALUE);
                grid.add(taskBtn, task.getStartTime() + 1, positionMap.get(task.getID()) + 1,
                        task.getDuration(), task.getQuantityOfResourceRequest(rs.getId()));
            }

            grid.setStyle("-fx-margin:2px");
            allocResScrollPane.setFitToHeight(true);
            allocResScrollPane.setContent(grid);
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setContentText("There is no task for selected day!");
            alert.showAndWait();

            return false;
        }
    }

    public void setSchedulingTable() {
        LocalDate currentDate = LocalDate.now();
        /**Fill grid*/
        /**Hours*/
        gridScheduling.setVgap(5);
        gridScheduling.setHgap(5);
        int numRows = 7;
        int numColumns = 25;
        for (int row = 0; row < numRows; row++) {
            RowConstraints rc = new RowConstraints();
            rc.setFillHeight(true);
            rc.setVgrow(Priority.ALWAYS);
            gridScheduling.getRowConstraints().add(rc);
        }
        for (int col = 0; col < numColumns; col++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setFillWidth(true);
            cc.setHgrow(Priority.ALWAYS);
            gridScheduling.getColumnConstraints().add(cc);
        }

        int currentRow = 0;
        int currentColumn = 1;
        int hour = 8;

        for (int i = 0; i < 24; i++) {
            Button button = new Button(hour + "-" + (hour + 1));
            hour++;
            if (hour == 24) {
                hour = 0;
            }
            button.setMaxWidth(Double.MAX_VALUE);
            button.setMinWidth(Control.USE_PREF_SIZE);
            gridScheduling.add(button, currentColumn, currentRow);

            currentColumn++;
        }

        currentRow = 1;
        currentColumn = 0;
        for (int i = 0; i < 10; i++) {
            Button button1 = new Button(currentDate.plusDays(i).toString());

            // printTasksForCurrentUserOnSelectedDay(currentDate.plusDays(i));

            gridScheduling.add(button1, currentColumn, currentRow);
            button1.setMinWidth(Control.USE_PREF_SIZE);
            button1.setMaxWidth(Double.MAX_VALUE);
            button1.setMinHeight(Region.USE_PREF_SIZE);


            int idClient = Client.getInstance().getCurrentUser().getID();
            for (TaskRealModel task : projectLocal.getListaTaskuriReale()) {
                if (task.getDay().isEqual(currentDate.plusDays(i)) && idClient == task.getOriginTask().getExecutant().getID() && task.getStartTime() >= 0) {
                    Button taskBtn = new Button(task.getName());
                    taskBtn.setMinWidth(Control.USE_PREF_SIZE);
                    taskBtn.setMaxWidth(Double.MAX_VALUE);
                    gridScheduling.add(taskBtn, task.getStartTime() + 1, currentRow, task.getDuration(), 1);
                }
            }
            currentRow++;
        }


    }

    public void printTasksForCurrentUserOnSelectedDay(LocalDate day) {
        Client client = Client.getInstance();
        int id = client.getCurrentUser().getID();

        System.out.println("Ziua:" + day);
        for (TaskRealModel task : projectLocal.getListaTaskuriReale()) {
            if (task.getDay().isEqual(day) && id == task.getOriginTask().getExecutant().getID()) {
                System.out.println(task.getName());
            }
        }
    }

    public void setTableTaskNotScheduled() {
        idColumnTaskNotSch.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idColumnTaskNotSch.setStyle("-fx-alignment: CENTER ; ");
        nameColumnTaskNotSch.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumnTaskNotSch.setStyle("-fx-alignment: CENTER ; ");
        dayColumnTaskNotSch.setCellValueFactory(new PropertyValueFactory<>("day"));
        dayColumnTaskNotSch.setStyle("-fx-alignment: CENTER ; ");

        ObservableList<TaskRealModel> listaTasksReal = FXCollections.observableArrayList();

        /**To do get lista TaskUnscheduled*/
        for (TaskRealModel task : projectLocal.getListaTaskuriReale()) {
            if (task.getStartTime() < 0) {
                listaTasksReal.add(task);
            }
        }

        tableTaskNotScheduled.setItems(listaTasksReal);

        tableTaskNotScheduled.getSelectionModel().selectedIndexProperty().addListener((obs, oldSelection, newSelection) -> {
            int idSelected = tableTaskNotScheduled.getSelectionModel().getSelectedItem().getID();
            System.out.println("ID selectat:" + idSelected);
            Client client = Client.getInstance();
            JSONObject tosend = new JSONObject();
            tosend.put("Type", "Request Recommendations");
            tosend.put("IDtask", idSelected);
            try {
                client.sendText(tosend.toString());

                String receive = client.receiveText();
            } catch (Exception e) {
                e.printStackTrace();
            }


        });
    }

    public void setTablePeople() {
        idPeopleColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idPeopleColumn.setStyle("-fx-alignment: CENTER ; ");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        firstNameColumn.setStyle("-fx-alignment: CENTER ; ");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        lastNameColumn.setStyle("-fx-alignment: CENTER ; ");

        ObservableList<EmployeeModel> listaEmployees = FXCollections.observableArrayList();


        for (int i = 0; i < projectLocal.getListaOameni().size(); i++) {
            listaEmployees.add(projectLocal.getListaOameni().get(i));
        }

        tablePeople.setItems(listaEmployees);
    }


    @FXML
    void allocResourceToNewTaskAction(ActionEvent event) throws IOException {

        SelectedWorkersIDs resourceList = new SelectedWorkersIDs();
        resourceList.listaResProiect.clear();
        for (ResourceModel rs : projectLocal.getListaResurseCurente()) {
            resourceList.listaResProiect.add(rs);
        }


        Parent root = FXMLLoader.load(getClass().getResource("/MiniPages/AddResourcesToTask.fxml"));

        resourceList.listaResurse.clear();
        listaResurseNewTask.clear();


        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Alloc Resources");
        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.showAndWait();

        for (int i = 0; i < resourceList.listaResurse.size(); i++) {
            listaResurseNewTask.add(resourceList.listaResurse.get(i));
        }

        resourcesToNewTaskTable.setItems(listaResurseNewTask);
    }

    @FXML
    void createNewTaskAction(ActionEvent event) throws Exception {
        boolean isempty = false;
        if (createNewTaskName.getText() == null || createNewTaskName.getText().trim().isEmpty()) {
            isempty = true;
            createNewTaskName.setStyle("-fx-border-color:red");
        } else {
            createNewTaskName.setStyle("-fx-border-color:none");
        }
        if (createNewTaskPeriodicity.getValue() == null) {
            isempty = true;
            createNewTaskPeriodicity.setStyle("-fx-border-color:red");
        } else {
            createNewTaskPeriodicity.setStyle("-fx-border-color:none");
        }
        if (createNewTaskStart.getValue() == null) {
            isempty = true;
            createNewTaskStart.setStyle("-fx-border-color:red");
        } else {
            createNewTaskStart.setStyle("-fx-border-color:none");
        }
        if (createNewTaskDeadline.getValue() == null) {
            isempty = true;
            createNewTaskDeadline.setStyle("-fx-border-color:red");
        } else {
            createNewTaskDeadline.setStyle("-fx-border-color:none");
        }
        if (listaResurseNewTask.size() == 0) {
            isempty = true;
            resourcesToNewTaskTable.setStyle("-fx-border-color:red");
        } else {
            resourcesToNewTaskTable.setStyle("-fx-border-color:none");
        }
        if (assignToNewTaskComboBox.getValue() == null) {
            isempty = true;
            assignToNewTaskComboBox.setStyle("-fx-border-color:red");
        } else {
            assignToNewTaskComboBox.setStyle("-fx-border-color:none");
        }

        if (isempty) {
            labelNewTaskMessage.setText("Fields uncompleted!");
        } else {
            labelNewTaskMessage.setText("");
            TaskModel task = new TaskModel();
            task.setName(createNewTaskName.getText());
            task.setPeriodicity(createNewTaskPeriodicity.getValue().toString());
            task.setDuration(createNewTaskDuration.getValue());
            task.setStarttime(createNewTaskStart.getValue());
            task.setDeadline(createNewTaskDeadline.getValue());

            ArrayList<ResourceModel> listaResCurrentTask = new ArrayList<>();
            for (ResourceModel rs : listaResurseNewTask) {
                listaResCurrentTask.add(rs);
            }
            task.setListaResurse(listaResCurrentTask);

            if (predecesorNewTaskComboBox.getValue() != null) {
                String parent = predecesorNewTaskComboBox.getValue();
                for (TaskModel ts : projectLocal.getListaTaskuri()) {
                    if (ts.getName().equals(parent)) {
                        task.setParinte(ts);
                    }
                }
            }
            for (EmployeeModel em : projectLocal.getListaOameni()) {
                String fullname = assignToNewTaskComboBox.getValue();
                if (em.getFullName().equals(fullname)) {
                    System.out.println("Executant:" + em.getFullName());
                    task.setExecutant(em);
                }
            }


            task.setID_Proiect(projectLocal.getID());

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
            gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
            Gson gson = gsonBuilder.setPrettyPrinting().create();
            String stringJson = gson.toJson(task);
            System.out.println("***\n" + stringJson);

            Client client = Client.getInstance();
            JSONObject tosend = new JSONObject(stringJson);
            tosend.put("Type", "Add new task to project");

            client.sendText(tosend.toString());

            String result = client.receiveText();
            JSONObject response = new JSONObject(result);

            if (response.get("Final Response").equals("ok")) {
                JSONObject tosend2 = new JSONObject();
                tosend2.put("Type", "Get Project");
                tosend2.put("IDproject", projectLocal.getID());
                client.sendText(tosend2.toString());
                String receive = client.receiveText();
                this.projectLocal = gson.fromJson(receive, ProjectModel.class);
                System.out.println(gson.toJson(projectLocal));

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Project Created!");
                alert.setContentText("Task was inserted and a new scheduling is available!");
                alert.showAndWait();

                resetAllTables();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setContentText("SQL Exception occured!");
                alert.showAndWait();
            }

        }


    }

    @FXML
    void modifyResourceAction(ActionEvent event) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        selectedResource.setCantitate(quantitySpinner.getValue());
        JSONObject tosend = new JSONObject(selectedResource);
        tosend.put("Type", "Resource new quantity");
        tosend.put("IDproject", projectLocal.getID());

        tosend.remove("Type");
        tosend.remove("IDproject");
        ResourceModel rs = gson.fromJson(tosend.toString(), ResourceModel.class);
        System.out.println(gson.toJson(rs));


        System.out.println("Clicked!");
    }

    ObservableList<TaskModel> listaTaskuriForUser = FXCollections.observableArrayList();
    ObservableList<EmployeeModel> listaEmployee = FXCollections.observableArrayList();
    private ObservableList<ResourceModel> listaResurseNewTask = FXCollections.observableArrayList();
    private ObservableList<String> listaTaskuriTotale = FXCollections.observableArrayList();
    private ObservableList<String> listaUseri = FXCollections.observableArrayList();
    ObservableList<String> periodicityIntervals =
            FXCollections.observableArrayList(
                    "No Periodicity", "Daily", "Weekly", "Monthly"
            );

    public void resetAllTables() {
        setTableTaskNotScheduled();
        setSchedulingTable();

        /**Set Add New Task Tab*/
        setFieldForNewTaskTab();

        /**Set modify Resource Tab*/
        setResourceModifyTab();

        /**Resource Allocation Tab*/
        setViewResourceAllocationTab();

        /**Teams Tab*/
        setTeamsTab();

        /**People Tab*/
        setPeopleTab();


        /**Check Positions**/
        fillWithZeroWhenResourceIsNotUsed();
        calculateCompletionTime();
    }

    public void setFieldForNewTaskTab() {
        resourceNameNewTask.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        resourceNameNewTask.setStyle("-fx-alignment: CENTER ; ");
        resourceQuantityNewTask.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        resourceQuantityNewTask.setStyle("-fx-alignment: CENTER ; ");

        SpinnerValueFactory<Integer> spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        spinner.setValue(1);
        createNewTaskDuration.setValueFactory(spinner);

        createNewTaskPeriodicity.setItems(periodicityIntervals);

        createNewTaskStart.setValue(projectLocal.getStarttime());
        createNewTaskStart.setDayCellFactory(d ->
                new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(projectLocal.getDeadline()) || item.isBefore(projectLocal
                                .getStarttime()));
                    }
                });

        createNewTaskDeadline.setValue(projectLocal.getDeadline());
        createNewTaskDeadline.setDayCellFactory(d ->
                new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(projectLocal.getDeadline()) || item.isBefore(createNewTaskStart.getValue().plusDays(1)));
                    }
                });

        for (TaskModel task : projectLocal.getListaTaskuri()) {
            listaTaskuriTotale.add(task.getName());
        }
        for (EmployeeModel emp : projectLocal.getListaOameni()) {
            listaUseri.add(emp.getFullName());
        }


        predecesorNewTaskComboBox.setItems(listaTaskuriTotale);
        assignToNewTaskComboBox.setItems(listaUseri);

    }

    /**
     * Functions to print Resources' allocation
     **/
    public void calculatePositions(ResourceModel res, ArrayList<TaskRealModel> listaTaskuri) {
        System.out.println("Resource: " + res.getDenumire() + " Quantity:" + res.getCantitate() + " Makespan:" + getMakespan(listaTaskuri));

        String[][] positionMat = new String[res.getCantitate()][];

        for (int i = 0; i < res.getCantitate(); i++) {
            positionMat[i] = new String[getMakespan(listaTaskuri)];
        }

        // System.out.println("Incepe pozitionarea!");

        for (int i = 0; i < res.getCantitate(); i++) {
            //   System.out.println();
            for (int j = 0; j < getMakespan(listaTaskuri); j++) {
                positionMat[i][j] = "0";
                //   System.out.print(positionMat[i][j] + " ");
            }
        }


        String m[][] = new String[res.getCantitate()][getMakespan(listaTaskuri)];
        for (int i = 0; i < res.getCantitate(); i++) {
            for (int j = 0; j < getMakespan(listaTaskuri); j++) {
                m[i][j] = "0";
            }
        }

        System.out.println("Incepe functia recursiva");
        found = 0;
        RecursiveFunction(0, 0, m, 0, res, listaTaskuri);

        System.out.println("\nFinalizare RESURSA\n");
    }

    public int getNoTimeslot(int timeslot, ArrayList<TaskRealModel> listaTaskuri) {
        int no = 0;
        for (int i = 0; i < listaTaskuri.size(); i++) {
            TaskRealModel task = listaTaskuri.get(i);
            if (task.getStartTime() == timeslot)
                no++;
        }

        return no;
    }

    public TaskRealModel getTaskNoTimeslot(int timeslot, int placedTimeslot, ArrayList<TaskRealModel> listaTaskuri) {
        int no = 0;
        for (int i = 0; i < listaTaskuri.size(); i++) {
            TaskRealModel task = listaTaskuri.get(i);
            if (task.getStartTime() == timeslot) {
                if (no == placedTimeslot)
                    return task;
                else
                    no++;
            }
        }

        return null;
    }

    int found = 0;

    //placed time splot = index task
    public void RecursiveFunction(int timeslot, int placedTimeslot, String[][] m, int noTaskPut, ResourceModel res, ArrayList<TaskRealModel> listaTaskuri) {
        // System.out.println("Taskuri aranjate:"+noTaskPut);
        if (found == 1)
            return;
        if (noTaskPut == listaTaskuri.size()) {
            //   System.out.println("Am pus :" + noTaskPut);
            //   System.out.println("FINISH");
            printMatrix(m, res.getCantitate(), getMakespan(listaTaskuri));
            // System.out.println("AICI");
            found = 1;
            return;
        }
        if (timeslot > getMakespan(listaTaskuri))
            return;
        if (getNoTimeslot(timeslot, listaTaskuri) == 0)
            RecursiveFunction(timeslot + 1, 0, m, noTaskPut, res, listaTaskuri);
        else if (placedTimeslot == getNoTimeslot(timeslot, listaTaskuri))
            RecursiveFunction(timeslot + 1, 0, m, noTaskPut, res, listaTaskuri);
        else {
            TaskRealModel task = getTaskNoTimeslot(timeslot, placedTimeslot, listaTaskuri);

            int nrResurse = task.getQuantityOfResourceRequest(res.getId());

            for (int rand = 0; rand < (res.getCantitate() - nrResurse + 1); rand++) {
                int start = task.getStartTime();
                int durata = task.getDuration();

                // System.out.println("Testez pentru :" + task.getName());
                boolean result = checkSpace(rand, start, nrResurse, durata, m);
                if (result) {
                    for (int linie = rand; linie < rand + nrResurse; linie++) {
                        /***/
                        this.positionMap.put(task.getID(), rand);
                        /***/
                        for (int coloana = start; coloana < start + durata; coloana++) {
                            m[linie][coloana] = task.getName();
                        }
                    }
                    //   printMatrix(m,res.getCantitate(),getMakespan(listaTaskuri));

                    RecursiveFunction(timeslot, placedTimeslot + 1, m, noTaskPut + 1, res, listaTaskuri);
                    if (found == 1)
                        return;
                    for (int linie = rand; linie < rand + nrResurse; linie++) {
                        for (int coloana = start; coloana < start + durata; coloana++) {
                            m[linie][coloana] = "0";
                        }
                    }
                }
            }
        }
    }

    public void calculateCompletionTime() {
        for (TaskRealModel task : projectLocal.getListaTaskuriReale()) {
            task.setCompletionTime(task.getStartTime() + task.getDuration());
        }
    }

    public int getMakespan(ArrayList<TaskRealModel> listaTaskuri) {
        int max = listaTaskuri.get(0).getCompletionTime();
        for (int i = 0; i < listaTaskuri.size(); i++) {
            if (listaTaskuri.get(i).getCompletionTime() > max) {
                max = listaTaskuri.get(i).getCompletionTime();
            }
        }

        return max;
    }

    public void printMatrix(String[][] m, int linii, int coloana) {
        System.out.println();

        for (int i = 0; i < linii; i++) {
            for (int j = 0; j < coloana; j++) {
                String p = "00";
                if (!m[i][j].equals("0"))
                    p = m[i][j];
                System.out.print(p + " ");
            }
            System.out.println();
        }
    }

    public boolean checkSpace(int linie, int coloana, int nrRes, int durata, String m[][]) {
        boolean ok = true;
        for (int i = linie; i < linie + nrRes; i++) {
            for (int j = coloana; j < coloana + durata; j++) {
                if (!m[i][j].equals("0")) {
                    ok = false;
                }
            }
        }
        return ok;
    }

    public void fillWithZeroWhenResourceIsNotUsed() {
        for (int i = 0; i < projectLocal.getListaTaskuriReale().size(); i++) {
            for (int j = 0; j < projectLocal.getListaResurseCurente().size(); j++) {
                if (!projectLocal.getListaTaskuriReale().get(i).checkIfTaskUseAResource(projectLocal.getListaResurseCurente().get(j).getId())) {
                    projectLocal.getListaTaskuriReale().get(i).addIntoHashMap(projectLocal.getListaResurseCurente().get(j).getId(), 0);
                }
            }
        }
    }
}
