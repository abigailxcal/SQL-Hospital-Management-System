package HospitalManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DB_Connection {
    private static final String CONNECTION_URL = "jdbc:mysql://127.0.0.1:3306/?user=root";
    public static Connection getConnection() {
        //System.out.println("Connecting to Database. . .");
        try {
            return DriverManager.getConnection(CONNECTION_URL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to database.");
        }
    }
}


