package ro.mta.licenta.badea.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ro.mta.licenta.badea.models.EmployeeModel;
import ro.mta.licenta.badea.models.ProjectModel;
import ro.mta.licenta.badea.temporalUse.ProjectTemporalModel;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ProjectAdminController implements Initializable {


    @FXML
    private TableColumn<ProjectModel, String> detailsColumn;

    @FXML
    private TableColumn<ProjectModel, String> leaderColumn;

    @FXML
    private TableColumn<ProjectModel, String> nameColumn;

    @FXML
    private TableColumn<ProjectModel, LocalDateTime> startColumn;

    @FXML
    private TableColumn<ProjectModel, LocalDateTime> deadlineColumn;

    @FXML
    private TableView<ProjectModel> projectTable;

    @FXML
    private TextField searchProjectField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**set table */
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
        leaderColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCoordonatorFullName()));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("starttime"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("descriere"));

        projectTable.setItems(projectModels);

        FilteredList<ProjectModel> filteredData = new FilteredList<>(projectModels, b -> true);

        searchProjectField.textProperty().addListener((observable,oldValue,newValue)->{
            filteredData.setPredicate(projectModel ->{

                if(newValue.isEmpty() || newValue.isBlank() || newValue==null){
                    return true;
                }
                String searchKeyword=newValue.toLowerCase();
                if(projectModel.getNume().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }else if(projectModel.getDescriere().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }
                else if(projectModel.getCoordonatorFullName().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }
                else
                    return false; //no match
            });
        });

        SortedList<ProjectModel> sortedData=new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(projectTable.comparatorProperty());

        projectTable.setItems(sortedData);
    }


    private ObservableList<ProjectModel> projectModels = FXCollections.observableArrayList(
            new ProjectModel("Soft",new EmployeeModel("Badea","Valentin"), LocalDate.now(),LocalDate.now(),"descriere 1"),
            new ProjectModel("Hard",new EmployeeModel("Mihai","Andrei"), LocalDate.now(),LocalDate.now(),"descriere 2")
    );
}
