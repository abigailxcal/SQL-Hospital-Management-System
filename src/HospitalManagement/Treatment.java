package HospitalManagement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Treatment{
    private Connection connection;

    public Treatment(){
        this.connection = DB_Connection.getConnection();
    }
    public List<String> getAllTreatments() {
        List<String> treatments = new ArrayList<String>();
        String query = "SELECT treatment_id, description AS treatment_name, COUNT(treatment_id) AS total_occurrences "+
            "FROM Hospital.Treatment GROUP BY treatment_id, description ORDER BY total_occurrences DESC;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int treatmentId = rs.getInt("treatment_id");
                String treatmentName = rs.getString("treatment_name");
                int totalOccurrences = rs.getInt("total_occurrences");
                treatments.add("Treatment ID: " + treatmentId + ", Name: " + treatmentName + ", Total Occurrences: " + totalOccurrences);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return treatments;
    }

    public boolean createTreatment(String treatmentType, String description, String timeOrdered, int admissionId, int doctorId, int employeeId) {
        String query = " INSERT INTO Hospital.Treatment (TreatmentType, description, time_ordered, admission_id, ordered_by, administered_by) VALUES (?, ?, ?, ?, ?); ";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, treatmentType);
            stmt.setString(2, description);
            stmt.setString(3, timeOrdered);
            stmt.setInt(4, admissionId);
            stmt.setInt(5, doctorId);
            stmt.setInt(6,employeeId); //administered_by
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Return false if the operation failed
    }


    public boolean updateTreatmentAdministration(int treatmentId, String adminTime) {
        String query = "UPDATE Hospital.Treatment SET time_administered = ? WHERE treatment_id = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, adminTime);
            stmt.setInt(2, treatmentId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}