package ro.mta.licenta.badea.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class ProjectModel {
    private String numeProiect;
    private String leader;
    private LocalDateTime starttime;
    private LocalDateTime deadline;
    private String details;

    public ProjectModel(String name, String coordonator, LocalDateTime start,LocalDateTime dead,String descriere){
        this.numeProiect=name;
        this.leader=coordonator;
        this.details=descriere;
        this.starttime= start;
        this.deadline=dead;

    }


    public void setNumeProiect(String nume) {
        this.numeProiect=nume;
    }

    public void setStarttime(LocalDateTime starttime) {
        this.starttime = starttime;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setDetails(String details) {
        this.details=details;
    }

    public void setLeader(String leader) {
        this.leader=leader;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public LocalDateTime getStarttime() {
        return starttime;
    }

    public String getDetails() {
        return details;
    }

    public String getLeader() {
        return leader;
    }

    public String getNumeProiect() {
        return numeProiect;
    }

}
