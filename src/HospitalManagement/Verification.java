package HospitalManagement;
import java.sql.*;

public class Verification {
    private final Connection connection;

    public Verification() {
        this.connection = DB_Connection.getConnection();
    }

    public boolean verifyPatientID(int patientId) {
        return verifyID("SELECT patient_id FROM Hospital.Patient WHERE patient_id = ?", patientId);
    }

    public boolean verifyDoctorID(int doctorId) {
        return verifyID("SELECT doctor_id FROM Hospital.Doctor WHERE doctor_id = ?", doctorId);
    }

    public boolean verifyDiagnosisID(int diagnosisId) {
        return verifyID("SELECT diagnosis_id FROM Hospital.Diagnosis WHERE diagnosis_id = ?", diagnosisId);
    }

    public boolean verifyAdminID(int adminId) {
        return verifyID("SELECT admin_id FROM Hospital.Administrator WHERE admin_id = ?", adminId);
    }

    public boolean verifyAdmissionID(int admissionId) {
        return verifyID("SELECT admission_id FROM Hospital.Admission WHERE admission_id = ?", admissionId);
    }

    public boolean verifyTreatmentID(int treatmentId) {
        return verifyID("SELECT treatment_id FROM Hospital.Treatment WHERE treatment_id = ?", treatmentId);
    }

    public boolean verifyEmployeeID(int employeeId) {
        return verifyID("SELECT employee_id FROM Hospital.Employee WHERE employee_id = ?", employeeId);
    }

    private boolean verifyID(String query, int id) {
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

                return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
