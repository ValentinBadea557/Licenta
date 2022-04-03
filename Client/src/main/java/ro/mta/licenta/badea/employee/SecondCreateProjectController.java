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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.mta.licenta.badea.miniPagesControllers.AddResourceToProjectController;
import ro.mta.licenta.badea.miniPagesControllers.AddTeamToProjectController;
import ro.mta.licenta.badea.models.EmployeeModel;
import ro.mta.licenta.badea.models.ProjectModel;
import ro.mta.licenta.badea.models.ResourceModel;
import ro.mta.licenta.badea.models.TeamModel;
import ro.mta.licenta.badea.temporalUse.ProjectTemporalModel;
import ro.mta.licenta.badea.temporalUse.SelectedWorkersIDs;
import ro.mta.licenta.badea.temporalUse.SenderText;
import ro.mta.licenta.badea.temporalUse.WorkerModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private TableColumn<TeamModel, String> teamNameColumn;

    @FXML
    private Pane paneMaster;

    @FXML
    private StackPane stackPaneView;

    @FXML
    private TableView<TeamModel> teamsTableView;

    @FXML
    private Button rightPageButton;

    @FXML
    private TableView<ResourceModel> resourceTable;

    @FXML
    private TableColumn<ResourceModel, Integer> idResColumn;

    @FXML
    private TableColumn<ResourceModel, String> nameResColumn;

    @FXML
    private TableColumn<ResourceModel, Integer> quantityResColumn;

    @FXML
    private TableColumn<ResourceModel, Boolean> shareableResColumn;

    @FXML
    private Button addResourceButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**border radius*/
        stackPaneView.setStyle("-fx-border-radius:10px;" +
                "-fx-background-radius:10px; ");

        SelectedWorkersIDs lista = new SelectedWorkersIDs();
        for (int i = 0; i < lista.finalList.size(); i++) {
            workers.add(lista.finalList.get(i));
        }

        /**Set team table*/
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        teamNameColumn.setStyle("-fx-alignment: CENTER ; ");
        /**Set workers table*/
        idRolesColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        fullNameRoleColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        idRolesColumn.setStyle("-fx-alignment: CENTER ; ");
        fullNameRoleColumn.setStyle("-fx-alignment: CENTER ; ");
        coworkersTableView.setItems(workers);
        /***/


        /**Add role and permission*/
        coworkersTableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    /**Send selected worker's name*/
                    SenderText aux = new SenderText();
                    aux.setData(coworkersTableView.getSelectionModel().getSelectedItem().getFullName());

                    /**Open the correct pane in stack pane*/
                    Parent fxml = FXMLLoader.load(getClass().getResource("/MiniPages/AddRolesToWorkersPage.fxml"));
                    stackPaneView.getChildren().removeAll();
                    stackPaneView.getChildren().setAll(fxml);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        /** Add Resource*/

        setResourceTable();

        /*****/
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MiniPages/AddTeamPage.fxml"));
            Parent fxml = loader.load();
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

    private ObservableList<WorkerModel> workers = FXCollections.observableArrayList();

    private ObservableList<ResourceModel> resources = FXCollections.observableArrayList();


    public void backLeftAction(ActionEvent actionEvent) throws Exception {
        paneMaster.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("/EmployeePages/CreateProjectPane.fxml")));

    }


    public void addTeamAction(ActionEvent actionEvent) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MiniPages/AddTeamPage.fxml"));
        Parent fxml = loader.load();
        stackPaneView.getChildren().removeAll();
        stackPaneView.getChildren().setAll(fxml);

        AddTeamToProjectController childController = loader.getController();
        childController.setCustomerSelectCallback(customer -> {
            teams.add(customer);
            teamsTableView.setItems(teams);
        });

    }

    public void addResourceAction(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/MiniPages/AddResourcesToProject.fxml"));

        SelectedWorkersIDs resourceList = new SelectedWorkersIDs();
        resourceList.clearResourceList();
        resources.clear();

        scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Add Resources to Project");
        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.showAndWait();


        ProjectTemporalModel tempProject=new ProjectTemporalModel();
        tempProject.setListaResurse(resourceList.listaResProiect);

        for (int i = 0; i < resourceList.listaResProiect.size(); i++) {
            resources.add(resourceList.listaResProiect.get(i));
        }

        resourceTable.setItems(resources);

        System.out.println(resources);


    }

    public void setResourceTable() {
        idResColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameResColumn.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        quantityResColumn.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        shareableResColumn.setCellValueFactory(new PropertyValueFactory<>("shareable"));

        idResColumn.setStyle("-fx-alignment: CENTER ; ");
        nameResColumn.setStyle("-fx-alignment: CENTER ;");
        quantityResColumn.setStyle("-fx-alignment: CENTER ;");
        shareableResColumn.setStyle("-fx-alignment: CENTER ;");

    }

    public void rightPageAction(ActionEvent actionEvent) throws Exception {

        ProjectTemporalModel project = new ProjectTemporalModel();

        SelectedWorkersIDs listaEmp=new SelectedWorkersIDs();

        ArrayList<EmployeeModel> lista = listaEmp.getListaEmployees();

        boolean nextpage = true;

        for (int i = 0; i < lista.size(); i++) {
            System.out.println(lista.get(i).getFullName()+" "+lista.get(i).getRole());
            if (lista.get(i).getRole() == null) {

                nextpage = false;
            }
        }

        if (nextpage) {
            paneMaster.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("/EmployeePages/ThirdCreateProjectPane.fxml")));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setContentText("All workers must have a role to continue!");
            alert.showAndWait();
        }
    }


}
