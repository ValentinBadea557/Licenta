package ro.mta.licenta.badea.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ResourceModel implements Cloneable{
    private int ID;
    private String denumire;
    private int cantitate;
    private boolean shareable;
    private String descriere;
    private int IDcompanie;

    public ResourceModel(){}

    public ResourceModel(int id,String name,int quantity,boolean sh,String description){
        this.ID=id;
        this.denumire=name;
        this.cantitate=quantity;
        this.shareable=sh;
        this.descriere=description;
    }

    public int getIDcompanie() {
        return IDcompanie;
    }

    public void setIDcompanie(int IDcompanie) {
        this.IDcompanie = IDcompanie;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public void setShareable(boolean shareable) {
        this.shareable = shareable;
    }

    public String getDescriere() {
        return descriere;
    }

    public String getDenumire() {
        return denumire;
    }

    public int getCantitate() {
        return cantitate;
    }

    public boolean isShareable() {
        return shareable;
    }

    public int getId() {
        return ID;
    }

    public void setId(int id) {
        this.ID = id;
    }
}
