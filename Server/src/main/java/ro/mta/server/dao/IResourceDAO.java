package ro.mta.server.dao;

public interface IResourceDAO {

    public String addResource(String nume,int cant,boolean sh,String descriere,int idCompanie);
    public String getListOfResourcesBasedOnCompanyID(int IDcompany);
    public String getResourcebasedOnID(int id);
    public int getResourceIDbasedOnName(String name);
}
