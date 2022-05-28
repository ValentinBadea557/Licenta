package ro.mta.server.handlers;

import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONObject;
import ro.mta.server.GsonDateFormat.LocalDateTimeDeserializer;
import ro.mta.server.GsonDateFormat.LocalDateTimeSerializer;
import ro.mta.server.dao.ProjectDAO;
import ro.mta.server.dao.ResourceDAO;
import ro.mta.server.dao.UserDAO;
import ro.mta.server.entities.Resource;
import ro.mta.server.entities.Schedule;
import ro.mta.server.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
        ProjectDAO proiect = new ProjectDAO();
        ResourceDAO rs = new ResourceDAO();
        Schedule sch = new Schedule();
        String result = new String();

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
                this.messageToSend = result;
                break;

            case "ViewProjects":
                int id = json.getInt("IDuser");
                result = user.viewProjects(id);
                this.messageToSend = result;
                break;

            case "Get Project":
                int id_project = json.getInt("IDproject");
                result = proiect.getProject(id_project);
                this.messageToSend = result;
                break;

            case "Add new task to project":

                json.remove("Type");
                result = user.addNewTaskToProject(json.toString());
                this.messageToSend = result;
                break;

            case "Resource new quantity":
                json.remove("Type");
                int idProject = json.getInt("IDproject");
                int idRes = json.getInt("IDresource");
                int newQuantity = json.getInt("Quantity");

                System.out.println(idProject + " " + idRes + " " + newQuantity);

                result = rs.modifyResourceForProject(idProject, idRes, newQuantity);
                this.messageToSend = result;
                break;

            case "Request Recommendations":
                System.out.println("Recomandare");
                int idTaskReal = json.getInt("idTaskReal");
                int idProjectRecomandation = json.getInt("idProject");

                result = sch.taskRecommandations(idTaskReal, idProjectRecomandation);
                this.messageToSend = result;
                break;

            case "Modify Resources":
                System.out.println("Modify Resources");
                JSONArray array = json.getJSONArray("Modificari");
                int idPrj = json.getInt("IDproject");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    int idtask = obj.getInt("IDtask");
                    int newQuantityTask = obj.getInt("MaximQuantity");
                    int idResursaTask = obj.getInt("IDresursa");
                    result = rs.modifyResourceForTask(idtask, idResursaTask, newQuantityTask);
                }
                sch.setTheSchedulingForEntireProject(idPrj);

                this.messageToSend = result;
                break;

            case "Decrease Duration":
                int idtask = json.getInt("IDtask");
                int durationToDecrease = json.getInt("ValueToDecrease");
                result = rs.modifyTaskDuration(idtask, durationToDecrease);
                sch.setTheSchedulingForEntireProject(json.getInt("IDproject"));
                this.messageToSend = result;
                break;
        }
    }
}


