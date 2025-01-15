package HospitalManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DB_Connection {
    private static final String CONNECTION_URL = "jdbc:mysql://127.0.0.1:3306/?user=root";

    public DB_Connection() {
    }
    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(CONNECTION_URL);

//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM Hospital.Patient");
//
//            while (resultSet.next()) {
//                System.out.println(resultSet.getString("first_name"));
//            }




    }
}


