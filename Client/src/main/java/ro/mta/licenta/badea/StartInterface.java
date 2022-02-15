package ro.mta.licenta.badea;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;


public class StartInterface extends Application{
    private static Stage stg;

    public void launchApplication(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
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
