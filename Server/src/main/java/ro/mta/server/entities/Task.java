package ro.mta.server.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Task {
    private int ID;
    private String name;
    private String periodicity;
    private int duration;
    private LocalDate starttime;
    private LocalDate deadline;
    private Task parinte;
    private int ID_Proiect;
    private ArrayList<Resource> listaResurse;
    private User executant;

    public void setExecutant(User executant) {
        this.executant = executant;
    }

    public User getExecutant() {
        return executant;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setStarttime(LocalDate starttime) {
        this.starttime = starttime;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public LocalDate getStarttime() {
        return starttime;
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



    public void setListaResurse(ArrayList<Resource> listaResurse) {
        this.listaResurse = listaResurse;
    }

    public int getID_Proiect() {
        return ID_Proiect;
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

    public void setID_Proiect(int ID_Proiect) {
        this.ID_Proiect = ID_Proiect;
    }

    public Task getTaskParinte() {
        return parinte;
    }

    public ArrayList<Resource> getListaResurse() {
        return listaResurse;
    }


    public void setTaskParinte(Task taskParinte) {
        this.parinte = taskParinte;
    }

}
