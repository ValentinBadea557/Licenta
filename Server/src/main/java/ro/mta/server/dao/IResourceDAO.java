package ro.mta.server.dao;

import ro.mta.server.entities.Resource;

public interface IResourceDAO {

    public String addResource(String nume,int cant,boolean sh,String descriere,int idCompanie);
    public String getListOfResourcesBasedOnCompanyID(int IDcompany);
    public String getResourcebasedOnID(int id);
    public int getResourceIDbasedOnName(String name);
    public Resource getFullResourceBasedOnID(int idRes);
}
