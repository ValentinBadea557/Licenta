package ro.mta.licenta.badea.miniPagesControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONObject;
import ro.mta.licenta.badea.Client;
import ro.mta.licenta.badea.models.ProjectModel;
import ro.mta.licenta.badea.temporalUse.SenderText;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class viewProjectLvl1Controller implements Initializable {

    private ProjectModel project;

    @FXML
    private TreeView teamsTreeView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SenderText data = new SenderText();
        int id = Integer.parseInt(data.getData());

        Client client = Client.getInstance();
        JSONObject tosend = new JSONObject();

        tosend.put("Type", "Get Project");
        tosend.put("IDproject",id);

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

//        try {
//            client.sendText(tosend.toString());
//            String response=client.receiveText();
//            this.project=gson.fromJson(response,ProjectModel.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        ImageView img=new ImageView("/Images/employees.png");
        img.setFitWidth(20);
        img.setFitHeight(20);
        TreeItem<String> rootItem = new TreeItem<>("Teams",img);

        TreeItem<String> FirstItem = new TreeItem<>("Team Soft");
        TreeItem<String> SecondItem = new TreeItem<>("Team Hard");
        TreeItem<String> ThirdItem = new TreeItem<>("Team Marketing");

        TreeItem<String> ch1 = new TreeItem<>("Matei");
        TreeItem<String> ch2 = new TreeItem<>("Cosmin");
        TreeItem<String> ch3 = new TreeItem<>("Andrei");

        FirstItem.getChildren().addAll(ch1, ch2);
        SecondItem.getChildren().addAll(ch3);
        rootItem.getChildren().addAll(FirstItem, SecondItem, ThirdItem);


        teamsTreeView.setRoot(rootItem);

    }
}
