package ro.mta.server.dao;

public interface ICompanyDAO {
    public int addCompany(String nume_companie);
    public int checkCompanyExistance(String nume_companie);
    public int getCompanyIDbasedOnName(String nume_companie);

}
