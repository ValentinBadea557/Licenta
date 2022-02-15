package ro.mta.server.dao;

import ro.mta.server.Database;
import ro.mta.server.entities.Companie;
import ro.mta.server.entities.Resource;

import java.sql.*;

public class ResourceDAO implements IResourceDAO{
    public Resource resursa;
    public ResourceDAO(){resursa=new Resource();}
    public ResourceDAO(Resource rs){resursa=rs;}


    @Override
    public String addResource(String nume, int cant, boolean sh, String descriere, int idCompanie) {
        Database db=new Database();
        Connection con = db.getConn();

        int bitValue;
        if(sh==true){
            bitValue=1;
        }else{
            bitValue=0;
        }
        String sql="Insert into Resurse "
                +"Values ('"+nume+"',"+cant+","+bitValue+",'"+descriere+"',"+idCompanie+")";
        try {
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            System.out.println("Resursa adaugata");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getResourceIDbasedOnName(String name) {
        Database db=new Database();
        Connection con = db.getConn();
        int id=0;
        String sql="Select ID_Resursa from Resurse "
                +"where Denumire='"+name+"'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {               /**testez daca exista compania deja in baza de date   */
                id=rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            id=-1;
        }
        System.out.println("ID Resursa : "+id);
        return id;
    }

    @Override
    public String getResourcebasedOnID(int id) {
        Database db=new Database();
        Connection con = db.getConn();
        String numeResursa=new String();
        String sql="Select Denumire from Resurse "
                +"where ID_Resursa="+id;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {               /**testez daca exista compania deja in baza de date   */
                numeResursa=rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Nume resursa: "+numeResursa);
        return numeResursa;
    }
}
