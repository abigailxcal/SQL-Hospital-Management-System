package HospitalManagement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private final Connection connection;

    public Room(){
        this.connection = DB_Connection.getConnection();
    }

    public boolean isRoomAvailable(int roomNumber) {
        String query = "SELECT Status FROM Hospital.Room WHERE room_number = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, roomNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return "Available".equals(rs.getString("Status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void markRoomOccupied(int roomNumber) {
        String query = "UPDATE Hospital.Room SET Status = 'Occupied' WHERE room_number = ?";
        try  {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, roomNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void markRoomAvailable(int roomNumber) {
        String query = "UPDATE Hospital.Room SET Status = 'Available' WHERE room_number = ?";
        try  {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, roomNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> listOccupiedRooms() {
        List<String> results = new ArrayList<String>();

        String query = " SELECT Room.room_number, Patient.name AS patient_name, Admission.admission_date "+
            "FROM Hospital.Room " +
            "JOIN Hospital.Admission ON Room.room_number = Admission.room_number "+
           "JOIN Hospital.Patient ON Admission.patient_id = Patient.patient_id "+
            "WHERE Room.Status = 'Occupied' AND Admission.discharge_date IS NULL;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int roomNumber = rs.getInt("room_number");
                String patientName = rs.getString("patient_name");
                Date admissionDate = rs.getDate("admission_date");
                results.add("Room: " + roomNumber + ", Patient: " + patientName + ", Admission Date: " + admissionDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<Integer> listUnoccupiedRooms() {
        List<Integer> rooms = new ArrayList<Integer>();
        String query = "SELECT room_number FROM Hospital.Room WHERE Status = 'Available'";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rooms.add(rs.getInt("room_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public List<String> listAllRooms() {
        List<String> results = new ArrayList<String>();
        String query = "SELECT Room.room_number, Room.Status, Patient.name AS patient_name, Admission.admission_date "+
            "FROM Hospital.Room "+
            "LEFT JOIN Hospital.Admission ON Room.room_number = Admission.room_number AND Admission.discharge_date IS NULL " +
            "LEFT JOIN Hospital.Patient ON Admission.patient_id = Patient.patient_id;";

        try  {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int roomNumber = rs.getInt("room_number");
                String status = rs.getString("Status");
                String patientName = rs.getString("patient_name");
                Date admissionDate = rs.getDate("admission_date");

                if ("Occupied".equals(status)) {
                    results.add("Room: " + roomNumber + ", Status: " + status +
                            ", Patient: " + patientName + ", Admission Date: " + admissionDate);
                } else {
                    results.add("Room: " + roomNumber + ", Status: " + status );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
