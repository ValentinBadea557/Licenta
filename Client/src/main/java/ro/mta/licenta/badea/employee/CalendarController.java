package ro.mta.licenta.badea.employee;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.json.JSONArray;
import org.json.JSONObject;
import ro.mta.licenta.badea.Client;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateDeserializer;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateSerializer;
import ro.mta.licenta.badea.models.ProjectModel;
import ro.mta.licenta.badea.models.TaskRealModel;

import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.time.temporal.ChronoUnit.DAYS;

public class CalendarController implements Initializable {


    @FXML
    private AnchorPane entirePane;

    @FXML
    private GridPane gridPane;

    @FXML
    private ScrollPane scrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        Client client = Client.getInstance();
        JSONObject send = new JSONObject();
        send.put("Type", "View Future Tasks");
        send.put("idUser", client.getCurrentUser().getID());



        try {
            client.sendText(send.toString());
            String response = client.receiveText();


            Type userListType = new TypeToken<ArrayList<TaskRealModel>>(){}.getType();
            ArrayList<TaskRealModel> listaTaskuri = gson.fromJson(response, userListType);

            /**Set Date*/
            LocalDate currentDate = LocalDate.now();

            /**Fill grid*/

            /**Hours*/


            gridPane.setVgap(5);
            gridPane.setHgap(3);
//            int numRows = 8;
//            int numColumns = 25;
//            for (int row = 0; row < numRows; row++) {
//                RowConstraints rc = new RowConstraints();
//                rc.setFillHeight(true);
//                rc.setVgrow(Priority.ALWAYS);
//                gridPane.getRowConstraints().add(rc);
//            }
//            for (int col = 0; col < numColumns; col++) {
//                ColumnConstraints cc = new ColumnConstraints();
//                cc.setFillWidth(true);
//                cc.setHgrow(Priority.ALWAYS);
//                gridPane.getColumnConstraints().add(cc);
//            }
//
//            int currentRow = 0;
//            int currentColumn = 1;
//            int hour = 8;
//
//            for (int i = 0; i < 24; i++) {
//                Button button = new Button(hour + "-" + (hour + 1));
//                hour++;
//                if (hour == 24) {
//                    hour = 0;
//                }
//                button.setStyle("-fx-background-color:#e6b800; " +
//                        "-fx-text-fill:black;");
//                button.setMaxWidth(Double.MAX_VALUE);
//                button.setMinWidth(Control.USE_PREF_SIZE);
//                gridPane.add(button, currentColumn, currentRow);
//
//                currentColumn++;
//            }
//
//            currentRow = 1;
//            currentColumn = 0;
//            for (int i = 0; i < 11; i++) {
//                Button button1 = new Button(currentDate.plusDays(i).toString());
//                button1.setStyle("-fx-background-color:#e6b800; " +
//                        "-fx-text-fill:black;");
//                gridPane.add(button1, currentColumn, currentRow);
//                button1.setMinWidth(Control.USE_PREF_SIZE);
//                button1.setMaxWidth(Double.MAX_VALUE);
//                button1.setMinHeight(Region.USE_PREF_SIZE);
//                currentRow++;
//            }

            for(TaskRealModel task:listaTaskuri){
                Button taskBtn=new Button(task.getName());
                taskBtn.setStyle("-fx-background-color:#E0FBFC;" +
                        "-fx-border-color:black");
                taskBtn.setMinWidth(Control.USE_PREF_SIZE);
                taskBtn.setMaxWidth(Double.MAX_VALUE);
                long daysBetween = DAYS.between(LocalDate.now(), task.getDay());
                gridPane.add(taskBtn,task.getStartTime()+1, Math.toIntExact(daysBetween + 1),task.getDuration(),1);
                System.out.println(task.getName()+" "+daysBetween+" start:"+task.getStartTime());
            }

            Node[] verticalHeaders=new Node[]{
                    createEmptyButton(),
                createHeaderNode(0),
                createHeaderNode(1),
                createHeaderNode(2),
                createHeaderNode(3),
                    createHeaderNode(4),
                    createHeaderNode(5),
                    createHeaderNode(6),
                    createHeaderNode(7),
                    createHeaderNode(8),
                    createHeaderNode(9),
                    createHeaderNode(10)
            };

            Node[] horizontalHeaders=new Node[]{
                    createHeaderNode("07-08"),
                    createHeaderNode("08-09"),
                    createHeaderNode("09-10"),
                    createHeaderNode("10-11"),
                    createHeaderNode("11-12"),
                    createHeaderNode("12-13"),
                    createHeaderNode("13-14"),
                    createHeaderNode("14-15"),
                    createHeaderNode("15-16"),
                    createHeaderNode("16-17"),
                    createHeaderNode("17-18"),
                    createHeaderNode("18-19"),
                    createHeaderNode("19-20"),
                    createHeaderNode("20-21"),
                    createHeaderNode("21-22"),
                    createHeaderNode("22-23"),
                    createHeaderNode("23-00"),
                    createHeaderNode("00-01"),
                    createHeaderNode("01-02"),
                    createHeaderNode("02-03"),
                    createHeaderNode("03-04"),
                    createHeaderNode("04-05"),
                    createHeaderNode("05-06"),
                    createHeaderNode("06-07")
            };


            gridPane.addColumn(0,verticalHeaders);
            gridPane.addRow(0,horizontalHeaders);

            InvalidationListener headerUpdater = o -> {
                final double tx = (gridPane.getWidth() - scrollPane.getViewportBounds().getWidth()) * scrollPane.getHvalue();

                for (Node header : verticalHeaders) {
                    header.setTranslateX(tx);
                }
            };
            gridPane.widthProperty().addListener(headerUpdater);
            scrollPane.viewportBoundsProperty().addListener(headerUpdater);
            scrollPane.hvalueProperty().addListener(headerUpdater);

            InvalidationListener headerUpdaterUp = o -> {
                final double ty = (gridPane.getHeight() - scrollPane.getViewportBounds().getHeight()) * scrollPane.getVvalue();

                for (Node header : horizontalHeaders) {
                    header.setTranslateY(ty);
                }
            };
            gridPane.heightProperty().addListener(headerUpdaterUp);
            scrollPane.viewportBoundsProperty().addListener(headerUpdaterUp);
            scrollPane.vvalueProperty().addListener(headerUpdaterUp);


            //scrollPane.setStyle("-fx-background-color:#98c1d9");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private  static Button createEmptyButton(){
        Button button1 = new Button();
        button1.setVisible(false);
        button1.setMinWidth(Control.USE_PREF_SIZE);
        button1.setMaxWidth(Double.MAX_VALUE);
        button1.setMinHeight(Region.USE_PREF_SIZE);
        return button1;
    }

    private static Button createHeaderNode(String str) {
        Button button1 = new Button(str);
        button1.setStyle("-fx-background-color:#e6b800; " +
                "-fx-text-fill:black;");
        button1.setMinWidth(Control.USE_PREF_SIZE);
        button1.setMaxWidth(Double.MAX_VALUE);
        button1.setMinHeight(Region.USE_PREF_SIZE);
        return button1;
    }


    private static Button createHeaderNode(int i) {
        Button button1 = new Button(LocalDate.now().plusDays(i).toString());
                button1.setStyle("-fx-background-color:#e6b800; " +
                        "-fx-text-fill:black;");
                button1.setMinWidth(Control.USE_PREF_SIZE);
                button1.setMaxWidth(Double.MAX_VALUE);
                button1.setMinHeight(Region.USE_PREF_SIZE);
        return button1;
    }
}
