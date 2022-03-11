package ro.mta.server.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ro.mta.server.Database;
import ro.mta.server.entities.Project;
import ro.mta.server.entities.Resource;
import ro.mta.server.entities.Team;
import ro.mta.server.entities.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ProjectDAO {
    public Project project;

    public Project getProject() {
        return project;
    }
    public void setProject(Project project) {
        this.project = project;
    }

    public String getTeamsOfProject(int IDproject){
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String sqlTeam="Select * from Echipe E " +
                "inner join Proiecte P " +
                "on E.ID_Proiect=P.ID_Proiect " +
                "Where P.ID_Proiect="+IDproject;

        ArrayList<Team> listaEchipe=new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlTeam);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
               Team auxTeam=new Team();
               auxTeam.setID(rs.getInt(1));
               auxTeam.setName(rs.getString(2));
               listaEchipe.add(auxTeam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i=0;i<listaEchipe.size();i++){
            String sqlEachTeam="Select * from Echipe E " +
                    "inner join Echipe_Useri EU " +
                    "on E.ID_Echipa=EU.ID_Echipa " +
                    "inner join Useri U " +
                    "on U.ID_User=EU.ID_User " +
                    "Where E.ID_Echipa="+listaEchipe.get(i).getID();

            ArrayList<User> listaUseri=new ArrayList<>();
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sqlTeam);
                ResultSetMetaData meta = rs.getMetaData();
                while (rs.next()) {
                    User auxUser=new User();
                    auxUser.setID(rs.getInt(1));
                    auxUser.setUsername(rs.getString(2));
                    auxUser.setLastname(rs.getString(14));
                    auxUser.setFirstname(rs.getString(15));
                    listaUseri.add(auxUser);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /***AM RAMS AICI**/


        return null;
    }
}
