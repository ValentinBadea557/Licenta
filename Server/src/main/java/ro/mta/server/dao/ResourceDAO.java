package ro.mta.server.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import ro.mta.server.Database;
import ro.mta.server.entities.Companie;
import ro.mta.server.entities.Resource;
import ro.mta.server.entities.Schedule;

import java.sql.*;

public class ResourceDAO implements IResourceDAO {
    public Resource resursa;

    public ResourceDAO() {
        resursa = new Resource();
    }

    public ResourceDAO(Resource rs) {
        resursa = rs;
    }


    @Override
    public String addResource(String nume, int cant, boolean sh, String descriere, int idCompanie) {
        JSONObject json = new JSONObject();

        Database db = new Database();
        Connection con = db.getConn();

        int bitValue;
        if (sh == true) {
            bitValue = 1;
        } else {
            bitValue = 0;
        }
        String sql = "Insert into Resurse "
                + "Values ('" + nume + "'," + cant + "," + bitValue + ",'" + descriere + "'," + idCompanie + ")";
        try {
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            json.put("Response", "ok");
        } catch (SQLException e) {
            e.printStackTrace();
            json.put("Response", "SQL Exception");
        }

        return json.toString();
    }

    @Override
    public String getListOfResourcesBasedOnCompanyID(int IDcompany) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String sql = "Select * from Resurse " +
                "Where ID_Companie=" + IDcompany;

        String returnString;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();

            Companie company = new Companie();
            while (rs.next()) {
                Resource resource = new Resource();
                resource.setID(rs.getInt(1));
                resource.setDenumire(rs.getString(2));
                resource.setCantitate(rs.getInt(3));
                resource.setShareable(rs.getBoolean(4));
                resource.setDescriere(rs.getString(5));
                resource.setIDcompanie(rs.getInt(6));

                company.addResource(resource);
                System.out.println(gson.toJson(resource));

            }

            returnString = gson.toJson(company);

        } catch (SQLException e) {
            e.printStackTrace();
            JSONObject errorJson = new JSONObject();
            errorJson.put("Response", "SQL Error!");
            returnString = errorJson.toString();
        }

        return returnString;
    }


    @Override
    public int getResourceIDbasedOnName(String name) {
        Database db = new Database();
        Connection con = db.getConn();
        int id = 0;
        String sql = "Select ID_Resursa from Resurse "
                + "where Denumire='" + name + "'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {               /**testez daca exista compania deja in baza de date   */
                id = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            id = -1;
        }
        System.out.println("ID Resursa : " + id);
        return id;
    }

    @Override
    public Resource getFullResourceBasedOnID(int idRes) {
        Database db = new Database();
        Connection con = db.getConn();
        String numeResursa = new String();
        String sql = "Select * from Resurse "
                + "where ID_Resursa=" + idRes;

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();

            Resource res = new Resource();
            while (rs.next()) {
                res.setID(rs.getInt(1));
                res.setDenumire(rs.getString(2));
                res.setCantitate(rs.getInt(3));
                res.setShareable(rs.getBoolean(4));
                res.setDescriere(rs.getString(5));

            }
            return res;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String modifyResourceForProject(int idProject, int idRes, int newQuantity) {
        Database db = new Database();
        Connection con = db.getConn();

        String sql = "update Resurse_Proiecte " +
                "set Cantitate=" + newQuantity + " " +
                "where ID_Proiect=" + idProject + " and ID_Resursa=" + idRes + " ;";

        JSONObject response = new JSONObject();
        try {
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            response.put("Result", "OK");

            Schedule sch = new Schedule();
            sch.setTheSchedulingForEntireProject(idProject);
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("Result", "Fail");
        }


        return response.toString();
    }

    @Override
    public String modifyResourceForTask(int idTaskReal, int idRes, int newQuantity) {
        Database db = new Database();
        Connection con = db.getConn();

        String sql = "Select * from Taskuri_Reale " +
                "where ID=" + idTaskReal;

        JSONObject response = new JSONObject();

        int idTask = 0;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();

            while (rs.next()) {
                idTask = rs.getInt(5);
                System.out.println("ID TASK : " + idTask);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql1 = "Update Resurse_Taskuri " +
                "Set Cantitate=" + newQuantity + " " +
                "Where ID_Task=" + idTask + " and ID_Resursa=" + idRes;

        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute(sql1);
            response.put("Result", "OK");
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("Result", "Fail");
        }

        return response.toString();
    }

    @Override
    public String modifyTaskDuration(int idTaskReal, int durationToDecrease) {
        Database db = new Database();
        Connection con = db.getConn();

        String sql = "Update Taskuri_Reale " +
                "Set Durata=Durata-" + durationToDecrease + " " +
                "Where ID=" + idTaskReal;
        JSONObject response = new JSONObject();
        try {
            Statement stmt = con.createStatement();
            stmt = con.createStatement();
            stmt.execute(sql);
            response.put("Result", "OK");
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("Result", "Fail");
        }

        return response.toString();
    }

    @Override
    public String getResourcebasedOnID(int id) {
        Database db = new Database();
        Connection con = db.getConn();
        String numeResursa = new String();
        String sql = "Select Denumire from Resurse "
                + "where ID_Resursa=" + id;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {               /**testez daca exista compania deja in baza de date   */
                numeResursa = rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Nume resursa: " + numeResursa);
        return numeResursa;
    }
}
