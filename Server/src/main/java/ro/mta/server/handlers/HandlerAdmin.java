package ro.mta.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import ro.mta.server.dao.UserDAO;

import java.time.LocalDateTime;

public class HandlerAdmin implements IHandler{
    private String messageReceived;
    private String messageToSend;

    public void setMessageToSend(String messageToSend) {
        this.messageToSend = messageToSend;
    }

    public void setMessageReceived(String messageReceived) {
        this.messageReceived = messageReceived;
    }

    public String getMessageToSend() {
        return messageToSend;
    }

    public String getMessageReceived() {
        return messageReceived;
    }

    @Override
    public void analyzeMessage() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        JSONObject json = new JSONObject(this.messageReceived);
        String type= json.get("type").toString();

        switch (type){
            case "viewAllEmployees":
                UserDAO userInstance=new UserDAO();
                userInstance.AdministratorViewAllEmployees(1);
        }
    }
}
