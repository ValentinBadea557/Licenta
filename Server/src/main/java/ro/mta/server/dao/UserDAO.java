package ro.mta.server.dao;

import org.bouncycastle.util.encoders.Hex;
import ro.mta.server.Database;
import ro.mta.server.entities.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserDAO implements IUserDAO{
    public User utilizator;
    public UserDAO(){utilizator=new User();}
    public UserDAO(User usr){utilizator=usr;}


    @Override
    public String addUserPlusCompany(String username, String parola, String nume_companie, String Nume, String Prenume, String Adrs, String Telefon, String mail, int admin) {
        CompanieDAO comp=new CompanieDAO();
        int addComp=comp.addCompany(nume_companie);

        if(addComp==-1){
            //eroare
        }else if(addComp==0){
            //exista deja
        }else{
            //adaugare cu succes
            addOnlyUser(username,parola,nume_companie,Nume,Prenume,Adrs,Telefon,mail,1);
        }
        return null;
    }

    @Override
    public String addOnlyUser(String username, String parola, String nume_companie, String Nume, String Prenume, String Adrs, String Telefon, String mail, int admin) {
        CompanieDAO cmp=new CompanieDAO();
        int id_companie=cmp.getCompanyIDbasedOnName(nume_companie);

        Database db=new Database();
        Connection con = db.getConn();

        int unicity=checkUsernameUnicity(username);
        if(unicity==1) {
            String hashpass=hashFunction(parola);
            String sql = "Insert into Useri(Username,Parola,ID_Companie) "
                    + "Values ('" + username + "','" + hashpass+ "','" + id_companie + "')";

            try {
                Statement stmt = con.createStatement();
                stmt.execute(sql);
            } catch (Exception e) {
                System.out.println("Eroare la inserare user");
                e.printStackTrace();
            }
            int id=getUserIDbasedOnUsername(username);
            String sql2="Insert into Date_Personale(ID_User,Nume,Prenume,Adresa,Telefon,Email,Ore_Max_Munca,Ore_Munca) "
                        +"Values ("+id+",'"+Nume+"','"+Prenume+"','"+Adrs+"','"+Telefon+"','"+mail+"',"+8+","+0+")";
            try{
                Statement stmt = con.createStatement();
                stmt.execute(sql2);
            } catch (Exception e) {
                System.out.println("Eroare la inserare date personale");
                e.printStackTrace();
            }

            if (admin==1){
                int id_user=getUserIDbasedOnUsername(username);
                addAdminbasedOnUserID(id_user);
            }
        }else{
            System.out.println("Username existent");
        }

        return null;

    }

    /**
     *
     * @param username
     * @return id-ul corespunzator usernameului sau -1 daca returneaza eroare
     */
    @Override
    public int getUserIDbasedOnUsername(String username) {
        Database db=new Database();
        Connection con = db.getConn();
        int id=-1;
        String sql="Select ID_User from Useri "
                +"where Username='"+username+"'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while(rs.next()){
                id=rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            id=-1;
        }
        return id;
    }

    /**
     *
     * @param username
     * @return 1 daca usernameul este unic, 0 daca se repeta
     */
    @Override
    public int checkUsernameUnicity(String username) {
        Database db=new Database();
        Connection con = db.getConn();

        int unicity=1;
        String sql="Select Username from Useri "
                +"Where Username='"+username+"'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while(rs.next()){
                unicity=0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            unicity=-1;
        }

        return unicity;
    }

    @Override
    public int addAdminbasedOnUserID(int userID) {
        Database db=new Database();
        Connection con = db.getConn();

        String sql="Insert into Admini(ID_User) "
                +"Values ("+userID+")";
        try {
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            System.out.println("Admin adaugat cu succes");
        } catch (SQLException e) {
            System.out.println("Eroare la add admin");
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public String hashFunction(String pass) {
        String sha256hex=new String();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    pass.getBytes(StandardCharsets.UTF_8));
             sha256hex = new String(Hex.encode(hash));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("String original-> "+pass);
        System.out.println("Hash-> "+sha256hex);

        return sha256hex;

    }

}
