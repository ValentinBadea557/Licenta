package ro.mta.licenta.badea;

import ro.mta.licenta.badea.StartInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class Main {

    public static void main(String[] args) throws Exception {

        Client client = Client.getInstance();
        StartInterface startInterface= new StartInterface();
        startInterface.launchApplication(args);
    }


}
