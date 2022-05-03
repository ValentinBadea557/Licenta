package ro.mta.server.entities;

import java.time.LocalDate;
import java.util.ArrayList;

public class Project {
    private int ID;
    private User coordonator;
    private String nume;
    private String descriere;
    private LocalDate starttime;
    private LocalDate deadline;
    private ArrayList<Team> listaEchipe;
    private ArrayList<User> listaOameni;
    private ArrayList<Task> listaTaskuri;
    private ArrayList<Resource> listaResurseCurente;
    private ArrayList<Task> listaTaskuriReale;
    private int finished;

    public void setListaTaskuriReale(ArrayList<Task> listaTaskuriReale) {
        this.listaTaskuriReale = new ArrayList<>(listaTaskuriReale);
    }

    public ArrayList<Task> getListaTaskuriReale() {
        return listaTaskuriReale;
    }

    public ArrayList<Resource> getListaResurseCurente() {
        return listaResurseCurente;
    }

    public void setListaResurseCurente(ArrayList<Resource> listaResurseCurente) {
        this.listaResurseCurente = new ArrayList<>(listaResurseCurente);
    }


    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public LocalDate getStarttime() {
        return starttime;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setStarttime(LocalDate starttime) {
        this.starttime = starttime;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setListaTaskuri(ArrayList<Task> listaTaskuri) {
        this.listaTaskuri = new ArrayList<>(listaTaskuri);
    }

    public void setListaOameni(ArrayList<User> listaOameni) {
        this.listaOameni = new ArrayList<>(listaOameni);
    }

    public void setListaEchipe(ArrayList<Team> listaEchipe) {
        this.listaEchipe = new ArrayList<>(listaEchipe);
    }

    public void setCoordonator(User coordonator) {
        this.coordonator = coordonator;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getDescriere() {
        return descriere;
    }

    public ArrayList<Task> getListaTaskuri() {
        return listaTaskuri;
    }

    public ArrayList<Team> getListaEchipe() {
        return listaEchipe;
    }

    public ArrayList<User> getListaOameni() {
        return listaOameni;
    }

    public User getCoordonator() {
        return coordonator;
    }

}
