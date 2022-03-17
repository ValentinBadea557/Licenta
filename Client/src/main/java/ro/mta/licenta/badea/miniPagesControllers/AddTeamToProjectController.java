package ro.mta.licenta.badea.miniPagesControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ro.mta.licenta.badea.models.EmployeeModel;
import ro.mta.licenta.badea.models.TeamModel;
import ro.mta.licenta.badea.temporalUse.ProjectTemporalModel;
import ro.mta.licenta.badea.temporalUse.SelectedWorkersIDs;
import ro.mta.licenta.badea.temporalUse.WorkerModel;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class AddTeamToProjectController implements Initializable {

    @FXML
    private Button createTeamButton;

    @FXML
    private DatePicker deadlineField;

    @FXML
    private ComboBox<String> deadlineTimeComboBox;

    @FXML
    private TableColumn<WorkerModel, String> fullNameColumn;

    @FXML
    private TableColumn<WorkerModel, Integer> idColumn;

    @FXML
    private DatePicker starttimeDateField;

    @FXML
    private ComboBox<String> starttimeTimeComboBox;

    @FXML
    private TableView<WorkerModel> tableWorkersView;

    @FXML
    private TextField teamNameField;

    private Consumer<TeamModel> customerSelectCallback ;

    public void setCustomerSelectCallback(Consumer<TeamModel> callback) {
        this.customerSelectCallback = callback ;
    }

    ObservableList<String> hours =
            FXCollections.observableArrayList(
                    "00:00","00:30","01:00","01:30","02:00","02:30","03:00","03:30","04:00","04:30","05:00","05:30","06:00","06:30",
                    "07:00","07:30","08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30",
                    "15:00","15:30","16:00","16:30","17:00","17:30","18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:30","22:00","22:30",
                    "23:00","23:30"
            );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /** Set combo boxes*/
        starttimeTimeComboBox.getItems().clear();
        deadlineTimeComboBox.getItems().clear();
        starttimeTimeComboBox.setItems(hours);
        deadlineTimeComboBox.setItems(hours);

        /**Get list of employees*/
        SelectedWorkersIDs lista= new SelectedWorkersIDs();
        for(int i=0;i<lista.finalList.size();i++){
            workers.add(lista.finalList.get(i));
        }

        for (int i = 0; i < lista.listaEmployees.size(); i++) {
           employees.add(lista.listaEmployees.get(i));
        }


        /** Set Table */
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idColumn.setStyle("-fx-alignment: CENTER");
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        fullNameColumn.setStyle("-fx-alignment: CENTER");
        tableWorkersView.setItems(workers);

        tableWorkersView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


    }

    public void createTeamAction(ActionEvent actionEvent) {
        boolean isempty=false;

        if(teamNameField.getText().isEmpty()){
            isempty=true;
            teamNameField.setStyle("-fx-border-color:red");
        }else{
            teamNameField.setStyle("-fx-border-color:none");
        }
        if(starttimeDateField.getValue()==null){
            isempty=true;
            starttimeDateField.setStyle("-fx-border-color:red");
        }else{
            starttimeDateField.setStyle("-fx-border-color:none");
        }
        if(starttimeTimeComboBox.getValue()==null){
            isempty=true;
            starttimeTimeComboBox.setStyle("-fx-border-color:red");
        }else{
            starttimeTimeComboBox.setStyle("-fx-border-color:none");
        }
        if(deadlineField.getValue()==null){
            isempty=true;
            deadlineField.setStyle("-fx-border-color:red");
        }else{
            deadlineField.setStyle("-fx-border-color:none");
        }
        if(deadlineTimeComboBox.getValue()==null){
            isempty=true;
            deadlineTimeComboBox.setStyle("-fx-border-color:red");
        }else{
            deadlineTimeComboBox.setStyle("-fx-border-color:none");
        }
        if(tableWorkersView.getSelectionModel().getSelectedItems().isEmpty()){
            tableWorkersView.setStyle("-fx-border-color:red");
            isempty=true;
        }else{
            tableWorkersView.setStyle("-fx-border-color:none");
        }

        if(!isempty) {
            /** Get data from every field*/
            String name = teamNameField.getText().toString();
            LocalDate startDate = starttimeDateField.getValue();
            LocalDate deadDate = deadlineField.getValue();
            String startTime = starttimeTimeComboBox.getValue();
            String deadTime = deadlineTimeComboBox.getValue();
            LocalDateTime finalStartTime = returnFinalDateTimeFormat(startDate, startTime);
            LocalDateTime finalDeadline = returnFinalDateTimeFormat(deadDate, deadTime);


            /** Get selected workers from table*/
            TeamModel echipa = new TeamModel();
            ObservableList<WorkerModel> selectedWorkers;
            selectedWorkers = tableWorkersView.getSelectionModel().getSelectedItems();
            for (int i = 0; i < selectedWorkers.size(); i++) {
                for (int j = 0; j < employees.size(); j++) {
                    if (selectedWorkers.get(i).getID() == employees.get(j).getID()) {
                        echipa.addEmployee(employees.get(j));
                    }
                }
            }
            echipa.setName(name);
            echipa.setStarttime(finalStartTime);
            echipa.setDeadline(finalDeadline);

            ProjectTemporalModel proiectNew = new ProjectTemporalModel();
            proiectNew.addTeam(echipa);

            if (customerSelectCallback != null) {
                customerSelectCallback.accept(echipa);
            }

            System.out.println(echipa.getName()+" "+echipa.getStarttime()+" "+echipa.getDeadline());
        }
    }

    private ObservableList<WorkerModel> workers = FXCollections.observableArrayList();

    private ObservableList<EmployeeModel> employees = FXCollections.observableArrayList();

    public LocalDateTime returnFinalDateTimeFormat(LocalDate date, String time){
        /**get infomartion from LocalDate*/
        int year= date.getYear();
        int month=date.getMonth().getValue();
        int day=date.getDayOfMonth();

        /**get hour and minute from combobox time*/
        String[] timeTokenizer=time.split(":");

        int hour=Integer.valueOf(timeTokenizer[0]);
        int minutes=Integer.valueOf(timeTokenizer[1]);
        int seconds=1;

        /**return final format that match with SQL format*/
        LocalDateTime finalDateTime = LocalDateTime.of(year, month, day, hour, minutes, seconds);

        return finalDateTime;
    }
}


