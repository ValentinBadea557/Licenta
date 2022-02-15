package ro.mta.licenta.badea.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ResourceModel {
    private SimpleIntegerProperty id;
    private SimpleStringProperty denumire;
    private SimpleIntegerProperty cantitate;
    private SimpleBooleanProperty shareable;
    private SimpleStringProperty descriere;

    public ResourceModel(int id,String name,int quantity,boolean sh,String description){
        this.id=new SimpleIntegerProperty(id);
        this.denumire=new SimpleStringProperty(name);
        this.cantitate=new SimpleIntegerProperty(id);
        this.shareable=new SimpleBooleanProperty(sh);
        this.descriere=new SimpleStringProperty(description);
    }

    public void setShareable(boolean shareable) {
        this.shareable.set(shareable);
    }

    public void setID(int ID) {
        this.id.set(ID);
    }

    public void setDenumire(String denumire) {
        this.denumire.set(denumire);
    }

    public void setCantitate(int cantitate) {
        this.cantitate.set(cantitate);
    }

    public void setDescriere(String descriere) {
        this.descriere.set(descriere);
    }

    public int getID() {
        return id.get();
    }

    public SimpleBooleanProperty shareableProperty() {
        return shareable;
    }

    public SimpleIntegerProperty cantitateProperty() {
        return cantitate;
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleStringProperty denumireProperty() {
        return denumire;
    }

    public SimpleStringProperty descriereProperty() {
        return descriere;
    }
}
