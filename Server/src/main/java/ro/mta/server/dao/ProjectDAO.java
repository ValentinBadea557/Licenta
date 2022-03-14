package ro.mta.server.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import ro.mta.server.Database;
import ro.mta.server.entities.*;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ProjectDAO {
    public Project project;


    public String getProject(int IDproject) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
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

                Timestamp starttime = rs.getTimestamp(5);
                project.setStarttime(starttime.toLocalDateTime());

                Timestamp deadline = rs.getTimestamp(6);
                project.setDeadline(deadline.toLocalDateTime());

                project.setFinished(rs.getInt(7));

                project.setListaEchipe(getTeamsOfProject(IDproject));
                project.setListaOameni(getUsersOfProject(IDproject));
                project.setListaTaskuriGenerale(getTaskuriGenerale(IDproject));
                project.setListaTaskuri(getListaTaskuri(IDproject));

            }
            returned=gson.toJson(project);

        } catch (SQLException e) {
            JSONObject errorJson=new JSONObject();
            errorJson.put("Error","SQL Exception");
            returned=errorJson.toString();
            e.printStackTrace();
        }


        return returned;
    }

    public ArrayList<Task> getListaTaskuri(int idProject) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();


        String sql = "SELECT * from Taskuri " +
                "Where ID_Proiect=" + idProject;

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

                Timestamp starttime = rs.getTimestamp(5);
                task.setStarttime(starttime.toLocalDateTime());

                Timestamp deadline = rs.getTimestamp(6);
                task.setDeadline(deadline.toLocalDateTime());

                task.setID_Proiect(idProject);

                int id_gen = rs.getInt(8);
                int id_parent = rs.getInt(9);
                task.setTaskGeneral(getTaskGeneral(id_gen));

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

    public TaskGeneral getTaskGeneral(int idGeneral) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String sql = "select * from Taskuri_Generale\n" +
                "WHERE ID=" + idGeneral;

        TaskGeneral general = new TaskGeneral();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {

                general.setID(rs.getInt(1));
                general.setName(rs.getString(2));
                general.setPeriodicity(rs.getString(3));
                general.setDuration(rs.getInt(4));

                Timestamp starttime = rs.getTimestamp(5);
                general.setStarttime(starttime.toLocalDateTime());

                Timestamp deadline = rs.getTimestamp(6);
                general.setDeadline(deadline.toLocalDateTime());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return general;
    }

    public Task getTask(int idTask) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String sql = "Select * from Taskuri T " +
                "Where ID_Task=" + idTask;

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

                Timestamp starttime = rs.getTimestamp(5);
                task.setStarttime(starttime.toLocalDateTime());

                Timestamp deadline = rs.getTimestamp(6);
                task.setDeadline(deadline.toLocalDateTime());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return task;
    }

    public ArrayList<TaskGeneral> getTaskuriGenerale(int idProject) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String sqlGeneral = "select distinct TG.ID,TG.Denumire,TG.Periodicitate,TG.Durata,TG.Starttime,TG.Deadline from Taskuri_Generale TG\n" +
                "inner join Taskuri T\n" +
                "on T.ID_General=TG.ID\n" +
                "inner join Proiecte P\n" +
                "on P.ID_Proiect=T.ID_Proiect\n" +
                "where P.ID_Proiect=" + idProject;

        ArrayList<TaskGeneral> listaGenerale = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlGeneral);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                TaskGeneral general = new TaskGeneral();
                general.setID(rs.getInt(1));
                general.setName(rs.getString(2));
                general.setPeriodicity(rs.getString(3));
                general.setDuration(rs.getInt(4));

                Timestamp starttime = rs.getTimestamp(5);
                general.setStarttime(starttime.toLocalDateTime());

                Timestamp deadline = rs.getTimestamp(6);
                general.setDeadline(deadline.toLocalDateTime());

                listaGenerale.add(general);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaGenerale;
    }

    public ArrayList<User> getUsersOfProject(int idProject) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
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
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
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

                Timestamp starttime = rs.getTimestamp(3);
                auxTeam.setStarttime(starttime.toLocalDateTime());

                Timestamp deadline = rs.getTimestamp(4);
                auxTeam.setDeadline(deadline.toLocalDateTime());

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
