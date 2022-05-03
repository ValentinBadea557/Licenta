package ro.mta.licenta.badea.models;

import ro.mta.licenta.badea.temporalUse.WorkerModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TeamModel {
    private int ID;
    private String name;
    private LocalDate starttime;
    private LocalDate deadline;
    private int ID_proiect;
    private ArrayList<EmployeeModel> listaEmployees =new ArrayList<>();

    public void printTeamsInfo(){
        System.out.println("Nume echipa: "+this.name+"\nStarttime: "+starttime+"\nDeadline: "+deadline+"\n");
        System.out.println("Echipa contine urmatorii workeri:\n");
        for(int i=0;i<this.listaEmployees.size();i++){
            System.out.println(this.listaEmployees.get(i).getFirstname());
        }
    }

    public ArrayList<EmployeeModel> getListaEmployees() {
        return listaEmployees;
    }


    public void addEmployee(EmployeeModel emp){listaEmployees.add(emp);}

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setStarttime(LocalDate starttime) {
        this.starttime = starttime;
    }

    public int getID_proiect() {
        return ID_proiect;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public LocalDate getStarttime() {
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
