package ro.mta.licenta.badea.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ro.mta.licenta.badea.models.ResourceModel;

import java.net.URL;
import java.util.ResourceBundle;


public class ResourceAdminController implements Initializable {

    @FXML
    private TextArea ResourceDescription;

    @FXML
    private TextField ResourceName;


    @FXML
    private RadioButton ShareableNo;

    @FXML
    private RadioButton ShareableYes;

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

    @FXML
    private TableColumn<ResourceModel, Boolean> shareableColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /** group for radio buttons*/
        final ToggleGroup group=new ToggleGroup();
        ShareableNo.setToggleGroup(group);
        ShareableNo.setSelected(true);

        ShareableYes.setToggleGroup(group);
        ShareableYes.setSelected(false);
        /** set spinner */
        SpinnerValueFactory<Integer> spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        spinner.setValue(1);
        SpinnerQuantity.setValueFactory(spinner);

        /**set table */

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        shareableColumn.setCellValueFactory(new PropertyValueFactory<>("shareable"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descriere"));

        ResourcesTable.setItems(resurseModels);
    }

    private ObservableList<ResourceModel> resurseModels = FXCollections.observableArrayList(
            new ResourceModel(1,"R1", 100,true,"Descriere 1"),
            new ResourceModel(2,"R2", 154,false,"Descriere 2")
            );

    public void defineResButton(ActionEvent actionEvent) {
        System.out.println(ResourceName.getText().toString());
        System.out.println(SpinnerQuantity.getEditor().getText().toString());
        if(ShareableYes.isSelected()){
            System.out.println("Yes");
        }else if(ShareableNo.isSelected()){
            System.out.println("No");
        }
        System.out.println(ResourceDescription.getText().toString());
    }

//    @FXML
//    void defineResourceAction(ActionEvent event) {
//        String a=SpinnerQuantity.getEditor().getText();
//        System.out.println(a);
//    }
}
