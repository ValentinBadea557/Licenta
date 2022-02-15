package ro.mta.server.entities;

public class Companie {
    private int id;
    private String nume;

    public Companie(){};
    public Companie(int idparam,String numeparam){
        this.id=idparam;
        this.nume=numeparam;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }
}
