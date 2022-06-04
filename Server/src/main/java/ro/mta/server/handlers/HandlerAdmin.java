package ro.mta.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import ro.mta.server.GsonDateFormat.LocalDateTimeDeserializer;
import ro.mta.server.GsonDateFormat.LocalDateTimeSerializer;
import ro.mta.server.dao.ResourceDAO;
import ro.mta.server.dao.UserDAO;
import ro.mta.server.entities.Resource;
import ro.mta.server.entities.User;

import java.time.LocalDateTime;

public class HandlerAdmin implements IHandler {
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

        UserDAO userInstance = new UserDAO();

        JSONObject json = new JSONObject(this.messageReceived);
        String type = json.get("Type").toString();

        String returned;
        switch (type) {
            case "viewAllEmployees":

                returned = userInstance.AdministratorViewAllEmployees(json.getInt("IDadmin"));
                this.messageToSend = returned;
                break;
            case "addEmployeeToCompany":
                json.remove("Type");
                User user = gson.fromJson(json.toString(), User.class);
                String username = user.getUsername();
                String pass = user.getPassword();
                int admin = user.getAdmin();
                String firstName = user.getFirstname();
                String lastName = user.getLastname();
                String phone = user.getPhone();
                String addr = user.getAddr();
                String mail = user.getEmail();
                String companyName = user.getCompany().getNume();

                returned = userInstance.addOnlyUser(username, pass, companyName, lastName, firstName, addr, phone, mail, admin);
                this.messageToSend = returned;
                break;
            case "Add Resource":
                json.remove("Type");
                Resource res = gson.fromJson(json.toString(), Resource.class);
                String name = res.getDenumire();
                int cantitate = res.getCantitate();
                boolean shareable = res.isShareable();
                String descriere = res.getDescriere();
                int idCompanie = res.getIDcompanie();

                ResourceDAO resDao = new ResourceDAO(res);
                returned = resDao.addResource(name, cantitate, shareable, descriere, idCompanie);
                this.messageToSend = returned;
                break;

            case "ViewProjects all":
                int idCompany = json.getInt("IDcompany");
                returned = userInstance.viewProjectsAsAdmin(idCompany);
                this.messageToSend = returned;
                break;
            case "View Resources":
                ResourceDAO resourceView = new ResourceDAO();
                int id_company = json.getInt("ID_Companie");
                returned = resourceView.getListOfResourcesBasedOnCompanyID(id_company);
                this.messageToSend = returned;
                System.out.println(returned);
                break;
        }
    }
}
