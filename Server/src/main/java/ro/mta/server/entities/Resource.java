package ro.mta.server.entities;

public class Resource {
    private int ID;
    private String denumire;
    private int cantitate;
    private boolean shareable;
    private String descriere;
    private int IDcompanie;

    public Resource(){}
    public Resource(int id,String nume,int quant,boolean s,String descrp, int IDc){
        this.ID=id;
        this.denumire=nume;
        this.cantitate=quant;
        this.shareable=s;
        this.descriere=descrp;
        this.IDcompanie=IDc;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public void setIDcompanie(int IDcompanie) {
        this.IDcompanie = IDcompanie;
    }

    public int getIDcompanie() {
        return IDcompanie;
    }

    public boolean isShareable() {
        return shareable;
    }

    public int getCantitate() {
        return cantitate;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setShareable(boolean shareable) {
        this.shareable = shareable;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

}
