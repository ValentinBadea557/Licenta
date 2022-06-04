package ro.mta.server.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import ro.mta.server.Database;
import ro.mta.server.entities.*;
import ro.mta.server.GsonDateFormat.*;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ProjectDAO {
    public Project project;

    public String getProject(int IDproject) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String sql = "Select * from Proiecte " +
                "Where ID_Proiect=" + IDproject;


        String returned = null;
        Project project = new Project();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                UserDAO usr = new UserDAO();
                project.setID(rs.getInt(1));
                project.setCoordonator(usr.getUserbasedOnID(rs.getInt(2)));
                project.setNume(rs.getString(3));
                project.setDescriere(rs.getString(4));

                //    Timestamp starttime = rs.getTimestamp(5);
                project.setStarttime(rs.getDate(5).toLocalDate());

                //   Timestamp deadline = rs.getTimestamp(6);
                project.setDeadline(rs.getDate(6).toLocalDate());

                project.setFinished(rs.getInt(7));

                project.setListaEchipe(getTeamsOfProject(IDproject));
                project.setListaOameni(getUsersOfProject(IDproject));
                project.setListaTaskuri(getListaTaskuri(IDproject));
                project.setListaResurseCurente(getListaResurseFolosite(IDproject));
                project.setListaTaskuriReale(getRealTasks(IDproject));
            }
            returned = gson.toJson(project);

        } catch (SQLException e) {
            JSONObject errorJson = new JSONObject();
            errorJson.put("Error", "SQL Exception");
            returned = errorJson.toString();
            e.printStackTrace();
        }

        System.out.println(returned);
        return returned;
    }


    public ArrayList<TaskReal> getRealTasks(int IDproject) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String sql = "SELECT * from Taskuri_Reale TR " +
                "inner join Taskuri T " +
                "on TR.ID_Task=T.ID_Task " +
                "Where T.ID_Proiect=" + IDproject;

        ArrayList<TaskReal> localList = new ArrayList<>();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();


            while (rs.next()) {
                TaskReal task = new TaskReal();

                task.setID(rs.getInt(1));
                task = task.setResourcesFromDB(task.getID());
                task.setName(rs.getString(2));
                task.setDuration(rs.getInt(3));

                Timestamp day = rs.getTimestamp(4);
                task.setDay(day.toLocalDateTime().toLocalDate());


                task.setOriginTask(getTask(rs.getInt(5)));
                task.setParentID(rs.getInt(6));

                task.setStartTime(rs.getInt(7));
                if (rs.wasNull()) {
                    task.setStartTime(-1);
                }

                localList.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(gson.toJson(localList));
        return localList;
    }

    public TaskReal getTaskRealBasedOnID(int idTaskReal) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String sql = "SELECT * from Taskuri_Reale TR " +
                "Where TR.ID=" + idTaskReal;

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();

            TaskReal task = new TaskReal();
            while (rs.next()) {


                task.setID(rs.getInt(1));
                task.setName(rs.getString(2));
                task.setDuration(rs.getInt(3));

                Timestamp day = rs.getTimestamp(4);
                task.setDay(day.toLocalDateTime().toLocalDate());

                int idOrigin = rs.getInt(5);

                task.setOriginTask(getTask(rs.getInt(6)));
                task.setStartTime(rs.getInt(7));
            }

            return task;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public ArrayList<Resource> getListaResurseFolosite(int IDproject) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String sql = "select  R.ID_Resursa, R.Denumire, R.Shareable , R.Descriere, RP.Cantitate from Resurse R\n" +
                "inner join Resurse_Proiecte RP\n" +
                "on RP.ID_Resursa=R.ID_Resursa\n" +
                "inner join Proiecte P\n" +
                "on P.ID_Proiect=RP.ID_Proiect\n" +
                "Where P.ID_Proiect="+IDproject;

        ArrayList<Resource> listaResurse = new ArrayList<>();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();

            while (rs.next()) {

                Resource resursa = new Resource();
                resursa.setID(rs.getInt(1));
                resursa.setDenumire(rs.getString(2));
                resursa.setShareable(rs.getBoolean(3));
                resursa.setDescriere(rs.getString(4));
                resursa.setCantitate(rs.getInt(5));
                listaResurse.add(resursa);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaResurse;
    }

    public ArrayList<Task> getListaTaskuri(int idProject) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();


        String sql = "SELECT * from Taskuri T " +
                "inner join Taskuri_Useri TU " +
                "on T.ID_Task=TU.ID_Task " +
                "Where T.ID_Proiect=" + idProject;

        ArrayList<Task> listaTaskuri = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();


            while (rs.next()) {
                Task task = new Task();
                task.setID(rs.getInt(1));
                task.setName(rs.getString(2));
                task.setPeriodicity(rs.getString(3));
                task.setDuration(rs.getInt(4));

                // Timestamp starttime = rs.getTimestamp(5);
                task.setStarttime(rs.getDate(5).toLocalDate());

                //  Timestamp deadline = rs.getTimestamp(6);
                task.setDeadline(rs.getDate(6).toLocalDate());

                task.setID_Proiect(idProject);

                int id_parent = rs.getInt(8);


                UserDAO usr = new UserDAO();
                task.setExecutant(usr.getUserbasedOnID(rs.getInt(10)));

                if (id_parent == 0) {
                    task.setTaskParinte(null);
                } else {
                    task.setTaskParinte(getTask(id_parent));
                }


                listaTaskuri.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaTaskuri;
    }


    public Task getTask(int idTask) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String sql = "select * from Taskuri T\n" +
                "inner join Taskuri_Useri TU\n" +
                "on T.ID_Task=TU.ID_Task \n" +
                "Where T.ID_Task=" + idTask;

        Task task = new Task();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {

                task.setID(rs.getInt(1));
                task.setName(rs.getString(2));
                task.setPeriodicity(rs.getString(3));
                task.setDuration(rs.getInt(4));

                //   Timestamp starttime = rs.getTimestamp(5);
                task.setStarttime(rs.getDate(5).toLocalDate());

                // Timestamp deadline = rs.getTimestamp(6);
                task.setDeadline(rs.getDate(6).toLocalDate());

                int idUser = rs.getInt(10);
                UserDAO usr = new UserDAO();
                task.setExecutant(usr.getUserbasedOnID(idUser));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return task;
    }


    public ArrayList<User> getUsersOfProject(int idProject) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String sqlUseri = "select U.ID_User,Username,DP.Nume,DP.Prenume,DP.Adresa,DP.Email,DP.Telefon,Per.Nivel,RO.Denumire from Useri U\n" +
                "inner join Date_Personale DP\n" +
                "on U.ID_User=DP.ID_User\n" +
                "inner join Proiecte_Useri PU\n" +
                "on PU.ID_USER=U.ID_User\n" +
                "inner join Permisiuni Per\n" +
                "on Per.ID_Permisiune=PU.ID_Permisiune\n" +
                "inner join Roluri RO\n" +
                "on RO.ID_Rol=PU.ID_Rol\n" +
                "where  PU.ID_Proiect=" + idProject;

        ArrayList<User> listaUseri = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlUseri);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                User auxUser = new User();
                auxUser.setID(rs.getInt(1));
                auxUser.setUsername(rs.getString(2));
                auxUser.setFirstname(rs.getString(3));
                auxUser.setLastname(rs.getString(4));
                auxUser.setAddr(rs.getString(5));
                auxUser.setEmail(rs.getString(6));
                auxUser.setPhone(rs.getString(7));
                auxUser.setPermission(rs.getString(8));
                auxUser.setRole(rs.getString(9));
                listaUseri.add(auxUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaUseri;
    }

    public ArrayList<Team> getTeamsOfProject(int IDproject) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String sqlTeam = "Select * from Echipe E " +
                "inner join Proiecte P " +
                "on E.ID_Proiect=P.ID_Proiect " +
                "Where P.ID_Proiect=" + IDproject;

        ArrayList<Team> listaEchipe = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlTeam);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                Team auxTeam = new Team();
                auxTeam.setID(rs.getInt(1));
                auxTeam.setName(rs.getString(2));

                // Timestamp starttime = rs.getTimestamp(3);
                auxTeam.setStarttime(rs.getDate(3).toLocalDate());

                //Timestamp deadline = rs.getTimestamp(4);
                auxTeam.setDeadline(rs.getDate(4).toLocalDate());

                auxTeam.setID_proiect(IDproject);
                listaEchipe.add(auxTeam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < listaEchipe.size(); i++) {
            String sqlEachTeam = "select U.ID_User,Username,DP.Nume,DP.Prenume,DP.Adresa,DP.Email,DP.Telefon,Per.Nivel,RO.Denumire from Useri U\n" +
                    "inner join Date_Personale DP\n" +
                    "on U.ID_User=DP.ID_User\n" +
                    "inner join Echipe_Useri EU\n" +
                    "on EU.ID_User=U.ID_User\n" +
                    "inner join Proiecte_Useri PU\n" +
                    "on PU.ID_USER=U.ID_User\n" +
                    "inner join Permisiuni Per\n" +
                    "on Per.ID_Permisiune=PU.ID_Permisiune\n" +
                    "inner join Roluri RO\n" +
                    "on RO.ID_Rol=PU.ID_Rol\n" +
                    "where EU.ID_Echipa=" + listaEchipe.get(i).getID() + " and PU.ID_Proiect=" + IDproject;

            ArrayList<User> listaUseri = new ArrayList<>();
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sqlEachTeam);
                ResultSetMetaData meta = rs.getMetaData();
                while (rs.next()) {
                    User auxUser = new User();
                    auxUser.setID(rs.getInt(1));
                    auxUser.setUsername(rs.getString(2));
                    auxUser.setFirstname(rs.getString(3));
                    auxUser.setLastname(rs.getString(4));
                    auxUser.setAddr(rs.getString(5));
                    auxUser.setEmail(rs.getString(6));
                    auxUser.setPhone(rs.getString(7));
                    auxUser.setPermission(rs.getString(8));
                    auxUser.setRole(rs.getString(9));
                    listaUseri.add(auxUser);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            listaEchipe.get(i).setListaUseri(listaUseri);
        }


        return listaEchipe;
    }
}
