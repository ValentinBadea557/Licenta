package ro.mta.server.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Team {
    private int ID;
    private String name;
    private LocalDate starttime;
    private LocalDate deadline;
    private ArrayList<User> listaEmployees;
    private int ID_proiect;

    public void setID_proiect(int ID_proiect) {
        this.ID_proiect = ID_proiect;
    }

    public int getID_proiect() {
        return ID_proiect;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public LocalDate getStarttime() {
        return starttime;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setStarttime(LocalDate starttime) {
        this.starttime = starttime;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public ArrayList<User> getListaUseri() {
        return this.listaEmployees;
    }

    public void setListaUseri(ArrayList<User> listaUseri) {
        this.listaEmployees = listaUseri;
    }
}
