package ro.mta.licenta.badea.miniPagesControllers;

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
import ro.mta.licenta.badea.models.ResourceModel;
import ro.mta.licenta.badea.temporalUse.SelectedWorkersIDs;
import ro.mta.licenta.badea.temporalUse.SenderText;
import ro.mta.licenta.badea.temporalUse.WorkerModel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
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

    private ObservableList<ResourceModel> resurseModels = FXCollections.observableArrayList(
            new ResourceModel(1,"R1", 10,true,"Descriere 1"),
            new ResourceModel(2,"R2", 154,false,"Descriere 2"),
            new ResourceModel(3,"R3", 200,false,"Descriere 2"),
            new ResourceModel(4,"R4", 187,true,"Descriere 2"),
            new ResourceModel(5,"R5", 654,false,"Descriere 2")
    );

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
