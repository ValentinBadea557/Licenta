package ro.mta.licenta.badea.miniPagesControllers;

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
import ro.mta.licenta.badea.temporalUse.SelectedWorkersIDs;
import ro.mta.licenta.badea.temporalUse.WorkerModel;

import java.net.URL;
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

    /**initial list of people*/
    private ObservableList<WorkerModel> workers = FXCollections.observableArrayList(
            new WorkerModel(1, "Badea Valentin"),
            new WorkerModel(2, "Popescu Ion"),
            new WorkerModel(56, "Badea Mihai"),
            new WorkerModel(4, "Nancu Petrica"),
            new WorkerModel(5, "Pesu Ciprian")
    );


    public void addSelectedCoworkersAction(ActionEvent actionEvent) {
        SelectedWorkersIDs listObject = new SelectedWorkersIDs();
        ObservableList<WorkerModel> localWorkers;
        localWorkers = tableCoworkers.getSelectionModel().getSelectedItems();

        int size = localWorkers.size();
        for (int i = 0; i < size; i++) {
            listObject.addWorker(localWorkers.get(i));
        }
        //listObject.printList();


        for(int i=0;i<size;i++){
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
