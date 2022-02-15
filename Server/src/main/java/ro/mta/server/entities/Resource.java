package ro.mta.server.entities;

public class Resource {
    private int ID;
    private String denumire;
    private int quantity;
    private boolean shareable;
    private String descriere;
    private int IDcompanie;

    public Resource(){}
    public Resource(int id,String nume,int quant,boolean s,String descrp, int IDc){
        this.ID=id;
        this.denumire=nume;
        this.quantity=quant;
        this.shareable=s;
        this.descriere=descrp;
        this.IDcompanie=IDc;
    }

}
