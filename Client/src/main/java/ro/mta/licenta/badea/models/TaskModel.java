package ro.mta.licenta.badea.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskModel {
    private int ID;
    private String name;
    private String periodicity;
    private int duration;
    private LocalDateTime starttime;
    private LocalDateTime deadline;
    private int ID_general;
    private int ID_parinte;
    private int ID_Proiect;
    private ArrayList<ResourceModel> listaResurse;

    public void addResourseToList(ResourceModel resource){
        this.listaResurse.add(resource);
    }

    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPeriodicity() {
        return periodicity;
    }

    public int getDuration() {
        return duration;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setStarttime(LocalDateTime starttime) {
        this.starttime = starttime;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public LocalDateTime getStarttime() {
        return starttime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID_general() {
        return ID_general;
    }

    public int getID_parinte() {
        return ID_parinte;
    }

    public int getID_Proiect() {
        return ID_Proiect;
    }

    public void setID_general(int ID_general) {
        this.ID_general = ID_general;
    }

    public void setID_parinte(int ID_parinte) {
        this.ID_parinte = ID_parinte;
    }

    public void setID_Proiect(int ID_Proiect) {
        this.ID_Proiect = ID_Proiect;
    }
}
