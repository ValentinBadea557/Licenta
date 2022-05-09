package ro.mta.licenta.badea.employee;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
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
import ro.mta.licenta.badea.models.ProjectModel;
import ro.mta.licenta.badea.temporalUse.SenderText;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Thread.sleep;

public class viewProjectsController implements Initializable {
    @FXML
    private TableColumn<ProjectModel, String> descriptionActiveColumn;

    @FXML
    private TableColumn<ProjectModel, String> dsecriptionFinishColumn;

    @FXML
    private TableColumn<ProjectModel, Integer> idActiveColumn;

    @FXML
    private TableColumn<ProjectModel, Integer> idFinishColumn;

    @FXML
    private TableColumn<ProjectModel, String> nameActiveColumn;

    @FXML
    private TableColumn<ProjectModel, String> nameFinishedColumn;

    @FXML
    private TableView<ProjectModel> tableActive;

    @FXML
    private TableView<ProjectModel> tableFinish;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        Client client = Client.getInstance();
        JSONObject tosend = new JSONObject();
        tosend.put("Type", "ViewProjects");
        tosend.put("IDuser", client.getCurrentUser().getID());
        String receive = null;
        try {
            client.sendText(tosend.toString());
            receive = client.receiveText();
            System.out.println(receive);
        } catch (Exception e) {
            e.printStackTrace();
        }



        Type listOfMyClassObject = new TypeToken<ArrayList<ProjectModel>>() {}.getType();
        List<ProjectModel> listaProiecte = gson.fromJson(receive, listOfMyClassObject);



        for (int i = 0; i < listaProiecte.size(); i++) {
            if (listaProiecte.get(i).getFinished() == 0) {
                activeList.add(listaProiecte.get(i));
            } else {
                finishedList.add(listaProiecte.get(i));
            }
        }

        /**Set table*/

        idActiveColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idActiveColumn.setStyle("-fx-alignment: CENTER ; ");
        nameActiveColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
        nameActiveColumn.setStyle("-fx-alignment: CENTER ; ");
        descriptionActiveColumn.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        descriptionActiveColumn.setStyle("-fx-alignment: CENTER ; ");
        tableActive.setItems(activeList);

        idFinishColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idFinishColumn.setStyle("-fx-alignment: CENTER ; ");
        nameFinishedColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
        nameFinishedColumn.setStyle("-fx-alignment: CENTER ; ");
        dsecriptionFinishColumn.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        dsecriptionFinishColumn.setStyle("-fx-alignment: CENTER ; ");
        tableFinish.setItems(finishedList);

        tableActive.getSelectionModel().selectedIndexProperty().addListener((obs, oldSelection, newSelection) -> {

            Parent root = null;
            try {

                SenderText data = new SenderText();

                data.setData(String.valueOf(tableActive.getSelectionModel().getSelectedItem().getID()));

                root = FXMLLoader.load(getClass().getResource("/MiniPages/ViewProjectLevel3.fxml"));
               // root = FXMLLoader.load(getClass().getResource("/MiniPages/LoadingPage.fxml"));



                Scene scene = new Scene(root);
                Stage primaryStage = new Stage();

                primaryStage.setTitle("Level 3 Priority");
                primaryStage.setScene(scene);
                primaryStage.initModality(Modality.APPLICATION_MODAL);

                primaryStage.setResizable(false);
                primaryStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    ObservableList<ProjectModel> activeList = FXCollections.observableArrayList();
    ObservableList<ProjectModel> finishedList = FXCollections.observableArrayList();

}
