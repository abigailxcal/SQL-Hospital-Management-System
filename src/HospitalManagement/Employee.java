package HospitalManagement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Employee{
private Connection connection;

public Employee(){
        this.connection = DB_Connection.getConnection();
    }
    public List<String> getAllWorkers() {
        List<String> results = new ArrayList<String>();
        String query = "SELECT Employee.name, Employee.employee_id, " +
        "CASE "+
            "WHEN EXISTS (SELECT 1 FROM Hospital.Doctor WHERE Doctor.doctor_id = Employee.employee_id) THEN 'Doctor' " +
            "WHEN EXISTS (SELECT 1 FROM Hospital.Nurse WHERE Nurse.nurse_id = Employee.employee_id) THEN 'Nurse' "+
            "WHEN EXISTS (SELECT 1 FROM Hospital.Technician WHERE Technician.technician_id = Employee.employee_id) THEN 'Technician' "+
            "WHEN EXISTS (SELECT 1 FROM Hospital.Administrator WHERE Administrator.admin_id = Employee.employee_id) THEN 'Administrator' "+
            "ELSE 'Unknown' " +
        "END AS job_category FROM Hospital.Employee ORDER BY Employee.name ASC;";

        try  {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String jobCategory = rs.getString("job_category");
                results.add("Name: " + name + ", Job Category: " + jobCategory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
    public void addDoctor(int employeeId, String specialtyOrCertification){
        String insertRoleQuery = "INSERT INTO Hospital.Doctor (doctor_id, Specialty) VALUES (?, ?)";
        try {
            PreparedStatement roleStmt = connection.prepareStatement(insertRoleQuery);
            roleStmt.setInt(1, employeeId);
            roleStmt.setString(2, specialtyOrCertification);
            roleStmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addNurse(int employeeId, String specialtyOrCertification){
        String insertRoleQuery = "INSERT INTO Hospital.Nurse (nurse_id, certification) VALUES (?, ?)";
        try {
            PreparedStatement roleStmt = connection.prepareStatement(insertRoleQuery);
            roleStmt.setInt(1, employeeId);
            roleStmt.setString(2, specialtyOrCertification);
            roleStmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void addAdministrator(int employeeId){
        String insertRoleQuery = "INSERT INTO Hospital.Administrator (admin_id) VALUES (?)";
        try {
            PreparedStatement roleStmt = connection.prepareStatement(insertRoleQuery);
            roleStmt.setInt(1, employeeId);

            roleStmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void addTechnician(int employeeId, String specialtyOrCertification){
        String insertRoleQuery = "INSERT INTO Hospital.Technician (technician_id, skill) VALUES (?, ?)";
        try {
            PreparedStatement roleStmt = connection.prepareStatement(insertRoleQuery);
            roleStmt.setInt(1, employeeId);
            roleStmt.setString(2, specialtyOrCertification);
            roleStmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public int addEmployee(String name, String dob, String startDate, String role, String specialtyOrCertification) {
        String insertEmployeeQuery = "INSERT INTO Hospital.Employee (name, DOB, start_date) VALUES (?, ?, ?)";
        try  {
            PreparedStatement stmt = connection.prepareStatement(insertEmployeeQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, dob);
            stmt.setString(3, startDate);
            stmt.executeUpdate();


            ResultSet rs = stmt.getGeneratedKeys();    //gets the generated employee_id
            if (rs.next()) {
                int employeeId = rs.getInt(1);
                // inserts into the role-specific table based on the role
                if ("doctor".equals(role)) {
                    addDoctor(employeeId, specialtyOrCertification);
                } else if ("nurse".equals(role)) {
                    addNurse(employeeId, specialtyOrCertification);
                } else if ("technician".equals(role)) {
                    addTechnician(employeeId, specialtyOrCertification);
                } else if ("administrator".equals(role)) {
                    addAdministrator(employeeId);
                } else {
                    throw new IllegalStateException("Unexpected value: " + role.toLowerCase());
                }
                return employeeId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

}