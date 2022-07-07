package ro.mta.licenta.badea.miniPagesControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import org.json.JSONArray;
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
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
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
    private ScrollPane scrollPane;

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

    @FXML
    void clearTaskAction(ActionEvent event) {
        clearCreateTaskFields();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearTaskButton.setStyle("button-hover-color: #293241; ");
        createNewTaskButton.setStyle("button-hover-color: #293241; ");

        /**Get all information about current project**/
        SenderText data = new SenderText();
        int id = Integer.parseInt(data.getData());

        Client client = Client.getInstance();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        ProjectModel project = new ProjectModel();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/MiniPages/LoadingPage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();

        primaryStage.initStyle(StageStyle.UNDECORATED);
        try {

            Service<ProjectModel> service = new Service<ProjectModel>() {
                @Override
                protected Task<ProjectModel> createTask() {
                    return new Task<ProjectModel>() {
                        @Override
                        protected ProjectModel call() throws Exception {
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

                            client.sendText(tosend.toString());
                            String response = client.receiveText();


                            ProjectModel project = gson.fromJson(response, ProjectModel.class);
                            projectLocal = project;

                            /***/
                            JSONObject getRealTask = new JSONObject();
                            getRealTask.put("Type", "Get Lista Taskuri Reale Project");
                            getRealTask.put("IDproject", id);
                            client.sendText(getRealTask.toString());
                            String receiveTasks = client.receiveText();

                            Type ArrayListRealTasks = new TypeToken<ArrayList<TaskRealModel>>() {
                            }.getType();
                            ArrayList<TaskRealModel> listaTaskuriRealeProject = gson.fromJson(receiveTasks, ArrayListRealTasks);

                            projectLocal.setListaTaskuriReale(listaTaskuriRealeProject);


                            /***/
                            setOriginTasks();
                            /***/

                            return project;
                        }
                    };

                }
            };
            service.start();

            service.setOnSucceeded(event -> {
                primaryStage.hide();
            });

            primaryStage.setScene(scene);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }


        gridScheduling.getChildren().clear();
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

    public void clearCreateTaskFields() {
        createNewTaskName.clear();
        resourcesToNewTaskTable.getItems().clear();
        predecesorNewTaskComboBox.getEditor().clear();
        predecesorNewTaskComboBox.setValue(null);
        assignToNewTaskComboBox.getEditor().clear();
        assignToNewTaskComboBox.setValue(null);

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
                            setGridAllocResourceEmpty(newSelection);
                            //dateForResourceAlloc.setValue(previousDate);
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

    public void setGridAllocResourceEmpty(ResourceModel rs){
        allocResScrollPane.setContent(null);
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

        allocResScrollPane.setFitToHeight(true);
        allocResScrollPane.setContent(grid);
    }

    public boolean setResourceAllocGrid(ResourceModel rs, LocalDate day) {

        ArrayList<TaskRealModel> listaTaskuriRealePerDay = new ArrayList<>();
        System.out.println("Current day :" + day + " size project: " + projectLocal.getListaTaskuriReale().size());
        for (TaskRealModel task : projectLocal.getListaTaskuriReale()) {
            if (task.getDay().equals(day) && task.getStartTime() >= 0 && task.getQuantityOfResourceRequest(rs.getId()) > 0) {
                System.out.println(task.getName()+" res: "+task.getQuantityOfResourceRequest(rs.getId())+" "+task.getStartTime()+" "+task.getCompletionTime());
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


            positionMap.clear();
            calculatePositions(rs, listaTaskuriRealePerDay);
            System.out.println("Taskuri pe ziua curenta: " + listaTaskuriRealePerDay.size());
            System.out.println(positionMap.size());

            for (Integer key : positionMap.keySet()) {
                for (TaskRealModel taskk : listaTaskuriRealePerDay) {
                    if (taskk.getID() == key)
                        System.out.println(taskk.getName());
                }
                System.out.println("key : " + key + " value : " + positionMap.get(key));

            }


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
                            alert.setContentText("Name : " + task.getName() + "\n" +
                                    "Duration : " + task.getDuration() + "\n" +
                                    "Start Point : " + task.getStartTime() + "\n" +
                                    "Executor : " + task.getOriginTask().getExecutant().getFullName() + "\n" +
                                    "Parent : " + parent.getName());

                            alert.showAndWait();

                        }

                    }
                });
                taskBtn.setStyle("-fx-background-color:#E0FBFC;" +
                        "-fx-border-color:black");
                taskBtn.setMaxWidth(Double.MAX_VALUE);
                taskBtn.setMinWidth(Control.USE_PREF_SIZE);
                taskBtn.setMaxHeight(Double.MAX_VALUE);

                System.out.println("Current task: "  + task.getName() + " " + positionMap.get(task.getID())+" "+
                        task.getQuantityOfResourceRequest(rs.getId())+ " duration:"+task.getDuration());

                grid.add(taskBtn, (task.getStartTime() + 1), (positionMap.get(task.getID()) + 1),
                        task.getDuration(), task.getQuantityOfResourceRequest(rs.getId()));

                System.out.println("added");
            }

            Node[] verticalHeaders = new Node[rs.getCantitate()+1];

            int max = rs.getCantitate();
            verticalHeaders[0]=createHeaderNode("Quantity/Timeline");
            for (int i = 0; i < max; i++) {
                    verticalHeaders[i+1]=createHeaderNode(String.valueOf(max-i));


            }
            Node[] horizontalHeaders = new Node[]{
                    createHeaderNode("07-08"),
                    createHeaderNode("08-09"),
                    createHeaderNode("09-10"),
                    createHeaderNode("10-11"),
                    createHeaderNode("11-12"),
                    createHeaderNode("12-13"),
                    createHeaderNode("13-14"),
                    createHeaderNode("14-15"),
                    createHeaderNode("15-16"),
                    createHeaderNode("16-17"),
                    createHeaderNode("17-18"),
                    createHeaderNode("18-19"),
                    createHeaderNode("19-20"),
                    createHeaderNode("20-21"),
                    createHeaderNode("21-22"),
                    createHeaderNode("22-23"),
                    createHeaderNode("23-00"),
                    createHeaderNode("00-01"),
                    createHeaderNode("01-02"),
                    createHeaderNode("02-03"),
                    createHeaderNode("03-04"),
                    createHeaderNode("04-05"),
                    createHeaderNode("05-06"),
                    createHeaderNode("06-07")
            };


            grid.addColumn(0, verticalHeaders);
            grid.addRow(0, horizontalHeaders);


            InvalidationListener headerUpdaterUp = o -> {
                final double ty = (grid.getHeight() - allocResScrollPane.getViewportBounds().getHeight()) * allocResScrollPane.getVvalue();

                for (Node header : horizontalHeaders) {
                    header.setTranslateY(ty);
                }
            };
            grid.heightProperty().addListener(headerUpdaterUp);
            allocResScrollPane.viewportBoundsProperty().addListener(headerUpdaterUp);
            allocResScrollPane.vvalueProperty().addListener(headerUpdaterUp);

            InvalidationListener headerUpdater = o -> {
                final double tx = (grid.getWidth() - allocResScrollPane.getViewportBounds().getWidth()) * allocResScrollPane.getHvalue();

                for (Node header : verticalHeaders) {
                    header.setTranslateX(tx);
                }
            };
            gridScheduling.widthProperty().addListener(headerUpdater);
            allocResScrollPane.viewportBoundsProperty().addListener(headerUpdater);
            allocResScrollPane.hvalueProperty().addListener(headerUpdater);


           // grid.setGridLinesVisible(true);
            grid.setStyle("-fx-margin:2px");
            allocResScrollPane.setFitToHeight(true);
            allocResScrollPane.setContent(grid);
            return true;
        } else {

            setGridAllocResourceEmpty(rs);
            return false;
        }
    }

    public void setSchedulingTable() {
        LocalDate currentDate = LocalDate.now();

        /**Fill grid*/
        /**Hours*/
        gridScheduling.setVgap(5);
        gridScheduling.setHgap(5);

        int currentRow = 1;
        int currentColumn = 0;
        for (int i = 0; i < 10; i++) {
            int idClient = Client.getInstance().getCurrentUser().getID();
            for (TaskRealModel task : projectLocal.getListaTaskuriReale()) {
                if (task.getDay().isEqual(currentDate.plusDays(i)) && idClient == task.getOriginTask().getExecutant().getID() && task.getStartTime() >= 0) {
                    Button taskBtn = new Button(task.getName());
                    taskBtn.setStyle("-fx-background-color:#E0FBFC;" +
                            "-fx-border-color:black");
                    taskBtn.setMinWidth(Control.USE_PREF_SIZE);
                    taskBtn.setMaxWidth(Double.MAX_VALUE);
                    gridScheduling.add(taskBtn, task.getStartTime() + 1, currentRow, task.getDuration(), 1);
                }
            }
            currentRow++;
        }
        Node[] verticalHeaders = new Node[]{
                createEmptyButton(),
                createHeaderNode(0),
                createHeaderNode(1),
                createHeaderNode(2),
                createHeaderNode(3),
                createHeaderNode(4),
                createHeaderNode(5),
                createHeaderNode(6),
                createHeaderNode(7),
                createHeaderNode(8),
                createHeaderNode(9),
                createHeaderNode(10)
        };

        Node[] horizontalHeaders = new Node[]{
                createHeaderNode("07-08"),
                createHeaderNode("08-09"),
                createHeaderNode("09-10"),
                createHeaderNode("10-11"),
                createHeaderNode("11-12"),
                createHeaderNode("12-13"),
                createHeaderNode("13-14"),
                createHeaderNode("14-15"),
                createHeaderNode("15-16"),
                createHeaderNode("16-17"),
                createHeaderNode("17-18"),
                createHeaderNode("18-19"),
                createHeaderNode("19-20"),
                createHeaderNode("20-21"),
                createHeaderNode("21-22"),
                createHeaderNode("22-23"),
                createHeaderNode("23-00"),
                createHeaderNode("00-01"),
                createHeaderNode("01-02"),
                createHeaderNode("02-03"),
                createHeaderNode("03-04"),
                createHeaderNode("04-05"),
                createHeaderNode("05-06"),
                createHeaderNode("06-07")
        };


        gridScheduling.addColumn(0, verticalHeaders);
        gridScheduling.addRow(0, horizontalHeaders);

        InvalidationListener headerUpdater = o -> {
            final double tx = (gridScheduling.getWidth() - scrollPane.getViewportBounds().getWidth()) * scrollPane.getHvalue();

            for (Node header : verticalHeaders) {
                header.setTranslateX(tx);
            }
        };
        gridScheduling.widthProperty().addListener(headerUpdater);
        scrollPane.viewportBoundsProperty().addListener(headerUpdater);
        scrollPane.hvalueProperty().addListener(headerUpdater);

        InvalidationListener headerUpdaterUp = o -> {
            final double ty = (gridScheduling.getHeight() - scrollPane.getViewportBounds().getHeight()) * scrollPane.getVvalue();

            for (Node header : horizontalHeaders) {
                header.setTranslateY(ty);
            }
        };
        gridScheduling.heightProperty().addListener(headerUpdaterUp);
        scrollPane.viewportBoundsProperty().addListener(headerUpdaterUp);
        scrollPane.vvalueProperty().addListener(headerUpdaterUp);


    }

    private static Button createEmptyButton() {
        Button button1 = new Button();
        button1.setVisible(false);
        button1.setMinWidth(Control.USE_PREF_SIZE);
        button1.setMaxWidth(Double.MAX_VALUE);
        button1.setMinHeight(Region.USE_PREF_SIZE);
        return button1;
    }

    private static Button createHeaderNode(String str) {
        Button button1 = new Button(str);
        button1.setStyle("-fx-background-color:#e6b800; " +
                "-fx-text-fill:black;");
        button1.setMinWidth(Control.USE_PREF_SIZE);
        button1.setMaxWidth(Double.MAX_VALUE);
        button1.setMinHeight(Region.USE_PREF_SIZE);
        return button1;
    }

    private static Button createHeaderNode(int i) {
        Button button1 = new Button(LocalDate.now().plusDays(i).toString());
        button1.setStyle("-fx-background-color:#e6b800; " +
                "-fx-text-fill:black;");
        button1.setMinWidth(Control.USE_PREF_SIZE);
        button1.setMaxWidth(Double.MAX_VALUE);
        button1.setMinHeight(Region.USE_PREF_SIZE);
        return button1;
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
            Integer returnErrorCode = -1;
            if (!returnErrorCode.equals(newSelection)) {

                int idSelected = tableTaskNotScheduled.getSelectionModel().getSelectedItem().getID();
                System.out.println("ID selectat:" + idSelected);
                Client client = Client.getInstance();
                JSONObject tosend = new JSONObject();
                tosend.put("Type", "Request Recommendations");
                tosend.put("idTaskReal", idSelected);
                tosend.put("idProject", projectLocal.getID());
                tosend.put("dataCurenta", tableTaskNotScheduled.getSelectionModel().getSelectedItem().getDay());

                String receive = null;
                try {
                    client.sendText(tosend.toString());

                    receive = client.receiveText();
                    System.out.println(receive);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JSONObject response = new JSONObject(receive);
                if (response.get("Response").equals("Modify Number Of Resources Alloc")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Server Response");
                    alert.setHeaderText("Suggestion");

                    String modificari = new String();
                    JSONArray array = response.getJSONArray("Modificari");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        System.out.println(obj.toString());
                        modificari += "Current task requests " + obj.get("RequestedQuantity") + " " + obj.get("ResourceName") + " " +
                                "but only " + obj.get("MaximQuantity") + " available\n";
                    }
                    modificari += "\n Do you want to change the amount of resource requested by task?";

                    alert.setContentText(modificari);
                    alert.getDialogPane().getButtonTypes().clear();
                    alert.getDialogPane().getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.YES) {

                        Parent root = null;
                        try {
                            root = FXMLLoader.load(getClass().getResource("/MiniPages/LoadingPage.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Scene scene = new Scene(root);
                        Stage primaryStage = new Stage();
                        primaryStage.initStyle(StageStyle.UNDECORATED);

                        Task<JSONObject> task = new Task<JSONObject>() {
                            @Override
                            protected JSONObject call() throws Exception {
                                JSONObject send = new JSONObject();
                                send.put("Type", "Modify Resources");
                                send.put("IDproject", projectLocal.getID());
                                send.put("Modificari", array);

                                try {
                                    client.sendText(send.toString());
                                    String responseModify = client.receiveText();

                                    JSONObject responseModifyQuantity = new JSONObject(responseModify);
                                    return responseModifyQuantity;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        };

                        task.setOnSucceeded(event -> {
                            if (task.getValue().get("Result").equals("OK")) {
                                service.restart();
                                service.setOnSucceeded(eventService -> {
                                    primaryStage.hide();

                                });
                            } else {
                                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                                alert2.setTitle("Request Response");
                                alert2.setHeaderText("ERROR!");
                                alert2.setContentText("Some error occurred!");
                                alert2.showAndWait();
                            }
                        });
                        new Thread(task).start();

                        primaryStage.setScene(scene);
                        primaryStage.initModality(Modality.APPLICATION_MODAL);
                        primaryStage.showAndWait();

                        Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                        alert3.setTitle("Request Response");
                        alert3.setHeaderText("All done!");
                        alert3.setContentText("Everything is good! A new scheduling is now available!");
                        alert3.showAndWait();

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                tableTaskNotScheduled.getSelectionModel().clearSelection();
                                resetAllTables();
                            }
                        });
                        // resetAllTables();


                    } else if (result.isPresent() && result.get() == ButtonType.NO) {
                        System.out.println("Cancel");

                    }
                } else if (response.get("Response").equals("Modify Duration")) {
                    System.out.println(response);
                    Alert alertaDuration = new Alert(Alert.AlertType.INFORMATION);
                    alertaDuration.setTitle("Server Response");
                    alertaDuration.setHeaderText("Suggestion");
                    String content = new String();
                    JSONObject obj = response.getJSONObject("Modificari");
                    System.out.println(obj);
                    content += "Current task exceed the timeline with " + obj.get("ToBeSubstracted") + " hours.\n";
                    content += "\nWould you like to decrease the duration of the task?";
                    alertaDuration.setContentText(content);
                    alertaDuration.getDialogPane().getButtonTypes().clear();
                    alertaDuration.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

                    Optional<ButtonType> result = alertaDuration.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.YES) {
                        Parent root = null;
                        try {
                            root = FXMLLoader.load(getClass().getResource("/MiniPages/LoadingPage.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Scene scene = new Scene(root);
                        Stage primaryStage = new Stage();
                        primaryStage.initStyle(StageStyle.UNDECORATED);

                        Task<JSONObject> task = new Task<JSONObject>() {
                            @Override
                            protected JSONObject call() throws Exception {
                                JSONObject send = new JSONObject();
                                send.put("Type", "Decrease Duration");
                                send.put("IDtask", obj.get("IDtask"));
                                send.put("IDproject", projectLocal.getID());
                                send.put("ValueToDecrease", obj.get("ToBeSubstracted"));

                                try {
                                    client.sendText(send.toString());
                                    String responseModifyDuration = client.receiveText();

                                    JSONObject JsonresponseModifyDuration = new JSONObject(responseModifyDuration);
                                    return JsonresponseModifyDuration;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        };

                        task.setOnSucceeded(event -> {
                            if (task.getValue().get("Result").equals("OK")) {
                                System.out.println("AICI");
                                service.restart();
                                service.setOnSucceeded(eventService -> {
                                    primaryStage.hide();

                                });
                            } else {
                                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                                alert2.setTitle("Request Response");
                                alert2.setHeaderText("ERROR!");
                                alert2.setContentText("Some error occurred!");
                                alert2.showAndWait();
                            }
                        });
                        new Thread(task).start();

                        primaryStage.setScene(scene);
                        primaryStage.initModality(Modality.APPLICATION_MODAL);
                        primaryStage.showAndWait();

                        Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                        alert3.setTitle("Request Response");
                        alert3.setHeaderText("All done!");
                        alert3.setContentText("Everything is good! A new scheduling is now available!");
                        alert3.showAndWait();

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                resetAllTables();
                            }
                        });
                        // resetAllTables();


                    } else {
                        System.out.println("No");
                    }
                }else if(response.get("Response").equals("No possibility for scheduling")){
                    Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                    alert3.setTitle("Server Response");
                    alert3.setHeaderText("INFO");
                    alert3.setContentText("This task can not be scheduled!");
                    alert3.showAndWait();
                }
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
    void createNewTaskAction(ActionEvent eventAction) throws Exception {
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


            /****/
            Task<JSONObject> thread1 = new Task<JSONObject>() {
                @Override
                protected JSONObject call() throws Exception {
                    System.out.println("IN THREAD\n");
                    Client client = Client.getInstance();
                    JSONObject tosend = new JSONObject(stringJson);
                    tosend.put("Type", "Add new task to project");

                    client.sendText(tosend.toString());

                    String result = client.receiveText();
                    JSONObject response = new JSONObject(result);

                    JSONObject tosend2 = new JSONObject();
                    tosend.put("Type", "Get Project");
                    tosend.put("IDproject", projectLocal.getID());
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
                    gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
                    Gson gson = gsonBuilder.setPrettyPrinting().create();
                    client.sendText(tosend.toString());
                    String response2 = client.receiveText();
                    ProjectModel project = gson.fromJson(response2, ProjectModel.class);
                    projectLocal = project;

                    JSONObject getRealTask = new JSONObject();
                    getRealTask.put("Type", "Get Lista Taskuri Reale Project");
                    getRealTask.put("IDproject", projectLocal.getID());
                    client.sendText(getRealTask.toString());
                    String receiveTasks = client.receiveText();

                    Type ArrayListRealTasks = new TypeToken<ArrayList<TaskRealModel>>() {
                    }.getType();
                    ArrayList<TaskRealModel> listaTaskuriRealeProject = gson.fromJson(receiveTasks, ArrayListRealTasks);

                    projectLocal.setListaTaskuriReale(listaTaskuriRealeProject);

                    setOriginTasks();
                    return response;
                }
            };

            new Thread(thread1).start();

            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/MiniPages/LoadingPage.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();
            primaryStage.initStyle(StageStyle.UNDECORATED);


            thread1.setOnSucceeded(event -> {
                System.out.println("Threadul a primit rasp: " + thread1.getValue().get("Final Response"));
                if (thread1.getValue().get("Final Response").equals("ok")) {
                    primaryStage.hide();

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alertTaskAdded = new Alert(Alert.AlertType.CONFIRMATION);
                            alertTaskAdded.setTitle("Request result");
                            alertTaskAdded.setHeaderText("Result");
                            alertTaskAdded.setContentText("Task was inserted and a new scheduling is available!");
                            alertTaskAdded.showAndWait();
                        }
                    });


                    predecesorNewTaskComboBox.getEditor().clear();
                    //setFieldForNewTaskTab();

                    listaTaskuriTotale.clear();
                    for (TaskModel task1 : projectLocal.getListaTaskuri()) {
                        listaTaskuriTotale.add(task1.getName());
                    }
                    predecesorNewTaskComboBox.setItems(listaTaskuriTotale);

                    clearCreateTaskFields();
                    resetAllTables();
                } else {
                    primaryStage.hide();
                    Alert alerterr = new Alert(Alert.AlertType.ERROR);
                    alerterr.setTitle("Request Response");
                    alerterr.setHeaderText("ERROR!");
                    alerterr.setContentText("Some error occurred!");
                    alerterr.showAndWait();
                }
            });


            primaryStage.setScene(scene);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.showAndWait();


        }


    }

    @FXML
    void modifyResourceAction(ActionEvent eventClick) throws Exception {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        Task<JSONObject> task = new Task<JSONObject>() {
            @Override
            protected JSONObject call() throws Exception {
                selectedResource.setCantitate(quantitySpinner.getValue());
                JSONObject tosend = new JSONObject();
                tosend.put("Type", "Resource new quantity");
                tosend.put("IDproject", projectLocal.getID());
                tosend.put("IDresource", selectedResource.getId());
                tosend.put("Quantity", quantitySpinner.getValue());
                System.out.println(gson.toJson(tosend));
                Client client = Client.getInstance();
                client.sendText(tosend.toString());
                String response = client.receiveText();
                JSONObject result = new JSONObject(response);
                return result;
            }
        };

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/MiniPages/LoadingPage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.initStyle(StageStyle.UNDECORATED);

        task.setOnSucceeded(event -> {
            if (task.getValue().get("Result").equals("OK")) {
                System.out.println("AICI");
                service.restart();
                service.setOnSucceeded(eventService -> {
                    primaryStage.hide();

                });
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Request Response");
                alert.setHeaderText("ERROR!");
                alert.setContentText("Some error occurred!");
                alert.showAndWait();
            }
        });
        new Thread(task).start();


        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.showAndWait();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Request Response");
        alert.setHeaderText("All done!");
        alert.setContentText("Everything is good! A new scheduling is now available!");
        alert.showAndWait();

        resetAllTables();

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

        gridScheduling.getChildren().clear();
//        tableTaskNotScheduled.getSelectionModel().clearSelection();
//        resourcesTable.getSelectionModel().clearSelection();
//        resourceAllocationTable.getSelectionModel().clearSelection();
//        tablePeople.getSelectionModel().clearSelection();


        fillWithZeroWhenResourceIsNotUsed();
        calculateCompletionTime();
        resourceAllocNameLabel.setText("");


        /**To do get lista TaskUnscheduled*/
        ObservableList<TaskRealModel> listaTasksReal = FXCollections.observableArrayList();
        for (TaskRealModel task : projectLocal.getListaTaskuriReale()) {
            if (task.getStartTime() < 0) {
                listaTasksReal.add(task);
            }
        }
        tableTaskNotScheduled.setItems(listaTasksReal);


        setSchedulingTable();


        /**Set modify Resource Tab*/
        setResourceTable();


        /**Resource Allocation Tab*/
        setResourceAllocTable();
        selectedResourceLabel.setVisible(false);
        dateResAllocLabel.setVisible(false);
        dateForResourceAlloc.setVisible(false);
        regionSelectedResource.setVisible(false);


        /**People Tab*/
        setTablePeople();
        setTasksTablePeoplePage();


        allocResScrollPane.setContent(null);


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

        if (found == 1)
            return;
        if (noTaskPut == listaTaskuri.size()) {
               System.out.println("Am pus :" + noTaskPut);
               System.out.println("FINISH");
            printMatrix(m, res.getCantitate(), getMakespan(listaTaskuri));

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

                 System.out.println("Testez pentru :" + task.getName());
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
                       printMatrix(m,res.getCantitate(),getMakespan(listaTaskuri));

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

    public void setOriginTasks(){
       for(TaskRealModel task:projectLocal.getListaTaskuriReale()){
           for(TaskModel t:projectLocal.getListaTaskuri()){
               if(task.getOriginTask().getID()==t.getID()){
                   task.setOriginTask(t);
               }
           }
       }
    }

    Service<ProjectModel> service = new Service<ProjectModel>() {
        @Override
        protected Task<ProjectModel> createTask() {
            return new Task<ProjectModel>() {
                @Override
                protected ProjectModel call() throws Exception {
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

                    client.sendText(tosend.toString());
                    String response = client.receiveText();


                    ProjectModel project = gson.fromJson(response, ProjectModel.class);
                    projectLocal = project;

                    JSONObject tosend3 = new JSONObject();
                    tosend3.put("Type", "Get Lista Taskuri Reale Project");
                    tosend3.put("IDproject", projectLocal.getID());
                    client.sendText(tosend3.toString());
                    String receive2 = client.receiveText();

                    Type ArrayListRealTasks = new TypeToken<ArrayList<TaskRealModel>>() {
                    }.getType();
                    ArrayList<TaskRealModel> listaTaskuriRealeProject = gson.fromJson(receive2, ArrayListRealTasks);
                    projectLocal.setListaTaskuriReale(listaTaskuriRealeProject);

                    setOriginTasks();
                    return project;
                }
            };
        }
    };
}
