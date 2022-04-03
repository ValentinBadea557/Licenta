package ro.mta.server.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ro.mta.server.Database;
import ro.mta.server.GsonDateFormat.LocalDateDeserializer;
import ro.mta.server.GsonDateFormat.LocalDateSerializer;
import ro.mta.server.GsonDateFormat.LocalDateTimeDeserializer;
import ro.mta.server.GsonDateFormat.LocalDateTimeSerializer;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TaskReal implements Comparable<TaskReal>{
    private int ID;
    private String name;
    private int duration;
    private LocalDate day;
    private Task originTask;
    private int parentID;
    private int startTime;
    private int completionTime;

    public HashMap<Integer, Integer> resourceUsage = new HashMap<Integer, Integer>(); //IDres / Quantity

    public int quantityOfResourceRequest(int idResource){
        return resourceUsage.get(idResource);
    }

    public void addIntoHashMap(int idRes,int quantity){
        this.resourceUsage.put(idRes,quantity);
    }

    public boolean checkIfTaskUseAResource(int idRes){
        if(resourceUsage.containsKey(idRes)){
            return true;
        }else{
            return false;
        }
    }


    public TaskReal setResourcesFromDB(int idTaskReal) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String sql = "Select TR.ID,TR.Nume,TR.Durata,TR.Zi,TR.ID_Task,TR.ID_Parinte,RT.ID_Resursa,RT.Cantitate from Taskuri_Reale TR\n" +
                "inner join Taskuri T\n" +
                "on TR.ID_Task=T.ID_Task\n" +
                "inner join Resurse_Taskuri RT\n" +
                "on RT.ID_Task=T.ID_Task\n" +
                "where TR.ID=" + idTaskReal + " " +
                "group by TR.ID,TR.Nume,TR.Durata,TR.Zi,TR.ID_Task,TR.ID_Parinte,RT.ID_Resursa,RT.Cantitate";

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

                task.setParentID(rs.getInt(6));

                task.addIntoHashMap(rs.getInt(7),rs.getInt(8));

            }

            return task;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public Task getOriginTask() {
        return originTask;
    }

    public void setOriginTask(Task originTask) {
        this.originTask = originTask;
    }


    @Override
    public int compareTo(TaskReal o) {

        return Integer.compare(getStartTime(),o.getStartTime());
    }
}
