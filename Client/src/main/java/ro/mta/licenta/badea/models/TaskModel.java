package ro.mta.licenta.badea.models;

import javafx.concurrent.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Collections.copy;

public class TaskModel {
    private int ID;
    private String name;
    private String periodicity;
    private int duration;
    private LocalDateTime starttime;
    private LocalDateTime deadline;
    private GeneralTaskModel taskGeneral;
    private TaskModel parinte;
    private int ID_Proiect;
    private ArrayList<ResourceModel> listaResurse;

    public void printTaskInformation(){
        System.out.println("Name: "+name+"\n Periodicity: "+periodicity+"\n Duration: "+duration+"\n Starttime: "+starttime+"\n Deadline: "+deadline);
        System.out.println("Task General: "+taskGeneral.getName());
        System.out.println("\nLista resurse:\n");
        for(int i=0;i<listaResurse.size();i++){
            System.out.println("Nume resursa: "+listaResurse.get(i).getDenumire()+" Cantitate:"+listaResurse.get(i).getCantitate()+"\n");
        }

    }

    public void printResourceListSize(){
        System.out.println(this.listaResurse.size());
    }

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

    public void setID_Proiect(int ID_Proiect) {
        this.ID_Proiect = ID_Proiect;
    }

    public int getID_Proiect() {
        return ID_Proiect;
    }

    public GeneralTaskModel getTaskGeneral() {
        return taskGeneral;
    }

    public TaskModel getParinte() {
        return parinte;
    }

    public ArrayList<ResourceModel> getListaResurse() {
        return listaResurse;
    }

    public void setListaResurse(ArrayList<ResourceModel> listaResurse) {
        this.listaResurse=new ArrayList<>(listaResurse);
    }

    public void setParinte(TaskModel parinte) {
        this.parinte = parinte;
    }

    public void setTaskGeneral(GeneralTaskModel taskGeneral) {
        this.taskGeneral = taskGeneral;
    }
}
