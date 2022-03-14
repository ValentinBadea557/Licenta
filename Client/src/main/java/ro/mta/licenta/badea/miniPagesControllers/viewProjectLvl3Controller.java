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
import ro.mta.licenta.badea.employee.LocalDateTimeDeserializer;
import ro.mta.licenta.badea.employee.LocalDateTimeSerializer;
import ro.mta.licenta.badea.models.EmployeeModel;
import ro.mta.licenta.badea.models.ProjectModel;
import ro.mta.licenta.badea.models.ResourceModel;
import ro.mta.licenta.badea.models.TeamModel;
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

        tablePeople.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                usernameLabel.setText(newSelection.getUsername());
                firstNameLabel.setText(newSelection.getFirstname());
                lastNameLabel.setText(newSelection.getLastname());
                addressLabel.setText(newSelection.getAddress());
                phoneLabel.setText(newSelection.getPhone());
                mailLabel.setText(newSelection.getMail());

            }
        });

        /**Set scheduling view**/
        setSchedulingTable();



        /**Set region border radius*/

        regionResources.setStyle("-fx-border-radius:20px;" +
                "-fx-background-radius:20px; " +
                "-fx-background-color: WHITE;");

        regionBackground.setStyle("-fx-border-radius:20px;" +
                "-fx-background-radius:20px; " +
                "-fx-background-color: WHITE;");
    }

    public void setSchedulingTable(){
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

        System.out.println(projectLocal.getListaOameni().size());
        for(int i=0;i<projectLocal.getListaOameni().size();i++){
            listaEmployees.add(projectLocal.getListaOameni().get(i));
            System.out.println(projectLocal.getListaOameni().get(i).getFullName());
        }

        tablePeople.setItems(listaEmployees);

    }
}
