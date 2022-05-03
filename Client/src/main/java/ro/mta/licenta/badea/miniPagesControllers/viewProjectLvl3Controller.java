package ro.mta.licenta.badea.miniPagesControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.json.JSONObject;
import ro.mta.licenta.badea.Client;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateTimeDeserializer;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateTimeSerializer;
import ro.mta.licenta.badea.models.*;
import ro.mta.licenta.badea.temporalUse.SenderText;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class viewProjectLvl3Controller implements Initializable {

    private ProjectModel projectLocal;

    @FXML
    private Label addressLabel;

    @FXML
    private TextArea descriptionResourceText;

    @FXML
    private TableColumn<EmployeeModel, String> firstNameColumn;

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
    void modifyResourceAction(ActionEvent event) {
        System.out.println("Clicked!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**Get all information about current project**/
        SenderText data = new SenderText();
        int id = Integer.parseInt(data.getData());

        Client client = Client.getInstance();
        JSONObject tosend = new JSONObject();

        tosend.put("Type", "Get Project");
        tosend.put("IDproject", id);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        ProjectModel project = new ProjectModel();
        try {
            client.sendText(tosend.toString());
            String response = client.receiveText();
            project = gson.fromJson(response, ProjectModel.class);
            this.projectLocal = project;

            System.out.println(gson.toJson(projectLocal));
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        /**Set table People**/
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

        /**Set scheduling view**/
        setSchedulingTable();

        /**Set resource Table*/
        setResourceTable();

        SpinnerValueFactory<Integer> spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999);
        descriptionResourceText.setWrapText(true);
        descriptionResourceText.setEditable(false);

        resourcesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
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

        /**Set Allocation Res Page**/

        setResourceAllocTable();
        resourceAllocationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                resourceAllocNameLabel.setText(newSelection.getDenumire());
                setResourceAllocGrid(newSelection.getCantitate());
            }
        });

    }

    ObservableList<TaskModel> listaTaskuriForUser = FXCollections.observableArrayList();

    public void setObservableListOfTaskForUser(EmployeeModel user) {
        listaTaskuriForUser.clear();
        ArrayList<TaskModel> taskuriLocal = projectLocal.getListaTaskuri();


        for (int i = 0; i < taskuriLocal.size(); i++) {
            if (taskuriLocal.get(i).getExecutant().getID() == user.getID()) {
                listaTaskuriForUser.add(taskuriLocal.get(i));
            }
        }
    }

    public void setTasksTablePeoplePage(){
        idTaskPeoplePage.setCellValueFactory(new PropertyValueFactory<>("ID"));
        taskNamePeoplePage.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    public void setResourceAllocTable() {
        idResAllocColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameResAllocColumn.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        ObservableList<ResourceModel> listaResurse = FXCollections.observableArrayList();

        for (int i = 0; i < projectLocal.getListaResurseCurente().size(); i++) {
            listaResurse.add(projectLocal.getListaResurseCurente().get(i));
        }

        resourceAllocationTable.setItems(listaResurse);
    }

    public void setResourceTable() {
        idResourceColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameResourceColumn.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        quantityResourceColumn.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        shareableResourceColumn.setCellValueFactory(new PropertyValueFactory<>("shareable"));

        ObservableList<ResourceModel> listaResurse = FXCollections.observableArrayList();

        for (int i = 0; i < projectLocal.getListaResurseCurente().size(); i++) {
            listaResurse.add(projectLocal.getListaResurseCurente().get(i));
        }

        resourcesTable.setItems(listaResurse);

    }

    public void setResourceAllocGrid(int nr_max) {

        /***REMAINDER: TRY MANUAL GRIDPANE***/
        GridPane grid = new GridPane();

        int currentRow = 0;
        int currentColumn = 1;
        int hour = 8;

        Button empty = new Button();
        empty.setMaxWidth(Double.MAX_VALUE);
        empty.setMinWidth(Control.USE_PREF_SIZE);
        grid.add(empty, 0, 0);
        for (int i = 0; i < 24; i++) {
            Button button = new Button(hour + "-" + (hour + 1));
            hour++;
            if (hour == 24) {
                hour = 0;
            }
            button.setMaxWidth(Double.MAX_VALUE);
            button.setMinWidth(Control.USE_PREF_SIZE);
            button.setMaxHeight(Double.MAX_VALUE);
            grid.add(button, currentColumn, currentRow);

            currentColumn++;
        }

        currentRow = 1;
        currentColumn = 0;
        int max = nr_max;
        for (int i = 0; i < max; i++) {
            Button quantity = new Button(String.valueOf(nr_max));
            quantity.setMaxWidth(Double.MAX_VALUE);
            quantity.setMinWidth(Control.USE_PREF_SIZE);
            quantity.setMaxHeight(Double.MAX_VALUE);
            grid.add(quantity, currentColumn, currentRow);
            currentRow++;
            nr_max--;
        }

        Button Task = new Button("Create Database");
        Task.setMaxWidth(Double.MAX_VALUE);
        Task.setMinWidth(Control.USE_PREF_SIZE);
        Task.setMaxHeight(Double.MAX_VALUE);
        grid.add(Task, 1, 1, 1, 4);

        Button a = new Button("Interface");
        a.setMaxWidth(Double.MAX_VALUE);
        a.setMinWidth(Control.USE_PREF_SIZE);
        a.setMaxHeight(Double.MAX_VALUE);
        grid.add(a, 1, 6, 4, 1);
        allocResScrollPane.setContent(grid);

//        currentRow = 1;
//        currentColumn = 0;
//        for (int i = 0; i < 10; i++) {
//            Button button1 = new Button("aaaaaaaaa");
//            gridAllocResource.add(button1, currentColumn, currentRow);
//            button1.setMinWidth(Control.USE_PREF_SIZE);
//            button1.setMaxWidth(Double.MAX_VALUE);
//            button1.setMinHeight(Region.USE_PREF_SIZE);
//            currentRow++;
//        }


//        Button test = new Button("Create Database");
//        test.setMinWidth(Control.USE_PREF_SIZE);
//        test.setMaxWidth(Double.MAX_VALUE);
//        gridAllocResource.add(test, 1, 1, 3, 3);

    }

    public void setSchedulingTable() {
        LocalDate currentDate = LocalDate.now();
        /**Fill grid*/
        /**Hours*/
        gridScheduling.setVgap(5);

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
            gridScheduling.add(button1, currentColumn, currentRow);
            button1.setMinWidth(Control.USE_PREF_SIZE);
            button1.setMaxWidth(Double.MAX_VALUE);
            button1.setMinHeight(Region.USE_PREF_SIZE);
            currentRow++;
        }

        Button test = new Button("Create Database");
        test.setMinWidth(Control.USE_PREF_SIZE);
        test.setMaxWidth(Double.MAX_VALUE);
        gridScheduling.add(test, 1, 1, 3, 1);

        Button test2 = new Button("Maintenance");
        test2.setMinWidth(Control.USE_PREF_SIZE);
        test2.setMaxWidth(Double.MAX_VALUE);
        gridScheduling.add(test2, 3, 4, 6, 1);

        Button test3 = new Button("Create Mail Server");
        test3.setMinWidth(Control.USE_PREF_SIZE);
        test3.setMaxWidth(Double.MAX_VALUE);
        gridScheduling.add(test3, 2, 2, 4, 1);

    }

    public void setTablePeople() {
        idPeopleColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        ObservableList<EmployeeModel> listaEmployees = FXCollections.observableArrayList();


        for (int i = 0; i < projectLocal.getListaOameni().size(); i++) {
            listaEmployees.add(projectLocal.getListaOameni().get(i));
        }

        tablePeople.setItems(listaEmployees);

    }
}
