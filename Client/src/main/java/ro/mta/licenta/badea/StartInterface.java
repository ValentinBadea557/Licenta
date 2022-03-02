package ro.mta.licenta.badea;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;


public class StartInterface extends Application{
    private static Stage stg;

    public void launchApplication(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                JSONObject jsonexit=new JSONObject();
                jsonexit.put("Type","Exit");
                Client client=Client.getInstance();
                try {
                    client.sendText(jsonexit.toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });

        FXMLLoader loader = new FXMLLoader();

        try {

            loader.setLocation(this.getClass().getResource("/LoginPage.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Project Management Tool");
            primaryStage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}
