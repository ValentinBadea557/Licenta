package ro.mta.licenta.badea.miniPagesControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ro.mta.licenta.badea.temporalUse.SelectedWorkersIDs;
import ro.mta.licenta.badea.temporalUse.SenderText;

import javax.swing.text.html.ImageView;
import java.net.URL;
import java.nio.channels.SelectionKey;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddRolesToWorkersController implements Initializable {
    @FXML
    private Label nameLabelField;

    @FXML
    private ComboBox<String> permissionComboBox;

    @FXML
    private TextField roleField;

    @FXML
    private Button setButton;

    @FXML
    private Tooltip toolTipPermission;

    @FXML
    private Label levelField;

    @FXML
    private Label resultLabel;

    ObservableList<String> levels =
            FXCollections.observableArrayList("Level 1", "Level 2", "Level 3");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**Set name*/
        SenderText Selectedname = new SenderText();
        nameLabelField.setText(Selectedname.getData());
        SelectedWorkersIDs lista = new SelectedWorkersIDs();

        /**Check if the role and permission is already set*/
        for (int i = 0; i < lista.finalList.size(); i++) {
            if (lista.finalList.get(i).getFullName() == Selectedname.getData()) {
                int id = lista.finalList.get(i).getID();
                for(int j=0;j<lista.listaEmployees.size();j++){
                    if(lista.listaEmployees.get(j).getID()==id){
                        if(lista.listaEmployees.get(j).getPermision()!=null){
                            permissionComboBox.setValue(lista.listaEmployees.get(j).getPermision());
                        }
                        if(lista.listaEmployees.get(j).getRole()!=null){
                            roleField.setText(lista.listaEmployees.get(j).getRole());
                        }
                    }
                }
            }

        }


        /**Set tooltip for roles*/
        toolTipPermission.setText("Level 1 : Only see task without permission to modify anything " +
                "Level 2 : Permission to modify tasks inside the team                   " +
                "Level 3 : Permission to modify tasks in the whole project");
        toolTipPermission.setPrefWidth(365);
        toolTipPermission.setWrapText(true);
        levelField.setTooltip(toolTipPermission);

        permissionComboBox.setItems(levels);


    }

    public void setRoleAction(ActionEvent actionEvent) {
        boolean isempty = false;

        if (roleField.getText().isEmpty()) {
            roleField.setStyle("-fx-border-color:red");
            isempty = true;
        } else {
            roleField.setStyle("-fx-border-color:none");
        }
        if (permissionComboBox.getValue() == null) {
            isempty = true;
            permissionComboBox.setStyle("-fx-border-color:red");
        } else {
            permissionComboBox.setStyle("-fx-border-color:none");
        }

        if (!isempty) {
            SenderText Selectedname = new SenderText();

            String role = roleField.getText().toString().toLowerCase();
            String permisiuni = permissionComboBox.getSelectionModel().getSelectedItem().toString();

            SelectedWorkersIDs lista = new SelectedWorkersIDs();

            for (int i = 0; i < lista.finalList.size(); i++) {
                if (lista.finalList.get(i).getFullName() == Selectedname.getData()) {
                    int id = lista.finalList.get(i).getID();
                    for(int j=0;j<lista.listaEmployees.size();j++){
                        if(lista.listaEmployees.get(j).getID()==id){
                            lista.listaEmployees.get(j).setRole(role);
                            lista.listaEmployees.get(j).setPermision(permisiuni);

                            resultLabel.setText("Role and permissions added!");
                            resultLabel.setStyle("-fx-text-fill:White");
                        }
                    }
                }

            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete fields");
            alert.setContentText("You must complete role and permissions!");
            alert.showAndWait();
        }
    }
}
