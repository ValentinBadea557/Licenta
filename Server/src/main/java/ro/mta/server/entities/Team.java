package ro.mta.server.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Team {
    private int ID;
    private String name;
    private LocalDateTime starttime;
    private LocalDateTime deadline;
    private ArrayList<User> listaUseri;
    private int ID_proiect;

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

    public LocalDateTime getStarttime() {
        return starttime;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setStarttime(LocalDateTime starttime) {
        this.starttime = starttime;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public ArrayList<User> getListaUseri() {
        return listaUseri;
    }

    public void setListaUseri(ArrayList<User> listaUseri) {
        this.listaUseri = listaUseri;
    }
}
