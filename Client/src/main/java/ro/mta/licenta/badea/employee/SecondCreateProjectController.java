package ro.mta.licenta.badea.employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ro.mta.licenta.badea.miniPagesControllers.AddTeamToProjectController;
import ro.mta.licenta.badea.models.TeamModel;
import ro.mta.licenta.badea.temporalUse.SenderText;
import ro.mta.licenta.badea.temporalUse.WorkerModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SecondCreateProjectController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button backLeftButton;

    @FXML
    private TableView<WorkerModel> coworkersTableView;

    @FXML
    private TableColumn<WorkerModel, String> fullNameRoleColumn;

    @FXML
    private TableColumn<WorkerModel, Integer> idRolesColumn;

    @FXML
    private TableColumn<TeamModel, Integer> idTeamColumn;

    @FXML
    private TableColumn<TeamModel, String> teamNameColumn;

    @FXML
    private Pane paneMaster;

    @FXML
    private StackPane stackPaneView;


    @FXML
    private TableView<TeamModel> teamsTableView;

    @FXML
    private Button rightPageButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**Set team table*/
        idTeamColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        idTeamColumn.setStyle("-fx-alignment: CENTER");
        teamNameColumn.setStyle("-fx-alignment: CENTER");
        /**Set workers table*/
        idRolesColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        fullNameRoleColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        idRolesColumn.setStyle("-fx-alignment: CENTER");
        fullNameRoleColumn.setStyle("-fx-alignment: CENTER");
        coworkersTableView.setItems(workers);
        /***/


        coworkersTableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    /**Send selected worker's name*/
                    SenderText aux=new SenderText();
                    aux.setData(coworkersTableView.getSelectionModel().getSelectedItem().getFullName());

                    /**Open the correct pane in stack pane*/
                    Parent fxml= FXMLLoader.load(getClass().getResource("/MiniPages/AddRolesToWorkersPage.fxml"));
                    stackPaneView.getChildren().removeAll();
                    stackPaneView.getChildren().setAll(fxml);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        /***/
        try {
           // Parent fxml= FXMLLoader.load(getClass().getResource("/MiniPages/AddTeamPage.fxml"));


            FXMLLoader loader=new FXMLLoader(getClass().getResource("/MiniPages/AddTeamPage.fxml"));
            Parent fxml=loader.load();
            stackPaneView.getChildren().removeAll();
            stackPaneView.getChildren().setAll(fxml);

            AddTeamToProjectController childController = loader.getController();
            childController.setCustomerSelectCallback(customer -> {
                teams.add(customer);
                teamsTableView.setItems(teams);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<TeamModel> teams = FXCollections.observableArrayList();

    private ObservableList<WorkerModel> workers = FXCollections.observableArrayList(
            new WorkerModel(1, "Badea Valentin"),
            new WorkerModel(2, "Popescu Ion"),
            new WorkerModel(56, "Badea Mihai"),
            new WorkerModel(4, "Nancu Petrica"),
            new WorkerModel(5, "Pesu Ciprian")
    );

    public void backLeftAction(ActionEvent actionEvent) throws Exception{
        paneMaster.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("/EmployeePages/CreateProjectPane.fxml")));

    }


    public void addTeamAction(ActionEvent actionEvent) throws Exception{

        FXMLLoader loader=new FXMLLoader(getClass().getResource("/MiniPages/AddTeamPage.fxml"));
        Parent fxml=loader.load();
        stackPaneView.getChildren().removeAll();
        stackPaneView.getChildren().setAll(fxml);

        AddTeamToProjectController childController = loader.getController();
        childController.setCustomerSelectCallback(customer -> {
            teams.add(customer);
            teamsTableView.setItems(teams);
        });

    }

    public void rightPageAction(ActionEvent actionEvent) throws Exception{
        paneMaster.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("/EmployeePages/ThirdCreateProjectPane.fxml")));
    }
}
