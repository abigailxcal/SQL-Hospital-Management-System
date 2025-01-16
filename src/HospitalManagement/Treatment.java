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