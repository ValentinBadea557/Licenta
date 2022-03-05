package ro.mta.licenta.badea.miniPagesControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONObject;
import ro.mta.licenta.badea.Client;
import ro.mta.licenta.badea.models.CompanyModel;
import ro.mta.licenta.badea.models.ResourceModel;
import ro.mta.licenta.badea.temporalUse.SelectedWorkersIDs;
import ro.mta.licenta.badea.temporalUse.SenderText;
import ro.mta.licenta.badea.temporalUse.WorkerModel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddResourceToTaskController implements Initializable {

    @FXML
    private Button addResourceButton;

    @FXML
    private TableColumn<ResourceModel, Integer> idColumn;

    @FXML
    private Label labelResponse;

    @FXML
    private TableColumn<ResourceModel, String> nameColumn;

    @FXML
    private TableColumn<ResourceModel, Integer> quantityColumn;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    private Label selectedNameLabel;

    @FXML
    private TableColumn<ResourceModel, Boolean> shareableColumn;

    @FXML
    private TableView<ResourceModel> tableResource;

    @FXML
    private Button finishButton;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**Set spinner*/
        SpinnerValueFactory<Integer> spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,999999);
        spinner.setValue(1);
        quantitySpinner.setValueFactory(spinner);

        /**Set table*/
        Client client= Client.getInstance();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        JSONObject requestView = new JSONObject();
        requestView.put("Type", "View Resources");
        requestView.put("ID_Companie", client.getCurrentUser().getCompany().getID());
        CompanyModel company = new CompanyModel();
        try {
            client.sendText(requestView.toString());
            String response = client.receiveText();
            company = gson.fromJson(response, CompanyModel.class);

            ArrayList<ResourceModel> listaTemporala = company.getListaResurse();

            for (int i = 0; i < listaTemporala.size(); i++) {
                resurseModels.add(listaTemporala.get(i));
            }
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setStyle("-fx-alignment: CENTER");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        nameColumn.setStyle("-fx-alignment: CENTER");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        quantityColumn.setStyle("-fx-alignment: CENTER");
        shareableColumn.setCellValueFactory(new PropertyValueFactory<>("shareable"));
        shareableColumn.setStyle("-fx-alignment: CENTER");

        tableResource.setItems(resurseModels);

        /**Handle event on selected item */
        tableResource.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectedNameLabel.setText(tableResource.getSelectionModel().getSelectedItem().getDenumire());
            }
        });


    }

    private ObservableList<ResourceModel> resurseModels = FXCollections.observableArrayList();

    public void addResourceAction(ActionEvent actionEvent) {
        SelectedWorkersIDs listObject = new SelectedWorkersIDs();
        ResourceModel selectedResource;
        selectedResource = tableResource.getSelectionModel().getSelectedItem();

        if(quantitySpinner.getValue()>selectedResource.getCantitate()){
            labelResponse.setText("Unavailable number of resources!");
            labelResponse.setStyle("-fx-text-fill:RED");

            quantitySpinner.setStyle("-fx-border-color:red");
        }else{
            quantitySpinner.setStyle("-fx-border-color:none");
            labelResponse.setText("Resource added!");
            labelResponse.setStyle("-fx-text-fill:GREEN");
            selectedResource.setCantitate(quantitySpinner.getValue());
            listObject.addResource(selectedResource);

            resurseModels.remove(selectedResource);
            tableResource.setItems(resurseModels);
        }


    }

    public void finishAction(ActionEvent actionEvent) {
        Stage stage = (Stage) finishButton.getScene().getWindow();
        stage.close();
    }
}
