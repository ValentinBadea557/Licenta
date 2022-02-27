package ro.mta.server.handlers;

import com.google.gson.*;
import org.json.JSONObject;
import ro.mta.server.dao.TaskGeneralDAO;
import ro.mta.server.dao.UserDAO;
import ro.mta.server.entities.TaskGeneral;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HandleUser implements IHandler{
    private String messageReceived;
    private String messageToSend;

    public String getMessageReceived() {
        return messageReceived;
    }

    public String getMessageToSend() {
        return messageToSend;
    }

    public void setMessageReceived(String messageReceived) {
        this.messageReceived = messageReceived;
    }

    public void setMessageToSend(String messageToSend) {
        this.messageToSend = messageToSend;
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
            case "Login":
                UserDAO user=new UserDAO();
                String result= user.login(json.get("username").toString(),json.get("password").toString());
                System.out.println(result);
                this.messageToSend=result;
                break;
            case "AddTaskGeneral":
                String test= "{\n  \"ID\": 0,\n  \"name\": \"fsda\",\n  \"periodicity\": \"Daily\",\n  \"duration\": 1,\n  \"starttime\": \"24::Feb::2022 02::30::01\",\n  \"deadline\": \"22::Feb::2022 02::30::01\"\n}";

                TaskGeneral generalObject=gson.fromJson(test, TaskGeneral.class);
                TaskGeneralDAO general=new TaskGeneralDAO(generalObject);
                general.addTaskGeneralBasedOnMember();
                break;


        }
    }
}


class LocalDateTimeSerializer implements JsonSerializer< LocalDateTime > {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss");

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(formatter.format(localDateTime));
    }
}

class LocalDateTimeDeserializer implements JsonDeserializer< LocalDateTime > {
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(),
                DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss").withLocale(Locale.ENGLISH));
    }
}
