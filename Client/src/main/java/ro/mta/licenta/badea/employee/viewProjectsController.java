package ro.mta.licenta.badea.employee;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;
import ro.mta.licenta.badea.Client;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateDeserializer;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateSerializer;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateTimeDeserializer;
import ro.mta.licenta.badea.GsonDateFormat.LocalDateTimeSerializer;
import ro.mta.licenta.badea.ThreadToRunLoading;
import ro.mta.licenta.badea.models.ProjectModel;
import ro.mta.licenta.badea.temporalUse.SenderText;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

import static java.lang.Thread.sleep;

public class viewProjectsController implements Initializable {
    private Parent root;
    @FXML
    private TableColumn<ProjectModel, String> descriptionActiveColumn;

    @FXML
    private TableColumn<ProjectModel, String> dsecriptionFinishColumn;

    @FXML
    private TableColumn<ProjectModel, Integer> idActiveColumn;

    @FXML
    private TableColumn<ProjectModel, Integer> idFinishColumn;

    @FXML
    private TableColumn<ProjectModel, String> nameActiveColumn;

    @FXML
    private TableColumn<ProjectModel, String> nameFinishedColumn;

    @FXML
    private TableView<ProjectModel> tableActive;

    @FXML
    private TableView<ProjectModel> tableFinish;

    @FXML
    private ScrollPane scrollPaneViewProjects;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        Client client = Client.getInstance();
        JSONObject tosend = new JSONObject();
        tosend.put("Type", "ViewProjects");
        tosend.put("IDuser", client.getCurrentUser().getID());
        String receive = null;
        try {
            client.sendText(tosend.toString());
            receive = client.receiveText();
            System.out.println(receive);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Type listOfMyClassObject = new TypeToken<ArrayList<ProjectModel>>() {
        }.getType();
        List<ProjectModel> listaProiecte = gson.fromJson(receive, listOfMyClassObject);


//        for (int i = 0; i < listaProiecte.size(); i++) {
//            if (listaProiecte.get(i).getFinished() == 0) {
//                activeList.add(listaProiecte.get(i));
//            } else {
//                finishedList.add(listaProiecte.get(i));
//            }
//        }
//
//        /**Set table*/
//
//        idActiveColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
//        idActiveColumn.setStyle("-fx-alignment: CENTER ; ");
//        nameActiveColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
//        nameActiveColumn.setStyle("-fx-alignment: CENTER ; ");
//        descriptionActiveColumn.setCellValueFactory(new PropertyValueFactory<>("descriere"));
//        descriptionActiveColumn.setStyle("-fx-alignment: CENTER ; ");
//        tableActive.setItems(activeList);
//
//        idFinishColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
//        idFinishColumn.setStyle("-fx-alignment: CENTER ; ");
//        nameFinishedColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
//        nameFinishedColumn.setStyle("-fx-alignment: CENTER ; ");
//        dsecriptionFinishColumn.setCellValueFactory(new PropertyValueFactory<>("descriere"));
//        dsecriptionFinishColumn.setStyle("-fx-alignment: CENTER ; ");
//        tableFinish.setItems(finishedList);
//
//        tableActive.getSelectionModel().selectedIndexProperty().addListener((obs, oldSelection, newSelection) -> {
//
//
//            try {
//
//                SenderText data = new SenderText();
//
//                data.setData(String.valueOf(tableActive.getSelectionModel().getSelectedItem().getID()));
//
//                root = FXMLLoader.load(getClass().getResource("/MiniPages/ViewProjectLevel3.fxml"));
//                // root = FXMLLoader.load(getClass().getResource("/MiniPages/LoadingPage.fxml"));
//
//
//                Scene scene = new Scene(root);
//                Stage primaryStage = new Stage();
//
//                primaryStage.setTitle("Level 3 Priority");
//                primaryStage.setScene(scene);
//                primaryStage.initModality(Modality.APPLICATION_MODAL);
//
//                primaryStage.setResizable(false);
//                primaryStage.showAndWait();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        });


//        pane.setPrefWidth(200);
//        pane.setPrefHeight(150);

        GridPane gp = new GridPane();
        gp.setHgap(37.5);
        gp.setVgap(10);
        int row, column;
        row = 0;
        column = 0;
        for (int i = 0; i < listaProiecte.size(); i++) {
            String labelStr = new String();
            labelStr += listaProiecte.get(i).getNume();
            Label prjNameLabel = new Label(labelStr);
            prjNameLabel.relocate(20, 20);
            prjNameLabel.setStyle("-fx-text-fill:black; -fx-font-weight: bold ;");
            // prjNameLabel.setFont(new Font("System",15));
            prjNameLabel.setWrapText(true);

            String startStr = new String("Start : ");
            startStr += listaProiecte.get(i).getStarttime().toString();
            Label startLabel = new Label(startStr);
            startLabel.relocate(20, 40);
            startLabel.setStyle("-fx-text-fill:black");

            String deadStr = new String("Deadline : ");
            deadStr += listaProiecte.get(i).getDeadline().toString();
            System.out.println(deadStr);
            Label deadLabel = new Label(deadStr);
            deadLabel.relocate(20, 60);
            deadLabel.setStyle("-fx-text-fill:black");

            LocalDate current = LocalDate.now();
            ProgressIndicator pi = new ProgressIndicator();
            if (current.isAfter(listaProiecte.get(i).getDeadline())) {
                pi.setProgress(1F);
            } else if (current.isBefore(listaProiecte.get(i).getStarttime())) {

                pi.setProgress(0F);
            } else {

                long daysAfterStart = ChronoUnit.DAYS.between(listaProiecte.get(i).getStarttime(), current);
                long totalDays = ChronoUnit.DAYS.between(listaProiecte.get(i).getStarttime(), listaProiecte.get(i).getDeadline());
                double procent = (double) daysAfterStart / totalDays;
                pi.setProgress(procent);
            }

            pi.setStyle("-fx-fill:black");
            pi.relocate(90, 90);

            VBox vertical = new VBox();
            vertical.setAlignment(Pos.CENTER);
            vertical.setSpacing(5);
            vertical.getChildren().addAll(prjNameLabel, startLabel, deadLabel, pi);
            Button btn = new Button("", vertical);
            btn.setPrefHeight(150);
            btn.setPrefWidth(200);

            btn.getStyleClass().add("buttonProjects");
            btn.setId(String.valueOf(listaProiecte.get(i).getID()));


            btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    SenderText data = new SenderText();

                    data.setData(String.valueOf(btn.getId()));

                    JSONObject getLv = new JSONObject();
                    getLv.put("Type", "Get lvl priority");
                    getLv.put("IDproject", btn.getId());
                    getLv.put("IDuser", client.getCurrentUser().getID());

                    System.out.println(getLv.toString());
                    try {
                        client.sendText(getLv.toString());
                        String result = client.receiveText();
                        JSONObject receive = new JSONObject(result);

                        System.out.println(result);
                        if (receive.get("Result").equals("Level 1")){
                            root = FXMLLoader.load(getClass().getResource("/MiniPages/ViewProjectLevel1.fxml"));
                        }
                        else if(receive.get("Result").equals("Level 2")){
                            root = FXMLLoader.load(getClass().getResource("/MiniPages/ViewProjectLevel3.fxml"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Scene scene = new Scene(root);
                    Stage primaryStage = new Stage();

                    primaryStage.setTitle("Level 3 Priority");
                    primaryStage.setScene(scene);
                    primaryStage.initModality(Modality.APPLICATION_MODAL);

                    primaryStage.setResizable(false);
                    primaryStage.showAndWait();
                }
            });
            gp.add(btn, column, row);
            column++;
            if (column == 3) {
                row++;
                column = 0;
            }


        }

        gp.relocate(20, 20);
        gp.setGridLinesVisible(false);


        scrollPaneViewProjects.setPadding(new Insets(20, 20, 0, 20));
        scrollPaneViewProjects.setContent(gp);


    }

    ObservableList<ProjectModel> activeList = FXCollections.observableArrayList();
    ObservableList<ProjectModel> finishedList = FXCollections.observableArrayList();

}


