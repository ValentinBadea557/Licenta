package ro.mta.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection conn;

    public Database(){
        this.createConnection();
    }

    public void createConnection(){
        try {

        String connectionUrl = "jdbc:sqlserver://localhost:1433;"
                +"database=Licenta;"
                +"user=valentin;"
                +"password=1234;";
            this.conn = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Connection getConn() {
        return this.conn;
    }
}
