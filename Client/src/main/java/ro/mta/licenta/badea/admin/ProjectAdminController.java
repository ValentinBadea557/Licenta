package ro.mta.licenta.badea.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import org.json.JSONObject;
import ro.mta.licenta.badea.Client;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateDeserializer;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateSerializer;
import ro.mta.licenta.badea.models.EmployeeModel;
import ro.mta.licenta.badea.models.ProjectModel;
import ro.mta.licenta.badea.temporalUse.ProjectTemporalModel;

import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

        /****/
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        Client client = Client.getInstance();
        JSONObject tosend = new JSONObject();
        tosend.put("Type", "ViewProjects all");
        tosend.put("IDcompany", client.getCurrentUser().getCompany().getID());
        String receive = null;
        try {
            client.sendText(tosend.toString());
            receive = client.receiveText();
            System.out.println(receive);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Type listOfMyClassObject = new TypeToken<ArrayList<ProjectModel>>() {
        }.getType();
        List<ProjectModel> listaProiecte = gson.fromJson(receive, listOfMyClassObject);
        projectModels.setAll(listaProiecte);
        /****/


        projectTable.setItems(projectModels);

        FilteredList<ProjectModel> filteredData = new FilteredList<>(projectModels, b -> true);

        searchProjectField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(projectModel -> {

                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (projectModel.getNume().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (projectModel.getDescriere().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (projectModel.getCoordonatorFullName().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else
                    return false; //no match
            });
        });

        SortedList<ProjectModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(projectTable.comparatorProperty());

        projectTable.setItems(sortedData);


    }


    private ObservableList<ProjectModel> projectModels = FXCollections.observableArrayList(
            new ProjectModel("Soft", new EmployeeModel("Badea", "Valentin"), LocalDate.now(), LocalDate.now(), "descriere 1"),
            new ProjectModel("Hard", new EmployeeModel("Mihai", "Andrei"), LocalDate.now(), LocalDate.now(), "descriere 2")
    );
}
