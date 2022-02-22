package ro.mta.server.entities;

import java.time.LocalDateTime;

public class TaskGeneral {
    private int ID;
    private String name;
    private String periodicity;
    private int duration;
    private LocalDateTime starttime;
    private LocalDateTime deadline;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getDuration() {
        return duration;
    }

    public String getPeriodicity() {
        return periodicity;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
    }

}
