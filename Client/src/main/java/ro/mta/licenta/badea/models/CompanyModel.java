package ro.mta.licenta.badea.models;

import java.util.ArrayList;

public class CompanyModel {
    private int ID;
    private String name;
    private ArrayList<EmployeeModel> listaPersonal;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<EmployeeModel> getListaPersonal() {
        return listaPersonal;
    }

    public void setListaPersonal(ArrayList<EmployeeModel> listaPersonal) {
        this.listaPersonal = listaPersonal;
    }
}
