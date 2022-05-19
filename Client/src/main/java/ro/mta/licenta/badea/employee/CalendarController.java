package ro.mta.licenta.badea.employee;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {
    @FXML
    private AnchorPane entirePane;

    @FXML
    private GridPane gridPane;

    @FXML
    private ScrollPane scrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            /**Set Date*/
            LocalDate currentDate = LocalDate.now();

            /**Fill grid*/

            /**Hours*/


            gridPane.setVgap(5);
            gridPane.setHgap(3);
            int numRows = 8;
            int numColumns = 25;
            for (int row = 0; row < numRows; row++) {
                RowConstraints rc = new RowConstraints();
                rc.setFillHeight(true);
                rc.setVgrow(Priority.ALWAYS);
                gridPane.getRowConstraints().add(rc);
            }
            for (int col = 0; col < numColumns; col++) {
                ColumnConstraints cc = new ColumnConstraints();
                cc.setFillWidth(true);
                cc.setHgrow(Priority.ALWAYS);
                gridPane.getColumnConstraints().add(cc);
            }

            int currentRow = 0;
            int currentColumn = 1;
            int hour = 8;

            for (int i = 0; i < 24; i++) {
                Button button = new Button(hour + "-" + (hour + 1));
                hour++;
                if (hour == 24) {
                    hour = 0;
                }
                button.setStyle("-fx-background-color:#e6b800; " +
                        "-fx-text-fill:black;");
                button.setMaxWidth(Double.MAX_VALUE);
                button.setMinWidth(Control.USE_PREF_SIZE);
                gridPane.add(button, currentColumn, currentRow);

                currentColumn++;
            }

            currentRow = 1;
            currentColumn = 0;
            for (int i = 0; i < 11; i++) {
                Button button1 = new Button(currentDate.plusDays(i).toString());
                button1.setStyle("-fx-background-color:#e6b800; " +
                        "-fx-text-fill:black;");
                gridPane.add(button1, currentColumn, currentRow);
                button1.setMinWidth(Control.USE_PREF_SIZE);
                button1.setMaxWidth(Double.MAX_VALUE);
                button1.setMinHeight(Region.USE_PREF_SIZE);
                currentRow++;
            }

            Button test = new Button("Create Database");
            test.setStyle("-fx-background-color:#E0FBFC;" +
                    "-fx-border-color:black");
            test.setMinWidth(Control.USE_PREF_SIZE);
            test.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(test, 1, 1, 3, 1);

            Button test2 = new Button("Maintenance");
            test2.setStyle("-fx-background-color:#E0FBFC;" +
                    "-fx-border-color:black");
            test2.setMinWidth(Control.USE_PREF_SIZE);
            test2.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(test2, 3, 4, 6, 1);

            Button test3 = new Button("Create Mail Server");
            test3.setStyle("-fx-background-color:#E0FBFC;" +
                    "-fx-border-color:black");
            test3.setMinWidth(Control.USE_PREF_SIZE);
            test3.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(test3, 2, 2, 4, 1);


            //scrollPane.setStyle("-fx-background-color:#98c1d9");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
