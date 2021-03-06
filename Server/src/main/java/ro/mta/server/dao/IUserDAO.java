package ro.mta.server.dao;

import ro.mta.server.entities.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * interfata pentru user dao
 */
public interface IUserDAO {
    //toate functiile pe care user le va folosi

    public int getUserIDbasedOnUsername(String username);
    public int checkUsernameUnicity(String username);
    public int addAdminbasedOnUserID(int userID);
    public String login(String user,String password);
    public String hashFunction(String pass);
    public int checkIfUserIsAdmin(int IDuser);
    public Companie getCompanybasedOnID(int IDcompany);
    public String getListOfActiveProjects(int IDuser);
    public User getUserbasedOnID(int idUser);
    public String selectListOfEmployeesNotAdmins(int ID_company);
    public String createNewProject(String jsonProject);
    public String addNewTaskToProject(String jsonTask);
    public int createRealTasks(Task task) throws SQLException;
    public int createRoleOrGetRoleID(String role);
    public int getIDofNormalTask(Task task);
    public String viewProjects(int idUser);
    public String getEveryInfoAboutProject(int idProject);
    public String getListOfPeopleAssignedToProject(int idCompanie, LocalDate start,LocalDate deadline);
    public int deleteAllDataAboutAProject(int idProject);
    public String getFutureTasks(int idUser);
    public String updateInfo(int idUser,String addr,String phone,String email);
    public String getLvlOfPriority(int idUser,int idProject);

    /**Functions used by admins*/
    public String addUserPlusCompany(String username,String parola,String nume_companie,String Nume,String Prenume,String Adrs,String Telefon,String mail, int admin);
    public String addOnlyUser(String username,String parola,String nume_companie,String Nume,String Prenume,String Adrs,String Telefon,String mail, int admin);
    public String AdministratorViewAllEmployees(int IDuseradmin);
    public String viewProjectsAsAdmin(int idCompany);

}
