package ro.mta.licenta.badea.models;

import ro.mta.licenta.badea.temporalUse.WorkerModel;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TeamModel {
    private int ID;
    private String name;
    private LocalDateTime starttime;
    private LocalDateTime deadline;
    private int ID_proiect;
    private ArrayList<WorkerModel> listaWorkers=new ArrayList<>();

    public void printTeamsInfo(){
        System.out.println("Nume echipa: "+this.name+"\nStarttime: "+starttime+"\nDeadline: "+deadline+"\n");
        System.out.println("Echipa contine urmatorii workeri:\n");
        for(int i=0;i<this.listaWorkers.size();i++){
            System.out.println(this.listaWorkers.get(i).getFullName());
        }
    }

    public void addWorker(WorkerModel work){
        this.listaWorkers.add(work);
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

    public int getID_proiect() {
        return ID_proiect;
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

    public void setID_proiect(int ID_proiect) {
        this.ID_proiect = ID_proiect;
    }

    public void setName(String name) {
        this.name = name;
    }

}
