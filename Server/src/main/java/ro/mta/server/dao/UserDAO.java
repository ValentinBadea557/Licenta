package ro.mta.server.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONObject;
import ro.mta.server.Database;
import ro.mta.server.entities.Companie;
import ro.mta.server.entities.Project;
import ro.mta.server.entities.User;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class UserDAO implements IUserDAO {
    public User utilizator;

    public UserDAO() {
        utilizator = new User();
    }

    public UserDAO(User usr) {
        utilizator = usr;
    }


    @Override
    public String addUserPlusCompany(String username, String parola, String nume_companie, String Nume, String Prenume, String Adrs, String Telefon, String mail, int admin) {
        JSONObject result = new JSONObject();
        int checkunicity = checkUsernameUnicity(username);
        if (checkunicity == 1) {
            CompanieDAO comp = new CompanieDAO();
            int addComp = comp.addCompany(nume_companie);


            if (addComp == -1) {
                //eroare
                result.put("Result Register", "Error trying to add Company!");

            } else if (addComp == 0) {
                //exista deja
                result.put("Result Register", "This company's name is already used!");

            } else {
                //adaugare cu succes
                addOnlyUser(username, parola, nume_companie, Nume, Prenume, Adrs, Telefon, mail, 1);
                result.put("Result Register", "ok");
            }
        } else {
            result.put("Result Register", "Username already used!");
        }
        return result.toString();
    }

    @Override
    public String addOnlyUser(String username, String parola, String nume_companie, String Nume, String Prenume, String Adrs, String Telefon, String mail, int admin) {

        JSONObject json=new JSONObject();

        int unicity = checkUsernameUnicity(username);
        if (unicity == 1) {
            CompanieDAO cmp = new CompanieDAO();
            int id_companie = cmp.getCompanyIDbasedOnName(nume_companie);


            Database db = new Database();
            Connection con = db.getConn();


            String hashpass = hashFunction(parola);
            String sql = "Insert into Useri(Username,Parola,ID_Companie) "
                    + "Values ('" + username + "','" + hashpass + "','" + id_companie + "')";

            try {
                Statement stmt = con.createStatement();
                stmt.execute(sql);
            } catch (Exception e) {
                json.put("Response Add User","Error");
                e.printStackTrace();
            }
            int id = getUserIDbasedOnUsername(username);
            String sql2 = "Insert into Date_Personale(ID_User,Nume,Prenume,Adresa,Telefon,Email,Ore_Max_Munca,Ore_Munca) "
                    + "Values (" + id + ",'" + Nume + "','" + Prenume + "','" + Adrs + "','" + Telefon + "','" + mail + "'," + 8 + "," + 0 + ")";
            try {
                Statement stmt = con.createStatement();
                stmt.execute(sql2);
            } catch (Exception e) {
                json.put("Response Add User","Error");
                e.printStackTrace();
            }

            if (admin == 1) {
                int id_user = getUserIDbasedOnUsername(username);
                addAdminbasedOnUserID(id_user);
            }
            json.put("Response Add User","ok");
        } else {
            json.put("Response Add User","Duplicate username");
        }

        return json.toString();

    }

    /**
     * @param username
     * @return id-ul corespunzator usernameului sau -1 daca returneaza eroare
     */
    @Override
    public int getUserIDbasedOnUsername(String username) {
        Database db = new Database();
        Connection con = db.getConn();
        int id = -1;
        String sql = "Select ID_User from Useri "
                + "where Username='" + username + "'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            id = -1;
        }
        return id;
    }

    /**
     * @param username
     * @return 1 daca usernameul este unic, 0 daca se repeta
     */
    @Override
    public int checkUsernameUnicity(String username) {
        Database db = new Database();
        Connection con = db.getConn();

        int unicity = 1;
        String sql = "Select Username from Useri "
                + "Where Username='" + username + "'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                unicity = 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            unicity = -1;
        }

        return unicity;
    }

    @Override
    public int addAdminbasedOnUserID(int userID) {
        Database db = new Database();
        Connection con = db.getConn();

        String sql = "Insert into Admini(ID_User) "
                + "Values (" + userID + ")";
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
    public String login(String user, String password) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        Database db = new Database();
        Connection con = db.getConn();
        String hashPassword = hashFunction(password);
        int results = 0;
        String result = null;
        User userReturned = new User();

        String sql = "Select * from Useri U " +
                "inner join Date_Personale DP " +
                "on U.ID_User=DP.ID_User " +
                "where U.Username='" + user + "' AND U.Parola='" + hashPassword + "'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                results++;
                int idCompanie;

                userReturned.setID(rs.getInt(1));
                userReturned.setAdmin(checkIfUserIsAdmin(rs.getInt(1)));
                userReturned.setUsername(rs.getString(2));
                userReturned.setPassword(password);
                idCompanie = rs.getInt(4);
                Companie company = getCompanybasedOnID(idCompanie);
                userReturned.setCompany(company);
                userReturned.setLastname(rs.getString(7));
                userReturned.setFirstname(rs.getString(8));
                userReturned.setAddr(rs.getString(9));
                userReturned.setPhone(rs.getString(10));
                userReturned.setEmail(rs.getString(11));
                userReturned.setOre_max_munca(rs.getInt(12));
                userReturned.setOre_munca(rs.getInt(13));

                result = gson.toJson(userReturned);
            }
            if (results != 0)
                return result;
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Eroare";
        }
        return "Eroare";
    }


    @Override
    public String hashFunction(String pass) {
        String sha256hex = new String();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    pass.getBytes(StandardCharsets.UTF_8));
            sha256hex = new String(Hex.encode(hash));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("String original-> " + pass);
        System.out.println("Hash-> " + sha256hex);

        return sha256hex;

    }

    @Override
    public int checkIfUserIsAdmin(int IDuser) {
        Database db = new Database();
        Connection con = db.getConn();
        String sql = "Select * from Admini " +
                "Where ID_User=" + IDuser;
        int result = 0;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                result = 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Companie getCompanybasedOnID(int IDcompany) {
        Database db = new Database();
        Connection con = db.getConn();
        String sql = "Select * from Companii " +
                "Where ID_Companie=" + IDcompany;

        Companie company = new Companie();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                company.setId(rs.getInt(1));
                company.setNume(rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return company;
    }

    /**
     * @param IDuser
     * @return returneaza un String ce contine 2 vectori, vector cu ID uri proiecte si un vector cu numele acestora.
     */
    @Override
    public String getListOfActiveProjects(int IDuser) {
        Database db = new Database();
        Connection con = db.getConn();
        String sql = "Select * from Proiecte P " +
                "Inner join Proiecte_Useri PU " +
                "on P.ID_Proiect=PU.ID_Proiect " +
                "where PU.ID_USER=" + IDuser;

        JsonArray ids = new JsonArray();
        JsonArray names = new JsonArray();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                ids.add(rs.getInt(1));
                names.add(rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject();
        obj.put("IDs", ids);
        obj.put("Names:", names);

        String jsonString = obj.toString();
        return jsonString;
    }

    /***
     *
     * @param idUser
     * @return user object based on ID
     */
    @Override
    public User getUserbasedOnID(int idUser) {
        Database db = new Database();
        Connection con = db.getConn();
        User userReturned = new User();

        String sql = "Select * from Useri U " +
                "inner join Date_Personale DP " +
                "on U.ID_User=DP.ID_User " +
                "Where U.ID_User=" + idUser;

        String result = null;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {

                userReturned.setID(rs.getInt(1));
                userReturned.setAdmin(checkIfUserIsAdmin(rs.getInt(1)));
                userReturned.setUsername(rs.getString(2));
//                int idCompanie = rs.getInt(4);
//                Companie company = getCompanybasedOnID(idCompanie);
//                userReturned.setCompany(company);
                userReturned.setLastname(rs.getString(7));
                userReturned.setFirstname(rs.getString(8));
                userReturned.setAddr(rs.getString(9));
                userReturned.setPhone(rs.getString(10));
                userReturned.setEmail(rs.getString(11));
                userReturned.setOre_max_munca(rs.getInt(12));
                userReturned.setOre_munca(rs.getInt(13));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Eroare";
        }
        return userReturned;
    }


    /***Functions used by administrators**/
    @Override
    public String AdministratorViewAllEmployees(int IDuseradmin) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        Database db = new Database();
        Connection con = db.getConn();
        String sql = "Select * from Useri U " +
                "Inner join Companii C " +
                "on U.ID_Companie=C.ID_Companie " +
                "where U.ID_User=" + IDuseradmin;

        Companie company = new Companie();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                company.setId(rs.getInt(5));
                company.setNume(rs.getString(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql2 = "Select * from Useri " +
                "Where ID_Companie=" + company.getId();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql2);
            ResultSetMetaData meta = rs.getMetaData();
            int idUserCurrent;
            while (rs.next()) {
                idUserCurrent = rs.getInt(1);
                company.addEmployee(getUserbasedOnID(idUserCurrent));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String returnString = gson.toJson(company);

        return returnString;
    }


}
