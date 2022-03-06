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
    private static String nume = new String();
    private static EmployeeModel coordonator;
    private static String descriere = new String();
    private static LocalDateTime starttime;
    private static LocalDateTime deadline;
    private static ArrayList<TeamModel> listaEchipe = new ArrayList<>();
    private static ArrayList<GeneralTaskModel> listaTaskuriGenerale = new ArrayList<>();
    private static ArrayList<TaskModel> listaTaskuri = new ArrayList<>();
    private static ArrayList<EmployeeModel> listaOameni = new ArrayList<>();

    public ProjectTemporalModel() {
    }

    public ProjectTemporalModel(String name, EmployeeModel coordonator, LocalDateTime start, LocalDateTime dead, String descriere) {
        this.nume = name;
        this.coordonator = coordonator;
        this.descriere = descriere;
        this.starttime = start;
        this.deadline = dead;

    }

    public void printData() {
        System.out.println("Name: " + this.nume);
        System.out.println("Starttime: " + this.starttime);
        System.out.println("Deadline: " + this.deadline);
        for (int i = 0; i < listaEchipe.size(); i++) {
            listaEchipe.get(i).printTeamsInfo();
        }
    }

    public void printEmployees(){
        for(int i=0;i<listaOameni.size();i++){
            System.out.println("** "+ listaOameni.get(i).getID()+" *\n");
        }
    }

    public void cleanListaEmployees(){
        listaOameni.clear();
    }

    public void addEmployee(EmployeeModel worker) {
        this.listaOameni.add(worker);
    }

    public static ArrayList<EmployeeModel> getListaOameni() {
        return listaOameni;
    }

    public static ArrayList<TaskModel> getListaTaskuri() {
        return listaTaskuri;
    }

    public static ArrayList<TeamModel> getListaEchipe() {
        return listaEchipe;
    }


    public void addGeneralTask(GeneralTaskModel genTask) {
        this.listaTaskuriGenerale.add(genTask);
    }

    public void addTask(TaskModel task) {
        this.listaTaskuri.add(task);
    }

    public void addTeam(TeamModel team) {
        this.listaEchipe.add(team);
    }

    public void setNumeProiect(String nume) {
        this.nume = nume;
    }

    public void setStarttime(LocalDateTime starttime) {
        this.starttime = starttime;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setDetails(String details) {
        this.descriere = details;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public LocalDateTime getStarttime() {
        return starttime;
    }

    public String getDetails() {
        return descriere;
    }

    public String getNumeProiect() {
        return nume;
    }

}
