package ro.mta.licenta.badea.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONObject;
import ro.mta.licenta.badea.Client;
import ro.mta.licenta.badea.models.CompanyModel;
import ro.mta.licenta.badea.models.ResourceModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ResourceAdminController implements Initializable {

    @FXML
    private TextArea ResourceDescription;

    @FXML
    private TextField ResourceName;

    @FXML
    private Spinner<Integer> SpinnerQuantity;

    @FXML
    private Button defineResourceButton;

    @FXML
    private TableView<ResourceModel> ResourcesTable;

    @FXML
    private TableColumn<ResourceModel, Integer> idColumn;

    @FXML
    private TableColumn<ResourceModel, String> descriptionColumn;

    @FXML
    private TableColumn<ResourceModel, String> nameColumn;

    @FXML
    private TableColumn<ResourceModel, Integer> quantityColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        updateTableView();

        /** set spinner */
        SpinnerValueFactory<Integer> spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        spinner.setValue(1);
        SpinnerQuantity.setValueFactory(spinner);

        /**set table */

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descriere"));

        ResourcesTable.setItems(resurseModels);
    }

    private ObservableList<ResourceModel> resurseModels = FXCollections.observableArrayList();

    public void updateTableView() {
        resurseModels.removeAll();
        ResourcesTable.getItems().clear();
        Client client = Client.getInstance();

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
    }

    public void defineResButton(ActionEvent actionEvent) throws Exception {

        boolean emptyFields = false;
        if (ResourceName.getText() == null || ResourceName.getText().trim().isEmpty()) {
            emptyFields = true;
            ResourceName.setStyle("-fx-border-color:red");
        } else {
            ResourceName.setStyle("-fx-border-color:none");
        }
        if (ResourceDescription.getText() == null || ResourceDescription.getText().trim().isEmpty()) {
            emptyFields = true;
            ResourceDescription.setStyle("-fx-border-color:red");
        } else {
            ResourceDescription.setStyle("-fx-border-color:none");
        }

        if (emptyFields) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete fields");
            alert.setContentText("Description missing!");
            alert.showAndWait();
        } else {
            Client client = Client.getInstance();

            String name = ResourceName.getText().toString();
            int number = SpinnerQuantity.getValue();
            String description = ResourceDescription.getText().toString();

            ResourceModel newResource = new ResourceModel();
            newResource.setDenumire(name);
            newResource.setCantitate(number);
            newResource.setDescriere(description);
            newResource.setIDcompanie(client.getCurrentUser().getCompany().getID());

            /**Build gson*/
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.setPrettyPrinting().create();
            String resourcesJson = gson.toJson(newResource);

            JSONObject jsonResource = new JSONObject(resourcesJson);
            jsonResource.put("Type", "Add Resource");

            client.sendText(jsonResource.toString());
            String response = client.receiveText();

            JSONObject responseJson = new JSONObject(response);
            if (responseJson.get("Response").toString().equals("ok")) {
                ResourceName.clear();
                ResourceDescription.clear();

                updateTableView();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Result");
                alert.setContentText("The resource was added into database!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setContentText(responseJson.get("Response").toString());
                alert.showAndWait();
            }

        }
    }

}
