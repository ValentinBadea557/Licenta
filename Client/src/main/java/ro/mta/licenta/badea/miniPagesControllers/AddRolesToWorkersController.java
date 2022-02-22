package ro.mta.licenta.badea.miniPagesControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ro.mta.licenta.badea.temporalUse.SenderText;

import javax.swing.text.html.ImageView;
import java.net.URL;
import java.nio.channels.SelectionKey;
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

    ObservableList<String> levels =
            FXCollections.observableArrayList("Level 1", "Level 2", "Level 3");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**Set tooltip for roles*/
        toolTipPermission.setText("Level 1 : Only see task without permission to modify anything " +
                "Level 2 : Permission to modify tasks inside the team                   " +
                "Level 3 : Permission to modify tasks in the whole project");
        toolTipPermission.setPrefWidth(365);
        toolTipPermission.setWrapText(true);
        levelField.setTooltip(toolTipPermission);

        permissionComboBox.setItems(levels);

        SenderText Selectedname = new SenderText();
        nameLabelField.setText(Selectedname.getData());
    }

    public void setRoleAction(ActionEvent actionEvent) {
        String role = roleField.getText();
        String permisiuni = permissionComboBox.getSelectionModel().getSelectedItem();
        if (role == null || permisiuni == null || role.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete fields");
            alert.setContentText("You must complete role and permissions!");
            alert.showAndWait();
        }else{
            System.out.println("Rol: "+role+"\nPermisiuni: "+permisiuni+"\n");
        }

    }
}
