package ro.mta.licenta.badea.models;

import java.util.ArrayList;

public class CompanyModel {
    private int id;
    private String nume;
    private ArrayList<EmployeeModel> listaEmployees =new ArrayList<>();

    public CompanyModel(int id,String name,ArrayList<EmployeeModel> lista){
        this.id=id;
        this.nume=name;
        this.listaEmployees=new ArrayList<>(lista);
    }

    public CompanyModel(){}

    public void printDetails(){
        System.out.println("Nume:"+nume);
        System.out.println("\nSize:"+listaEmployees.size());
    }

    public int getID() {
        return id;
    }

    public void setID(int ID) {
        this.id = ID;
    }

    public String getName() {
        return nume;
    }

    public void setName(String name) {
        this.nume = name;
    }

    public ArrayList<EmployeeModel> getListaPersonal() {
        return listaEmployees;
    }

    public void setListaPersonal(ArrayList<EmployeeModel> listaPersonal) {
        this.listaEmployees = new ArrayList<>(listaPersonal);
    }
}
