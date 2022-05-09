package ro.mta.licenta.badea.miniPagesControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;
import ro.mta.licenta.badea.Client;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateDeserializer;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateSerializer;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateTimeDeserializer;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateTimeSerializer;
import ro.mta.licenta.badea.ThreadToRunLoading;
import ro.mta.licenta.badea.models.*;
import ro.mta.licenta.badea.temporalUse.SenderText;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class viewProjectLvl3Controller implements Initializable {
    private HashMap<Integer, Integer> positionMap = new HashMap<Integer, Integer>();
    /**
     * ID task,Rand
     */
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
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        ProjectModel project = new ProjectModel();
        try {
            client.sendText(tosend.toString());

            Parent root = FXMLLoader.load(getClass().getResource("/MiniPages/LoadingPage.fxml"));

            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();

            primaryStage.initStyle(StageStyle.UNDECORATED);

            primaryStage.setScene(scene);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.show();

            String response = client.receiveText();

            project = gson.fromJson(response, ProjectModel.class);
            this.projectLocal = project;

            primaryStage.close();

            System.out.println(gson.toJson(projectLocal));
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**Start setting pages**/
        /**Task Scheduling Tab*/
        setTableTaskNotScheduled();
        setSchedulingTable();

        /**Set Add New Task Tab*/


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

//        for(TaskRealModel task:projectLocal.getListaTaskuriReale()){
//            System.out.println(task.toString());
//        }

//        LocalDate day = LocalDate.now().plusDays(3);
//        System.out.println(day);
//        ArrayList<TaskRealModel> listaTaskuriRealePerDay = new ArrayList<>();
//
//        for (TaskRealModel task : projectLocal.getListaTaskuriReale()) {
//            if (task.getDay().equals(day) && task.getStartTime() >= 0) {
//                listaTaskuriRealePerDay.add(task);
//                System.out.println(task.toString());
//            }
//        }
    //    System.out.println(listaTaskuriRealePerDay.size());


//        for (ResourceModel rs : projectLocal.getListaResurseCurente()) {
//            positionMap.clear();
//            calculatePositions(rs, listaTaskuriRealePerDay);
//            for (Integer key : positionMap.keySet()) {
//                System.out.println("key : " + key + " value : " + positionMap.get(key));
//            }
//        }


    }

    ObservableList<TaskModel> listaTaskuriForUser = FXCollections.observableArrayList();

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
    }

    public void setViewResourceAllocationTab() {
        setResourceAllocTable();
        selectedResourceLabel.setVisible(false);
        dateResAllocLabel.setVisible(false);
        dateForResourceAlloc.setVisible(false);

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
                resourceAllocNameLabel.setText(newSelection.getDenumire());

                setResourceAllocGrid(newSelection, dateForResourceAlloc.getValue());
                dateForResourceAlloc.valueProperty().addListener(new ChangeListener<LocalDate>() {
                    @Override
                    public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                        setResourceAllocGrid(newSelection, t1);
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
        shareableResourceColumn.setCellValueFactory(new PropertyValueFactory<>("shareable"));
        shareableResourceColumn.setStyle("-fx-alignment: CENTER ; ");

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

    public void setResourceAllocGrid(ResourceModel rs, LocalDate day) {

        ArrayList<TaskRealModel> listaTaskuriRealePerDay = new ArrayList<>();
        System.out.println("Current day :"+day + " size project: "+projectLocal.getListaTaskuriReale().size());
        for (TaskRealModel task : projectLocal.getListaTaskuriReale()) {
            if (task.getDay().equals(day) && task.getStartTime() >= 0 && task.getQuantityOfResourceRequest(rs.getId())>0) {
                listaTaskuriRealePerDay.add(task);
                //System.out.println(task.toString());
            }
        }
        System.out.println("Size day:"+listaTaskuriRealePerDay.size());
        System.out.println("Resource:"+ rs.toString());

        if(listaTaskuriRealePerDay.size()!=0) {
            GridPane grid = new GridPane();

            int currentRow = 0;
            int currentColumn = 1;
            int hour = 8;

            Button empty = new Button();
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

            positionMap.clear();
            calculatePositions(rs, listaTaskuriRealePerDay);

            for (Integer key : positionMap.keySet()) {
                System.out.println("key : " + key + " value : " + positionMap.get(key));
            }

            System.out.println("Size:"+listaTaskuriRealePerDay.size());
            for(TaskRealModel task:listaTaskuriRealePerDay){
                Button taskBtn=new Button(task.getName());
                taskBtn.setStyle("-fx-background-color:#E0FBFC;" +
                        "-fx-border-color:black");
                taskBtn.setMaxWidth(Double.MAX_VALUE);
                taskBtn.setMinWidth(Control.USE_PREF_SIZE);
                taskBtn.setMaxHeight(Double.MAX_VALUE);
                grid.add(taskBtn,task.getStartTime()+1,positionMap.get(task.getID())+1,
                        task.getDuration(),task.getQuantityOfResourceRequest(rs.getId()));
            }

//            Button Task = new Button("Create Database");
//            Task.setMaxWidth(Double.MAX_VALUE);
//            Task.setMinWidth(Control.USE_PREF_SIZE);
//            Task.setMaxHeight(Double.MAX_VALUE);
//            grid.add(Task, 1, 1, 1, 4);
//
//            Button a = new Button("Interface");
//            a.setMaxWidth(Double.MAX_VALUE);
//            a.setMinWidth(Control.USE_PREF_SIZE);
//            a.setMaxHeight(Double.MAX_VALUE);
//            grid.add(a, 1, 6, 4, 1);
            grid.setStyle("-fx-margin:2px");
            allocResScrollPane.setFitToHeight(true);
            allocResScrollPane.setContent(grid);

        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setContentText("There is no task for selected day!");
            alert.showAndWait();
        }
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

            // printTasksForCurrentUserOnSelectedDay(currentDate.plusDays(i));

            gridScheduling.add(button1, currentColumn, currentRow);
            button1.setMinWidth(Control.USE_PREF_SIZE);
            button1.setMaxWidth(Double.MAX_VALUE);
            button1.setMinHeight(Region.USE_PREF_SIZE);


            int idClient = Client.getInstance().getCurrentUser().getID();
            for (TaskRealModel task : projectLocal.getListaTaskuriReale()) {
                if (task.getDay().isEqual(currentDate.plusDays(i)) && idClient == task.getOriginTask().getExecutant().getID() && task.getStartTime() >= 0) {
                    Button taskBtn = new Button(task.getName());
                    taskBtn.setMinWidth(Control.USE_PREF_SIZE);
                    taskBtn.setMaxWidth(Double.MAX_VALUE);
                    gridScheduling.add(taskBtn, task.getStartTime() + 1, currentRow, task.getDuration(), 1);
                }
            }
            currentRow++;
        }


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
        for(TaskRealModel task:projectLocal.getListaTaskuriReale()){
            if(task.getStartTime()<0){
                listaTasksReal.add(task);
            }
        }

        tableTaskNotScheduled.setItems(listaTasksReal);
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


    /**
     * Functions to print Resources' allocation
     **/
    public void calculatePositions(ResourceModel res, ArrayList<TaskRealModel> listaTaskuri) {
        System.out.println("Resource: " + res.getDenumire() + " Quantity:" + res.getCantitate() + " Makespan:" + getMakespan(listaTaskuri));

        String[][] positionMat = new String[res.getCantitate()][];

        for (int i = 0; i < res.getCantitate(); i++) {
            positionMat[i] = new String[getMakespan(listaTaskuri)];
        }

        // System.out.println("Incepe pozitionarea!");

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
        // System.out.println("Taskuri aranjate:"+noTaskPut);
        if (found == 1)
            return;
        if (noTaskPut == listaTaskuri.size()) {
            //   System.out.println("Am pus :" + noTaskPut);
            //   System.out.println("FINISH");
            printMatrix(m, res.getCantitate(), getMakespan(listaTaskuri));
            // System.out.println("AICI");
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

                // System.out.println("Testez pentru :" + task.getName());
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
                    //   printMatrix(m,res.getCantitate(),getMakespan(listaTaskuri));

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
}
