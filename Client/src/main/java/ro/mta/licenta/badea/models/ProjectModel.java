package ro.mta.licenta.badea.models;

import ro.mta.licenta.badea.temporalUse.ProjectTemporalModel;

import java.time.LocalDate;
import java.util.ArrayList;

public class ProjectModel {
    private int ID;
    private EmployeeModel coordonator;
    private String nume;
    private String descriere;
    private LocalDate starttime;
    private LocalDate deadline;
    private ArrayList<TeamModel> listaEchipe = new ArrayList<>();
    private ArrayList<EmployeeModel> listaOameni = new ArrayList<>();
    private ArrayList<TaskModel> listaTaskuri = new ArrayList<>();
    private ArrayList<ResourceModel> listaResurseCurente;
    private ArrayList<TaskRealModel> listaTaskuriReale;
    private int finished;

    public ProjectModel(){}

    public ProjectModel(String nume, EmployeeModel coord, LocalDate start, LocalDate dead, String descriere){
        this.nume=nume;
        this.coordonator=coord;
        this.starttime=start;
        this.deadline=dead;
        this.descriere=descriere;
    }

    public void setListaResurseCurente(ArrayList<ResourceModel> listaResurseCurente) {
        this.listaResurseCurente = new ArrayList<>(listaResurseCurente);
    }

    public ArrayList<TaskRealModel> getListaTaskuriReale() {
        return listaTaskuriReale;
    }

    public void setListaTaskuriReale(ArrayList<TaskRealModel> listaTaskuriReale) {
        this.listaTaskuriReale = new ArrayList<>(listaTaskuriReale);
    }

    public ArrayList<ResourceModel> getListaResurseCurente() {
        return listaResurseCurente;
    }

    public void addNormalTask(TaskModel task){
        this.listaTaskuri.add(task);
    }


    public void setFinished(int finished) {
        this.finished = finished;
    }

    public String getCoordonatorFullName(){
        return this.coordonator.getFullName();
    }

    public int getFinished() {
        return finished;
    }

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

    public LocalDate getDeadline() {
        return deadline;
    }

    public LocalDate getStarttime() {
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


    @Override
    public String toString() {
        return "ProjectModel{" +
                "ID=" + ID +
                ", coordonator=" + coordonator +
                ", nume='" + nume + '\'' +
                ", descriere='" + descriere + '\'' +
                ", starttime=" + starttime +
                ", deadline=" + deadline +
                ", listaEchipe=" + listaEchipe +
                ", listaOameni=" + listaOameni +
                ", listaTaskuri=" + listaTaskuri +
                ", listaResurseCurente=" + listaResurseCurente +
                ", listaTaskuriReale=" + listaTaskuriReale +
                ", finished=" + finished +
                '}';
    }
}
