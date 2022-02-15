package ro.mta.server.dao;

/**
 * interfata pentru user dao
 */
public interface IUserDAO {
    //toate functiile pe care user le va folosi

    public String addUserPlusCompany(String username,String parola,String nume_companie,String Nume,String Prenume,String Adrs,String Telefon,String mail, int admin);
    public String addOnlyUser(String username,String parola,String nume_companie,String Nume,String Prenume,String Adrs,String Telefon,String mail, int admin);
    public int getUserIDbasedOnUsername(String username);
    public int checkUsernameUnicity(String username);
    public int addAdminbasedOnUserID(int userID);
    public String hashFunction(String pass);

}
