package ro.mta.server.dao;

import ro.mta.server.Database;
import ro.mta.server.entities.TaskGeneral;

import java.sql.*;
import java.time.LocalDateTime;

public class TaskGeneralDAO implements ITaskGeneralDAO {

    public TaskGeneral taskGeneral;

    public TaskGeneralDAO() {
        taskGeneral = new TaskGeneral();
    }

    public TaskGeneralDAO(TaskGeneral task) {
        taskGeneral = task;
    }


    @Override
    public String addTaskGeneral(String name, String periodicity, int duration, LocalDateTime starttime, LocalDateTime deadline) {
        Database db = new Database();
        Connection con = db.getConn();

        String start = starttime.toString();
        String dead = deadline.toString();
        System.out.println("**" + start);
        String sql = "Insert into Taskuri_Generale(Denumire,Periodicitate,Durata,Starttime,Deadline)"
                + "Values ('" + name + "','" + periodicity + "'," + duration + ",'" + start + "','" + dead + "')";
        try {
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            System.out.println("Task General adaugat");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String addTaskGeneralBasedOnMember() {
        Database db = new Database();
        Connection con = db.getConn();

        String start = this.taskGeneral.getStarttime().toString();
        String dead = this.taskGeneral.getDeadline().toString();
        String name = this.taskGeneral.getName();
        String periodicity=this.taskGeneral.getPeriodicity();
        int duration = this.taskGeneral.getDuration();

        String sql = "Insert into Taskuri_Generale(Denumire,Periodicitate,Durata,Starttime,Deadline)"
                + "Values ('" + name + "','" + periodicity + "'," + duration + ",'" + start + "','" + dead + "')";
        try {
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            System.out.println("Task General adaugat");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getIDBasedOnName(String name, LocalDateTime start, LocalDateTime deadline) {
        Database db = new Database();
        Connection con = db.getConn();
        String sql = "Select ID from Taskuri_Generale " +
                "Where Denumire='" + name + "' AND Starttime='" + start.toString() + "' AND Deadline='" + deadline + "'";

        int id = 0;

        Statement stmt = null;
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(id);
        return id;

    }
}
