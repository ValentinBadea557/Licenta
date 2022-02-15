package ro.mta.server.dao;

import ro.mta.server.Database;
import ro.mta.server.entities.Companie;

import java.sql.*;

public class CompanieDAO implements ICompanyDAO{
    public Companie company;
    public CompanieDAO(){company=new Companie();}
    public CompanieDAO(Companie cmp){company=cmp;}


    /**
     *
     * @param nume_companie
     * @return 0 daca exista deja, 1 success, -1 eroare
     */
    @Override
    public int addCompany(String nume_companie) {
        Database db=new Database();
        Connection con = db.getConn();
        int result=-1;
        int check=checkCompanyExistance(nume_companie);
        String returnString="";
        if(check==1){
            System.out.println("Already exists");
           result=0;
        }else if(check==0) {
            try {
                System.out.println("Do not exists");
                String sql1 = "INSERT INTO Companii(Nume)"
                        + "Values ('" + nume_companie + "')";
                Statement stmt = con.createStatement();
                stmt.execute(sql1);
                System.out.println("Companie adaugata");
                result=1;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Eroare SQL");
                result=-1;
            }
        }else{
            System.out.println("Eroare");
            result=-1;
        }

        return result;
    }

    /**
     *
     * @param nume_companie
     * @return 0 daca nu exista, ID-ul din baza de date data exista
     */
    @Override
    public int checkCompanyExistance(String nume_companie) {
        Database db=new Database();
        Connection con = db.getConn();
        int result=-1;
        int id_company=-1;
        String sql1="Select ID_Companie from Companii "
                +"where Nume='"+nume_companie+"'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql1);
            ResultSetMetaData meta = rs.getMetaData();
            int inregistrari = 0;
            while (rs.next()) {               /**testez daca exista compania deja in baza de date   */
                id_company = rs.getInt(1);
                inregistrari++;
            }
            if(inregistrari!=0){
                result=id_company;
            }else{
                result=0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result=-1;

        }
        return result;

    }

    /**
     *
     * @param nume_companie
     * @return id-ul companiei sau -1 in caz de eroare
     */
    @Override
    public int getCompanyIDbasedOnName(String nume_companie) {
        Database db=new Database();
        Connection con = db.getConn();
        int result=0;
        int id = -1;
        String sql="Select ID_Companie from Companii "
                +"where Nume='"+nume_companie+"'";
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {               /**testez daca exista compania deja in baza de date   */
                id= rs.getInt(1);
            }
            result=id;
        } catch (Exception e) {
            e.printStackTrace();
            result=-1;
        }
        return result;
    }
}
