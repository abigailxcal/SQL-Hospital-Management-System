package HospitalManagement;
import java.util.Scanner;
import java.util.List;

public class Driver {

    private static void managePatients(Scanner scanner, Patient patient) {
        while (true) {
            System.out.println("\nPATIENTS MENU");
            System.out.println("[1] Add a patient");
            System.out.println("[2] Check if a patient is already in the system");
            System.out.println("[3] View all patients in the system");
            System.out.println("[0] Return to main menu");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter patient name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter date of birth (YYYY-MM-DD): ");
                    String dob = scanner.nextLine();
                    System.out.print("Enter emergency contact: (name - phone number) ");
                    String emergencyContact = scanner.nextLine();
                    System.out.print("Enter insurance info: ");
                    String insuranceInfo = scanner.nextLine();
                    System.out.print("Enter SSN: ");
                    int ssn = scanner.nextInt();
                    scanner.nextLine();


                    int patientId = patient.addPatient(name, dob, emergencyContact, insuranceInfo, ssn);
                    if (patientId != -1) {
                        System.out.println("Patient added successfully with ID: " + patientId);
                    } else {
                        System.out.println("Failed to add patient.");
                    }

                case 2:
                    System.out.println("Checking if patient exists...");
                    System.out.print("Enter SSN of patient: ");
                    int searchSsn = scanner.nextInt();
                    scanner.nextLine();
                    boolean exists = patient.patientExists(searchSsn);
                    if (exists) {
                        System.out.println("Patient exists in the system.");
                    } else {
                        System.out.println("Patient not found in the system.");
                    }
                    break;
                case 3:
                    System.out.println("Viewing all patients...");
                    List<String> allPatients = patient.getAllPatients();
                    for (String item: allPatients) {
                        System.out.println(item);
                    }

                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private static void manageAdmissions(Scanner scanner, Admission admission) {
        while (true) {
            System.out.println("\nADMISSIONS MENU");
            System.out.println("[1] Create an admission");
            System.out.println("[2] Discharge a patient");
            System.out.println("[3] Assign a doctor to an admission");
            System.out.println("[4] View patients discharged between specific dates");
            System.out.println("[5] View details about a specific admission");
            System.out.println("[6] View all currently admitted patients");
            System.out.println("[0] Return to main menu");
            Room room = new Room();
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            int roomNumber;
            int adminId;
            int admission_id;

            switch (choice) {
                case 1:

                    System.out.println("Creating an admission...");
                    System.out.print("Enter patient ID: ");
                    int patientId = scanner.nextInt();
                    System.out.print("Enter primary doctor ID: ");
                    int primaryDoctorId = scanner.nextInt();

                    System.out.print("Enter diagnosis ID: ");
                    int diagnosisId = scanner.nextInt();
                    System.out.print("Enter admission date (YYYY-MM-DD): ");
                    String admissionDate = scanner.nextLine();

                    // authorization needed
                    System.out.print("Enter admin ID to assign a room: ");
                    adminId = scanner.nextInt();
                    System.out.print("Enter room number: ");
                    roomNumber = scanner.nextInt();
                    while (!room.isRoomAvailable(roomNumber)) {
                        System.out.println("Room " + roomNumber + " is not available. Choose another room: ");
                        roomNumber = scanner.nextInt();
                    }

                    boolean admissionCreated = admission.createAdmission(patientId, primaryDoctorId, admissionDate, diagnosisId, roomNumber, adminId);
                    if (admissionCreated) {
                        room.markRoomOccupied(roomNumber);
                        System.out.println("Admission created successfully.");
                    } else {
                        System.out.println("Failed to create admission.");
                    }

                    break;
                case 2:

                    System.out.println("Discharging a patient...");
                    System.out.print("Enter admission ID: ");
                    int admissionId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter discharge date (YYYY-MM-DD): ");
                    String dischargeDate = scanner.nextLine();

                    roomNumber = admission.getRoomNumberForAdmission(admissionId);

                    if (roomNumber == -1) {
                        System.out.println("Invalid admission ID. Discharge cannot proceed.");
                        break;
                    }
                    // authorization needed
                    System.out.print("Enter admin ID to proceed with discharge: ");
                    adminId = scanner.nextInt();
                    if (!admission.isAuthorizedAdmin(adminId)){
                        System.out.println("You are not authorized to perform this action.");
                    }
                    admission.dischargePatient(admissionId, dischargeDate, roomNumber);
                    break;
                case 3:
                    System.out.println("Assigning a doctor to an admission...");
                    System.out.println("Enter the ID number of the admission: ");
                    admission_id = Integer.parseInt(scanner.nextLine());
                    System.out.println("Enter the ID number of the patient: ");
                    int patient_id = Integer.parseInt(scanner.nextLine());
                    System.out.println("AUTHORIZATION NEEDED - Enter the ID number of the primary doctor: ");
                    int authorized_id = Integer.parseInt(scanner.nextLine());
                    scanner.nextLine();
                    if (admission.isAuthorizedDoctor(admission_id,authorized_id)){
                        System.out.println("Authorization successful.");
                        System.out.println("Enter the ID number of the doctor you wish to assign: ");
                        int assignedDoc_id = Integer.parseInt(scanner.nextLine());
                        admission.assignDoctor(patient_id,assignedDoc_id,admission_id,false);
                    }
                    else {
                        System.out.println("You are not authorized to assign doctors to this admission.");
                    }
                    break;
                case 4:
                    System.out.println("Viewing patients discharged in a specific date range...");
                    System.out.println("Enter start date YYYY-MM-DD: ");
                    String startDate = scanner.nextLine();
                    System.out.println("Enter end date YYYY-MM-DD: ");
                    String endDate = scanner.nextLine();
                    scanner.nextLine();
                    List<String> dischargedPatients = admission.getPatientsDischargedInDateRange(startDate,endDate);
                    for (String item: dischargedPatients) {
                        System.out.println(item);
                    }
                    break;
                case 5:
                    System.out.println("Viewing details about a specific admission...");
                    System.out.println("Enter the ID number of the admission: ");
                    admission_id = Integer.parseInt(scanner.nextLine());
                    List<String> info = admission.getAdmissionInfo(admission_id);
                    for (String item: info){
                        System.out.println(item);
                    }
                    break;
                case 6:
                    System.out.println("Viewing all currently admitted patients...");
                    List<String> allPatients = admission.getAllCurrentlyAdmittedPatients();
                    for (String item: allPatients) {
                        System.out.println(item);
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private static void manageRooms(Scanner scanner, Room room) {
        while (true) {
            System.out.println("\nROOMS MENU");
            System.out.println("[1] Check if room is available");
            System.out.println("[2] Update room status");
            System.out.println("[3] View all occupied rooms");
            System.out.println("[4] View all available rooms");
            System.out.println("[5] View all rooms");
            System.out.println("[0] Return to main menu");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            int room_id;
            List<String> results;
            switch (choice) {
                case 1:
                    System.out.println("Checking room availability...");
                    System.out.println("Enter room number");
                    room_id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println(room.isRoomAvailable(room_id));
                    break;
                case 2:
                    System.out.println("Updating room status...");
                    System.out.println("Enter room number");
                    room_id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println(room.isRoomAvailable(room_id));
                    if (room.isRoomAvailable(room_id)) {
                        System.out.println("Room "+room_id+" now occupied");
                        room.markRoomOccupied(room_id);
                    }
                    else {
                        System.out.println("Room "+room_id+" now available");
                        room.markRoomAvailable(room_id);
                    }
                    break;
                case 3:
                    System.out.println("Viewing all occupied rooms...");
                    results = room.listOccupiedRooms();
                    for (String result: results){
                        System.out.println(result);
                    }
                    break;
                case 4:
                    System.out.println("Viewing all available rooms...");
                    List<Integer> rooms = room.listUnoccupiedRooms();
                    for (Integer result: rooms){
                        System.out.println(result);
                    }
                    break;
                case 5:
                    System.out.println("Viewing all rooms...");
                    results = room.listAllRooms();
                    for (String result: results){
                        System.out.println(result);
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private static void manageDiagnoses(Scanner scanner, Diagnosis diagnosis) {
        while (true) {
            System.out.println("\nDIAGNOSES MENU");
            System.out.println("[1] Create a new diagnosis");
            System.out.println("[2] View all diagnoses given to patients");
            System.out.println("[0] Return to main menu");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter the name of the new diagnosis:");
                    String diagnosisName = scanner.nextLine();
                    int diagnosisId = diagnosis.createDiagnosis(diagnosisName);
                    if (diagnosisId != -1) {
                        System.out.println("New diagnosis created with ID: " + diagnosisId);
                    } else {
                        System.out.println("Failed to create diagnosis.");
                    }
                    break;
                case 2:
                    System.out.println("Viewing all diagnoses given to patients:");
                    List<String> diagnoses = diagnosis.getDiagnosisDescending();
                    for (String item: diagnoses) {
                        System.out.println(item);
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private static void manageDoctors(Scanner scanner, Doctor doctor) {
        while (true) {
            System.out.println("\nDOCTORS MENU");
            System.out.println("[1] View all doctors");
            System.out.println("[2] View primary doctors with high admissions");
            System.out.println("[0] Return to main menu");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Viewing all doctors:");
                    List<String> allDoctors = doctor.getAllDoctors();
                    for (String item: allDoctors) {
                        System.out.println(item);
                    }
                    break;
                case 2:
                    System.out.println("Viewing primary doctors with high admissions:");
                    Doctor assignedDoctors = new Doctor();
                    List<String> highAdmissions = assignedDoctors.getPrimaryDoctorsWithHighAdmissions();
                    for (String item: highAdmissions) {
                        System.out.println(item);
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private static void manageTreatments(Scanner scanner, Treatment treatment) {
        while (true) {
            System.out.println("\nTREATMENTS MENU");
            System.out.println("[1] View all treatments given to patients");
            System.out.println("[2] Update treatment administration");
            System.out.println("[0] Return to main menu");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Viewing all treatments given to patients:");
                    List<String> treatments = treatment.getAllTreatments();
                    for (String item: treatments) {
                        System.out.println(item);
                    }
                    break;
                case 2:
                    System.out.println("Enter the treatment ID to update:");
                    int treatmentId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter the new administration time (YYYY-MM-DD HH:MM:SS):");
                    String adminTime = scanner.nextLine();
                    boolean success = treatment.updateTreatmentAdministration(treatmentId, adminTime);
                    if (success) {
                        System.out.println("Treatment administration updated successfully.");
                    } else {
                        System.out.println("Failed to update treatment administration.");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private static void manageEmployees(Scanner scanner, Employee emp) {
        while (true) {
            System.out.println("\nEMPLOYEES MENU");
            System.out.println("[1] Add a new employee");
            System.out.println("[2] View all employees");
            System.out.println("[0] Return to main menu");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter employee name:");
                    String name = scanner.nextLine();
                    System.out.println("Enter date of birth (YYYY-MM-DD):");
                    String dob = scanner.nextLine();
                    System.out.println("Enter start date (YYYY-MM-DD):");
                    String startDate = scanner.nextLine();
                    System.out.println("Enter employee role (doctor, nurse, technician, administrator):");
                    String role = scanner.nextLine().toLowerCase();
                    String specialtyOrCertification = null;

                    if (!role.equals("administrator")) {
                        System.out.println("Enter " + (role.equals("doctor") ? "specialty" : "certification/skill") + ":");
                        specialtyOrCertification = scanner.nextLine();
                    }

                    int employeeId = emp.addEmployee(name, dob, startDate, role, specialtyOrCertification);
                    if (employeeId != -1) {
                        System.out.println("Employee added with ID: " + employeeId);
                    } else {
                        System.out.println("Failed to add employee.");
                    }
                    break;
                case 2:
                    System.out.println("Viewing all employees:");
                    List<String> allEmployees = emp.getAllWorkers();
                    for (String item: allEmployees) {
                        System.out.println(item);
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }




    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Admission admission = new Admission();
        Treatment treatment = new Treatment();
        Patient patient = new Patient();
        Diagnosis diagnosis = new Diagnosis();
        Room room = new Room();
        Doctor doctor = new Doctor();
        Employee emp = new Employee();

        boolean programRunning = true;

        while (programRunning) {
            System.out.println("\nWelcome to the hospital management system. What would you like to do?");
            System.out.println("[1] Manage Patients");
            System.out.println("[2] Manage Admissions");
            System.out.println("[3] Manage Rooms");
            System.out.println("[4] Manage Diagnoses");
            System.out.println("[5] Manage Doctors");
            System.out.println("[6] Manage Treatments");
            System.out.println("[7] Manage Employees");
            System.out.println("[0] Exit");

            System.out.print("Enter your choice: ");
            int categoryChoice = scanner.nextInt();
            scanner.nextLine();
            switch (categoryChoice) {
                case 1:
                    managePatients(scanner, patient);
                    break;
                case 2:
                    manageAdmissions(scanner, admission);
                    break;
                case 3:
                    manageRooms(scanner, room);
                    break;
                case 4:
                    manageDiagnoses(scanner, diagnosis);
                    break;
                case 5:
                    manageDoctors(scanner, doctor);
                    break;
                case 6:
                    manageTreatments(scanner, treatment);
                    break;
                case 7:
                    manageEmployees(scanner, emp);
                    break;
                case 0:
                    System.out.println("Exiting the system. Goodbye!");
                    programRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();



//        LiSystem.out.println(("    [st<String> rooms = room.listAllRooms();
//        System.out.println(rooms);
//        //List<String> allPatients = patient.getAllPatients();
//        List<String> allPatients = patient.getAllCurrentlyAdmittedPatients();
//        System.out.println(allPatients);
//
//        List<String> allDiagnosis = diagnosis.getDiagnosisAscending();
//        System.out.println(allDiagnosis);
//
//        List<String> highAdmissionRates = doctor.getPrimaryDoctorsWithHighAdmissions();
//        System.out.println(highAdmissionRates);

//        List<String> allWorkers = emp.getAllWorkers();
//        System.out.println(allWorkers);
//
//        Employee employee = new Employee();
//
//        // Add a doctor
//        int doctorId = employee.addEmployee("Dr. Alice Johnson", "1975-05-12", "2005-07-01", "doctor", "Cardiology");
//        System.out.println("Doctor added with ID: " + doctorId);
//
//        // Add a nurse
//        int nurseId = employee.addEmployee("Nurse Jane White", "1985-06-17", "2012-04-20", "nurse", "Critical Care");
//        System.out.println("Nurse added with ID: " + nurseId);
//
//        // Add an administrator
//        int adminId = employee.addEmployee("Admin Mark Wiser", "1988-12-28", "2008-04-11", "administrator", null);
//        System.out.println("Administrator added with ID: " + adminId);
//
//        allWorkers = emp.getAllWorkers();
//        System.out.println(allWorkers);


//        while (true) {
//            System.out.println("1. List all patients");
//            int choice = scanner.nextInt();
//            scanner.nextLine();
//            switch (choice) {
//                case 1:



    }
}
