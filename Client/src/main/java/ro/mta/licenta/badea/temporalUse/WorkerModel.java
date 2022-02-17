package ro.mta.licenta.badea.temporalUse;

import javafx.scene.input.InputMethodEvent;

public class WorkerModel {
    private int ID;
    private String fullName;

    public WorkerModel(int id,String name){
        this.ID=id;
        this.fullName=name;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public int getID() {
        return ID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
