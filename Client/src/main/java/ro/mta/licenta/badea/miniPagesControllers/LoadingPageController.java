package ro.mta.licenta.badea.miniPagesControllers;

import animatefx.animation.Bounce;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Circle;
import javafx.util.Duration;


import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class LoadingPageController implements Initializable {

    @FXML
    private Circle circle1;

    @FXML
    private Circle circle2;

    @FXML
    private Circle circle3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        new Bounce(circle1).setCycleCount(14).setDelay(Duration.valueOf("500ms")).play();
        new Bounce(circle2).setCycleCount(14).setDelay(Duration.valueOf("1000ms")).play();
        new Bounce(circle3).setCycleCount(14).setDelay(Duration.valueOf("1500ms")).play();

    }
}
