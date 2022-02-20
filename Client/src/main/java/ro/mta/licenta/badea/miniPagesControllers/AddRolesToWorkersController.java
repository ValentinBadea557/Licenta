package ro.mta.licenta.badea.miniPagesControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.swing.text.html.ImageView;
import java.net.URL;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       toolTipPermission.setText("Level 1 : Only see task without permission to modify anything " +
                                 "Level 2 : Permission to modify tasks inside the team                   " +
                                 "Level 3 : Permission to modify tasks in the whole project");
       toolTipPermission.setPrefWidth(365);
       toolTipPermission.setWrapText(true);
       levelField.setTooltip(toolTipPermission);

    }
}
