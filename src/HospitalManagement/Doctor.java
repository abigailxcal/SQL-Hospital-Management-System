package HospitalManagement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Doctor{
    private final Connection connection;

    public Doctor(){
        this.connection = DB_Connection.getConnection();
    }

    public List<String> getAllDoctors() {
        List<String> doctors = new ArrayList<String>();
        String query = "SELECT doctor_id, Specialty FROM Hospital.Doctor";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int doctorId = rs.getInt("doctor_id");
                String specialty = rs.getString("Specialty");
                doctors.add("Doctor ID: " + doctorId + ", Specialty: " + specialty);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }


    public List<String> getPrimaryDoctorsWithHighAdmissions() {
        List<String> results = new ArrayList<String>();
        String query = "SELECT Doctor.doctor_id, Employee.name, COUNT(Admission.admission_id) AS admission_count FROM Hospital.Admission JOIN Hospital.Doctor ON Admission.primary_doctor_id = Doctor.doctor_id JOIN Hospital.Employee ON Doctor.doctor_id = Employee.employee_id WHERE Admission.admission_date >= DATE_SUB(CURDATE(), INTERVAL 1 YEAR) GROUP BY Doctor.doctor_id, Employee.name HAVING admission_count >= 4 ORDER BY admission_count DESC;";
        try  {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int doctorId = rs.getInt("doctor_id");
                String name = rs.getString("name");
                int admissionCount = rs.getInt("admission_count");
                results.add("Doctor ID: " + doctorId + ", Name: " + name + ", Admission Count: " + admissionCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }


}