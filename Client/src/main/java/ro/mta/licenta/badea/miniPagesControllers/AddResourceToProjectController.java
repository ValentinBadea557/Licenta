package ro.mta.licenta.badea.miniPagesControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.json.JSONObject;
import ro.mta.licenta.badea.Client;
import ro.mta.licenta.badea.models.CompanyModel;
import ro.mta.licenta.badea.models.ResourceModel;
import ro.mta.licenta.badea.models.TeamModel;
import ro.mta.licenta.badea.temporalUse.SelectedWorkersIDs;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class AddResourceToProjectController implements Initializable {

    @FXML
    private Label InfoLabel;

    @FXML
    private Button addResourceButton;

    @FXML
    private TableColumn<ResourceModel, Integer> idColumn;

    @FXML
    private TableColumn<ResourceModel, String> nameColumn;

    @FXML
    private Label nameLabel;

    @FXML
    private TableColumn<ResourceModel, Integer> quantityColumn;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    private TableView<ResourceModel> resourceTable;

    @FXML
    private TableColumn<ResourceModel, Boolean> shareableColumn;

    @FXML
    void addResourceAction(ActionEvent event) {
        ResourceModel selectedResource;
        selectedResource = resourceTable.getSelectionModel().getSelectedItem();

        SelectedWorkersIDs lista=new SelectedWorkersIDs();


        if(quantitySpinner.getValue()>selectedResource.getCantitate()){
            InfoLabel.setText("Unavailable number of resources!");
            InfoLabel.setStyle("-fx-text-fill:RED");

            quantitySpinner.setStyle("-fx-border-color:red");
        }else{
            quantitySpinner.setStyle("-fx-border-color:none");
            InfoLabel.setText("Resource added!");
            InfoLabel.setStyle("-fx-text-fill:GREEN");

            resurseModels.remove(selectedResource);
            resourceTable.setItems(resurseModels);

            selectedResource.setCantitate(quantitySpinner.getValue());
            lista.listaResProiect.add(selectedResource);

        }
    }

    private Consumer<ResourceModel> customerSelectCallback ;
    public void setCustomerSelectCallback(Consumer<ResourceModel> callback) {
        this.customerSelectCallback = callback ;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SelectedWorkersIDs listObject = new SelectedWorkersIDs();
        listObject.clearResourceList();

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

        resourceTable.setItems(resurseModels);

        /**Handle event on selected item */
        resourceTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                nameLabel.setText(resourceTable.getSelectionModel().getSelectedItem().getDenumire());
            }
        });

        /**Set spinner*/
        SpinnerValueFactory<Integer> spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,999999);
        spinner.setValue(1);
        quantitySpinner.setValueFactory(spinner);
    }

    private ObservableList<ResourceModel> resurseModels = FXCollections.observableArrayList();

}
