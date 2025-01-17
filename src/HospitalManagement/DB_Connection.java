package HospitalManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DB_Connection {
    static final String CONNECTION_URL ="jdbc:mysql://127.0.0.1:3306/Hospital";
    static final String DB_USERNAME="student";
    static final String DB_PASSWORD="yourpassword";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to database.");
        }
    }
}


