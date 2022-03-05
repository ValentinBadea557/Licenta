package ro.mta.licenta.badea.miniPagesControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONObject;
import ro.mta.licenta.badea.Client;
import ro.mta.licenta.badea.models.CompanyModel;
import ro.mta.licenta.badea.models.EmployeeModel;
import ro.mta.licenta.badea.models.ProjectModel;
import ro.mta.licenta.badea.temporalUse.ProjectTemporalModel;
import ro.mta.licenta.badea.temporalUse.SelectedWorkersIDs;
import ro.mta.licenta.badea.temporalUse.WorkerModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddPeopleToProjectController implements Initializable {

    @FXML
    private Button testButton;

    @FXML
    private Button addSelectedButton;

    @FXML
    private Button finishButton;

    @FXML
    private TableColumn<WorkerModel, String> fullNameColumn;

    @FXML
    private TableColumn<WorkerModel, Integer> idColumn;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<WorkerModel> tableCoworkers;

    @FXML
    private Label totalNrLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        Client client = Client.getInstance();

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("Type", "View Employees not Admins");
        jsonRequest.put("ID_Companie",client.getCurrentUser().getCompany().getID());
        String response = null;
        try {
            client.sendText(jsonRequest.toString());
            response = client.receiveText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        CompanyModel company = gson.fromJson(response, CompanyModel.class);
        ArrayList<EmployeeModel> listaUseri = company.getListaPersonal();
        for (int i = 0; i < listaUseri.size(); i++) {
            String fullName = listaUseri.get(i).getFirstname() + " " + listaUseri.get(i).getLastname();
            WorkerModel aux = new WorkerModel(listaUseri.get(i).getID(), fullName);
            workers.add(aux);
            employees.add(listaUseri.get(i));
        }


        /** Set Table Columns*/
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        tableCoworkers.setItems(workers);

        /** Enable multiple selection */
        tableCoworkers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        /** Enable Search engine*/

        int totalNrOfCoworkers = workers.size();
        totalNrLabel.setText(String.valueOf(totalNrOfCoworkers));

        FilteredList<WorkerModel> filteredData = new FilteredList<>(workers, b -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(workerModel -> {

                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (workerModel.getFullName().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else {
                    return false; //no match
                }
            });

            SortedList<WorkerModel> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableCoworkers.comparatorProperty());

            tableCoworkers.setItems(sortedData);
        });


    }

    /**
     * initial list of people
     */
    private ObservableList<WorkerModel> workers = FXCollections.observableArrayList();

    private ObservableList<EmployeeModel> employees = FXCollections.observableArrayList();

    public void addSelectedCoworkersAction(ActionEvent actionEvent) {
        SelectedWorkersIDs listObject = new SelectedWorkersIDs();
        ObservableList<WorkerModel> localWorkers;
        localWorkers = tableCoworkers.getSelectionModel().getSelectedItems();

        int size = localWorkers.size();
        for (int i = 0; i < size; i++) {
            listObject.addWorker(localWorkers.get(i));
            int idSelected = localWorkers.get(i).getID();
            for (int j = 0; j < employees.size(); j++) {
                if (employees.get(j).getID() == idSelected) {
                    listObject.addEmployee(employees.get(j));
                }
            }
        }

        for (int i = 0; i < size; i++) {
            workers.remove(listObject.getWorkerbasedOnIndex(i));
        }
        listObject.clearList();

        int newTotalValues = workers.size();
        totalNrLabel.setText(String.valueOf(newTotalValues));



    }

    public void finishAction(ActionEvent actionEvent) {
        Stage stage = (Stage) finishButton.getScene().getWindow();
        stage.close();
    }
}
