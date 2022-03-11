package ro.mta.server.handlers;

import com.google.gson.*;
import org.json.JSONObject;
import ro.mta.server.dao.ResourceDAO;
import ro.mta.server.dao.TaskGeneralDAO;
import ro.mta.server.dao.UserDAO;
import ro.mta.server.entities.TaskGeneral;
import ro.mta.server.entities.User;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HandleUser implements IHandler {
    private String messageReceived;
    private String messageToSend;

    public HandleUser() {
    }

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
        String type = json.get("Type").toString();

        System.out.println(type);


        UserDAO user = new UserDAO();
        String result;

        switch (type) {
            case "Login":

                result = user.login(json.get("Username").toString(), json.get("Password").toString());
                if (result.equals("Eroare")) {
                    JSONObject resultjson = new JSONObject();
                    resultjson.put("Response Login", "Error");
                    this.messageToSend = resultjson.toString();
                } else {
                    this.messageToSend = result;
                }
                break;
            case "Register":

                json.remove("Type");
                User newUser = new User();
                newUser = gson.fromJson(json.toString(), User.class);
                String username = newUser.getUsername();
                String pass = newUser.getPassword();
                String companyName = newUser.getCompany().getNume();
                String first = newUser.getFirstname();
                String last = newUser.getLastname();
                String mail = newUser.getEmail();
                String addr = newUser.getAddr();
                String phone = newUser.getPhone();

                result = user.addUserPlusCompany(username, pass, companyName, last, first, addr, phone, mail, 1);
                this.messageToSend = result;
                break;

            case "View Employees not Admins":
                int idCompany = json.getInt("ID_Companie");
                result = user.selectListOfEmployeesNotAdmins(idCompany);
                this.messageToSend = result;
                break;

            case "View Resources":
                ResourceDAO resourceView = new ResourceDAO();
                int id_company = json.getInt("ID_Companie");
                result = resourceView.getListOfResourcesBasedOnCompanyID(id_company);
                this.messageToSend = result;
                System.out.println(result);
                break;

            case "Create new Project":
                json.remove("Type");
                result = user.createNewProject(json.toString());
                this.messageToSend=result;
                break;

            case "ViewProjects":
                int id=json.getInt("IDuser");
                result=user.viewProjects(id);
                this.messageToSend=result;
                break;
        }
    }
}


class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss");

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(formatter.format(localDateTime));
    }
}

class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(),
                DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss").withLocale(Locale.ENGLISH));
    }
}
