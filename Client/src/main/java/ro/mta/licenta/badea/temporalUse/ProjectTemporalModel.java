package ro.mta.licenta.badea.temporalUse;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import ro.mta.licenta.badea.models.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectTemporalModel {
    private static String nume = new String();
    private static EmployeeModel coordonator;
    private static String descriere = new String();
    private static LocalDate starttime;
    private static LocalDate deadline;
    private static ArrayList<TeamModel> listaEchipe = new ArrayList<>();
    private static ArrayList<TaskModel> listaTaskuri = new ArrayList<>();
    private static ArrayList<EmployeeModel> listaOameni = new ArrayList<>();
    private static ArrayList<ResourceModel> listaResurse = new ArrayList<>();

    public void clearAllList() {

        if (!listaOameni.isEmpty())
            listaOameni.clear();
        if (!listaEchipe.isEmpty())
            listaEchipe.clear();

        if (!listaResurse.isEmpty()){
            listaResurse.clear();
            System.out.println("Curat lista resurse new project!\n");
            System.out.println(listaResurse.size());
        }

        if (!listaTaskuri.isEmpty())
            listaTaskuri.clear();

    }

    public static void setListaResurse(ArrayList<ResourceModel> listaResurse) {
        ProjectTemporalModel.listaResurse = new ArrayList<>(listaResurse);
    }

    public static ArrayList<ResourceModel> getListaResurse() {
        return listaResurse;
    }

    public ProjectTemporalModel() {
    }

    public ProjectTemporalModel(String name, EmployeeModel coordonator, LocalDate start, LocalDate dead, String descriere) {
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

    public void printEmployees() {
        for (int i = 0; i < listaOameni.size(); i++) {
            System.out.println("** " + listaOameni.get(i).getID() + " *\n");
        }
    }

    public void cleanListaEmployees() {
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

    public static void setListaOameni(ArrayList<EmployeeModel> listaOameni) {
        ProjectTemporalModel.listaOameni = new ArrayList<>(listaOameni);
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

    public void setStarttime(LocalDate starttime) {
        this.starttime = starttime;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setDetails(String details) {
        this.descriere = details;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public LocalDate getStarttime() {
        return starttime;
    }

    public String getDetails() {
        return descriere;
    }

    public String getNumeProiect() {
        return nume;
    }

}
