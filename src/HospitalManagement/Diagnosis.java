package HospitalManagement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Diagnosis {
    private final Connection connection;

    public Diagnosis(){
        this.connection = DB_Connection.getConnection();
    }

    public int createDiagnosis(String diagnosisName) {
        String query = "INSERT INTO Hospital.Diagnosis (diagnosis_name) VALUES (?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, diagnosisName);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<String> getDiagnosisDescending() {
        return getDiagnosisOccurrences("DESC");
    }

    public List<String> getDiagnosisAscending() {
        return getDiagnosisOccurrences("ASC");
    }

    public List<String> getDiagnosisOccurrences(String order) {
        List<String> results = new ArrayList<String>();
        String query =
        "SELECT Diagnosis.diagnosis_id, Diagnosis.diagnosis_name, COUNT(Admission.initial_diagnosis) AS total_occurrences "+
        "FROM Hospital.Diagnosis " +
        "JOIN Hospital.Admission ON Diagnosis.diagnosis_id = Admission.initial_diagnosis " +
        "GROUP BY Diagnosis.diagnosis_id, Diagnosis.diagnosis_name " +
        "ORDER BY total_occurrences "+ order+ ";";


        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int diagnosisId = rs.getInt("diagnosis_id");
                String name = rs.getString("diagnosis_name");
                int occurrences = rs.getInt("total_occurrences");
                results.add("Diagnosis ID: " + diagnosisId + ", Name: " + name + ", Total Occurrences: " + occurrences);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}