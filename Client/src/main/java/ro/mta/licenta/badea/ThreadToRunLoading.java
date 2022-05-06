package ro.mta.licenta.badea;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ThreadToRunLoading extends Thread {
    private Scene scene;
    private Parent root;
    private  Stage primaryStage;

    public void run() {
        System.out.println("MyThread running");

        root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/MiniPages/LoadingPage.fxml"));

            scene = new Scene(root);
            primaryStage = new Stage();

            primaryStage.initStyle(StageStyle.UNDECORATED);

            primaryStage.setScene(scene);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
