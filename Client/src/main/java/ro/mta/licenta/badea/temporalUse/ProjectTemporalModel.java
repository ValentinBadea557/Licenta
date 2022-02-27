package ro.mta.licenta.badea.temporalUse;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import ro.mta.licenta.badea.models.EmployeeModel;
import ro.mta.licenta.badea.models.GeneralTaskModel;
import ro.mta.licenta.badea.models.TaskModel;
import ro.mta.licenta.badea.models.TeamModel;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectTemporalModel {
    private static String numeProiect = new String();
    private static EmployeeModel coordonator;
    private static String descriere = new String();
    private static LocalDateTime starttime;
    private static LocalDateTime deadline;
    private static String details = new String();
    private static ArrayList<TeamModel> listTeams = new ArrayList<>();
    private static ArrayList<GeneralTaskModel> listaTaskuriGenerale = new ArrayList<>();
    private static ArrayList<TaskModel> listaTaskuri = new ArrayList<>();
    private static ArrayList<EmployeeModel> listaUseri = new ArrayList<>();

    public ProjectTemporalModel() {
    }

    public ProjectTemporalModel(String name, EmployeeModel coordonator, LocalDateTime start, LocalDateTime dead, String descriere) {
        this.numeProiect = name;
        this.coordonator = coordonator;
        this.details = descriere;
        this.starttime = start;
        this.deadline = dead;

    }

    public void printData() {
        System.out.println("Name: " + this.numeProiect);
        System.out.println("Starttime: " + this.starttime);
        System.out.println("Deadline: " + this.deadline);
        for (int i = 0; i < listTeams.size(); i++) {
            listTeams.get(i).printTeamsInfo();
        }

    }

    public void addEmployee(EmployeeModel worker) {
        this.listaUseri.add(worker);
    }

    public void addGeneralTask(GeneralTaskModel genTask) {
        this.listaTaskuriGenerale.add(genTask);
    }

    public void addTask(TaskModel task) {
        this.listaTaskuri.add(task);
    }

    public void addTeam(TeamModel team) {
        this.listTeams.add(team);
    }

    public void setNumeProiect(String nume) {
        this.numeProiect = nume;
    }

    public void setStarttime(LocalDateTime starttime) {
        this.starttime = starttime;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public String getNumeProiect() {
        return numeProiect;
    }

}
