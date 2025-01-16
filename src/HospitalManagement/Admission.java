package HospitalManagement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Admission {
    private Connection connection;

    public Admission() {
        this.connection = DB_Connection.getConnection();
    }

    public boolean createAdmission(int patientId, int primaryDoctorId, String admissionDate, int diagnosisId, int roomNumber, int adminId) {
        String query = "INSERT INTO Hospital.Admission (patient_id, primary_doctor_id, admission_date, initial_diagnosis, room_number, admin_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try  {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, patientId);
            stmt.setInt(2, primaryDoctorId);
            stmt.setString(3, admissionDate);
            stmt.setInt(4, diagnosisId);
            stmt.setInt(5, roomNumber);
            stmt.setInt(6, adminId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void dischargePatient(int admissionId, String dischargeDate, int roomNumber) {
        String updateAdmission = "UPDATE Hospital.Admission SET discharge_date = ? WHERE admission_id = ?";
        try  {
            PreparedStatement stmt = connection.prepareStatement(updateAdmission);
            stmt.setString(1, dischargeDate);
            stmt.setInt(2, admissionId);
            stmt.executeUpdate();
            // mark the room as available
            Room room = new Room();
            room.markRoomAvailable(roomNumber);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Patient could not be discharged.");
        }
        System.out.println("Patient discharged successfully.");
    }

    // ensures changes are authorized by the primary doctor
    public boolean isAuthorizedDoctor(int admissionId, int doctorId) {
        String query = "SELECT primary_doctor_id FROM Hospital.Admission WHERE admission_id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, admissionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int primaryDoctorId = rs.getInt("primary_doctor_id");
                return primaryDoctorId == doctorId;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // ensures changes are authorized
    public boolean isAuthorizedAdmin( int adminId) {
        String query = "SELECT admin_id FROM Hospital.Administrator WHERE admin_id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, adminId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Administrator verified.");
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // should add verification that only the primary doctor is assigning other staff to an admission
    public boolean assignDoctor(int patientId, int doctorId, int admissionId, boolean isPrimary) {
        String query = "INSERT INTO Hospital.AssignedDoctors (patient_id, doctor_id, admission_id, isPrimary) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            stmt.setInt(3, admissionId);
            stmt.setBoolean(4, isPrimary);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<Integer> getAssignedDoctors(int admissionId) {
        List<Integer> doctorIds = new ArrayList<Integer>();
        String query = "SELECT doctor_id FROM Hospital.AssignedDoctors WHERE admission_id = ?";

        try  {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, admissionId); // Set the admission ID


                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    doctorIds.add(rs.getInt("doctor_id"));
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctorIds;
    }

    public List<String> getAdmissionInfo(int admissionId){
        List<String> admissionDetails = new ArrayList<String>();
        String query = "SELECT * FROM Hospital.Admission WHERE admission_id = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, admissionId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                admissionDetails.add("Admission ID: " + rs.getInt("admission_id"));
                admissionDetails.add("Patient ID: " + rs.getInt("patient_id"));
                admissionDetails.add("Primary Doctor ID: " + rs.getInt("primary_doctor_id"));
                admissionDetails.add("Admission Date: " + rs.getDate("admission_date"));
                admissionDetails.add("Initial Diagnosis ID: " + rs.getInt("initial_diagnosis"));
                admissionDetails.add("Room Number: " + rs.getInt("room_number"));
                admissionDetails.add("Discharge Date: " + rs.getDate("discharge_date"));
                admissionDetails.add("Admin ID: " + rs.getInt("admin_id"));
            } else {
                admissionDetails.add("No admission found for Admission ID: " + admissionId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admissionDetails;
    }



    public List<String> getAllCurrentlyAdmittedPatients(){
        List<String> patients = new ArrayList<String>();
        String query = "SELECT DISTINCT Patient.patient_id,Patient.name " +
                "FROM Hospital.Patient " +
                "JOIN Hospital.Admission ON Patient.patient_id = Admission.patient_id " +
                "WHERE Admission.discharge_date IS NULL;";
        try  {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("patient_id");
                String name = rs.getString("name");

                patients.add(" Patient ID: " + id + ", Name: " + name );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;

    }

    public List<String> getPatientsDischargedInDateRange(String startDate, String endDate) {
        List<String> patients = new ArrayList<String>();

        String query = "SELECT DISTINCT Patient.patient_id, Patient.name " +
                "FROM Hospital.Patient " +
                "JOIN Hospital.Admission ON Patient.patient_id = Admission.patient_id " +
                "WHERE Admission.discharge_date BETWEEN ? AND ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int patientId = rs.getInt("patient_id");
                String name = rs.getString("name");
                patients.add("ID: " + patientId + ", Name: " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }

    public int getRoomNumberForAdmission(int admissionId) {
        String query = "SELECT room_number FROM Hospital.Admission WHERE admission_id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, admissionId);
            ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("room_number");
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
