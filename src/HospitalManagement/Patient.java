package HospitalManagement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Patient {
    private final Connection connection;


    public Patient(){
        this.connection = DB_Connection.getConnection();
    }
    public List<String> getAllPatients() {
        List<String> patients = new ArrayList<String>();
        String query = "SELECT * FROM Hospital.Patient;";

        try  {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("patient_id");
                String name = rs.getString("name");
                int ssn = rs.getInt("SSN");
                String dob = rs.getString("DOB");
                String contact = rs.getString("emergency_contact");
                String policy = rs.getString("insurance_policy");
                patients.add(" Patient ID: " + id + ", Name: " + name +", SSN: "+ ssn+ ", DOB: " + dob +
                        ", Contact: " + contact + ", Policy: " + policy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }




    public boolean patientExists(int ssn) {
        String query = "SELECT patient_id FROM Hospital.Patient WHERE SSN = ?";
        try  {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, ssn);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public int getPatientID(int ssn){
        String query = "SELECT patient_id FROM Hospital.Patient WHERE SSN = ?";
        try  {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, ssn);
            ResultSet rs = stmt.executeQuery();
            return rs.getInt("patient_id");

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

    }

    public int addPatient(String name, String dob, String emergencyContact, String insurancePolicy, int ssn) {
        String query = "INSERT INTO Hospital.Patient (name, DOB, emergency_contact, insurance_policy, SSN) " +
                "VALUES (?, ?, ?, ?, ?)";
        try  {
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, dob);
            stmt.setString(3, emergencyContact);
            stmt.setString(4, insurancePolicy);
            stmt.setInt(5, ssn);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // return the generated patient_id
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }



}
