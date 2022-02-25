package ro.mta.licenta.badea.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ProjectModel {
    private int ID;
    private EmployeeModel coordonator;
    private String nume;
    private String descriere;
    private LocalDateTime starttime;
    private LocalDateTime deadline;
    private ArrayList<TeamModel> listaEchipe;
    private ArrayList<EmployeeModel> listaOameni;
    private ArrayList<GeneralTaskModel> listaTaskuriGenerale;
    private ArrayList<TaskModel> listaTaskuri;

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

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getNume() {
        return nume;
    }

    public ArrayList<EmployeeModel> getListaOameni() {
        return listaOameni;
    }

    public ArrayList<GeneralTaskModel> getListaTaskuriGenerale() {
        return listaTaskuriGenerale;
    }

    public ArrayList<TaskModel> getListaTaskuri() {
        return listaTaskuri;
    }

    public ArrayList<TeamModel> getListaEchipe() {
        return listaEchipe;
    }

    public EmployeeModel getCoordonator() {
        return coordonator;
    }

    public void setCoordonator(EmployeeModel coordonator) {
        this.coordonator = coordonator;
    }

    public void setListaEchipe(ArrayList<TeamModel> listaEchipe) {
        this.listaEchipe =new ArrayList<>(listaEchipe);
    }

    public void setListaOameni(ArrayList<EmployeeModel> listaOameni) {
        this.listaOameni = new ArrayList<>(listaOameni);
    }

    public void setListaTaskuri(ArrayList<TaskModel> listaTaskuri) {
        this.listaTaskuri = new ArrayList<>(listaTaskuri);
    }

    public void setListaTaskuriGenerale(ArrayList<GeneralTaskModel> listaTaskuriGenerale) {
        this.listaTaskuriGenerale = new ArrayList<>(listaTaskuriGenerale);
    }
}
