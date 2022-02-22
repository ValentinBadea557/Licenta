package ro.mta.licenta.badea.admin;

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
import ro.mta.licenta.badea.temporalUse.ProjectTemporalModel;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ProjectAdminController implements Initializable {


    @FXML
    private TableColumn<ProjectTemporalModel, String> detailsColumn;

    @FXML
    private TableColumn<ProjectTemporalModel, String> leaderColumn;

    @FXML
    private TableColumn<ProjectTemporalModel, String> nameColumn;

    @FXML
    private TableColumn<ProjectTemporalModel, LocalDateTime> startColumn;

    @FXML
    private TableColumn<ProjectTemporalModel, LocalDateTime> deadlineColumn;

    @FXML
    private TableView<ProjectTemporalModel> projectTable;

    @FXML
    private TextField searchProjectField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**set table */
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("numeProiect"));
        leaderColumn.setCellValueFactory(new PropertyValueFactory<>("leader"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("starttime"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));

        projectTable.setItems(projectModels);

        FilteredList<ProjectTemporalModel> filteredData = new FilteredList<>(projectModels, b -> true);

        searchProjectField.textProperty().addListener((observable,oldValue,newValue)->{
            filteredData.setPredicate(projectModel ->{

                if(newValue.isEmpty() || newValue.isBlank() || newValue==null){
                    return true;
                }
                String searchKeyword=newValue.toLowerCase();
                if(projectModel.getNumeProiect().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }else if(projectModel.getDetails().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }else if(projectModel.getLeader().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }else
                    return false; //no match
            });
        });

        SortedList<ProjectTemporalModel> sortedData=new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(projectTable.comparatorProperty());

        projectTable.setItems(sortedData);
    }


    private ObservableList<ProjectTemporalModel> projectModels = FXCollections.observableArrayList(
            new ProjectTemporalModel("Soft","Valentin Badea", LocalDateTime.now(),LocalDateTime.now(),"descriere 1"),
            new ProjectTemporalModel("Hard","Valentin Badea 2", LocalDateTime.now(),LocalDateTime.now(),"descriere 2")
    );
}
