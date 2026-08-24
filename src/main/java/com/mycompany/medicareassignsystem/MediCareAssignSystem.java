

package com.mycompany.medicareassignsystem;
import java.util.ArrayList;
import java.util.Scanner;


public class MediCareAssignSystem {// start of class
    private static Scanner scanner = new Scanner(System.in);
    private static HospitalSystem system = new HospitalSystem();

    public static void main(String[] args) {// start of method
        
        boolean running = true;
        while (running){
            printMenu();
            // what the menu will have 
            int choice = readInt("Enter your choice: ");
            try{
                switch (choice) {
                    case 1: registerPatient();
                    break;
                    case 2: searchPatient();
                    break;
                    case 3: updatePatient();
                    break;
                    case 4: deletePatient();
                    break;
                    case 5: displayAllPatients();
                    break;
                    case 6: allocateBed();
                    break;
                    case 7: releaseBed();
                    break;
                    case 8: System.out.println(system.getWardLayout());
                    break;
                    case 9: displayBedList(system.getAvailableBeds(),"Available Beds");
                    break;
                    case 10: displayBedList(system.getOccupiedBeds(), "Occupied Beds");
                    break;
                    case 11: System.out.println(system.generateSummaryReport());
                    break;
                    case 12: displaySortedPatients();
                    break;
                    case 0:
                        running = false;
                        System.out.println("Existing system. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. please try again");
                }
            } catch (DuplicatePatientIdException | PatientNotFoundException | BedUnavailableException e) {
                
                System.out.println("Error: " + e.getMessage());
                       
                    
                    
                               
                }
            }
        scanner.close();
        
        }// end of method 
      // what the user will see on their screen 
         private static void printMenu() {
            System.out.println("\n===== MEDICARE HOSPITAL - PATIENT ADMISSION SYSTEM =====");
            System.out.println(" 1. Register a new patient");
            System.out.println(" 2. Search for a patient");
            System.out.println(" 3. Update patient details");
            System.out.println(" 4. Delete a patient");
            System.out.println(" 5. Display all registered patients");
            System.out.println(" 6. Allocate a bed to an inpatient");
            System.out.println(" 7. Release a bed (discharge)");
            System.out.println(" 8. Display complete ward layout");
            System.out.println(" 9. Display available beds");
            System.out.println("10. Display occupied beds");
            System.out.println("11. Generate ward report");
            System.out.println("12. Display patients sorted (surname / ID)");
            System.out.println(" 0. Exit");
    }
         // patient management
         private static void registerPatient() throws DuplicatePatientIdException {
        System.out.println("\n-- Register New Patient --");
        String id = readString("Patient ID: ");
        String firstName = readString("First name: ");
        String lastName = readString("Last name: ");
        int age = readInt("Age: ");
        String gender = readString("Gender: ");
        String condition = readString("Medical condition: ");
        PatientCategory category = readCategory();

        Patient patient;
        if (category == PatientCategory.INPATIENT) {
            patient = new Inpatient(id, firstName, lastName, age, gender, condition);
        } else {
            patient = new Patient(id, firstName, lastName, age, gender, condition, category);
        }

        system.registerPatient(patient);
        System.out.println("Patient " + id + " registered successfully as " + category + ".");
        if (category == PatientCategory.INPATIENT) {
            System.out.println("(No bed assigned yet - use option 6 to allocate a bed.)");
        }
    }

    private static void searchPatient() throws PatientNotFoundException {
        System.out.println("\n-- Search Patient --");
        String id = readString("Enter Patient ID: ");
        Patient p = system.searchPatient(id);
        p.displayDetails();
    }

    private static void updatePatient() throws PatientNotFoundException {
        System.out.println("\n-- Update Patient --");
        String id = readString("Enter Patient ID to update: ");
        Patient existing = system.searchPatient(id); // throws if missing
        System.out.println("Current details:");
        existing.displayDetails();

        System.out.println("Enter new details below:");
        String firstName = readString("First name: ");
        String lastName = readString("Last name: ");
        int age = readInt("Age: ");
        String gender = readString("Gender: ");
        String condition = readString("Medical condition: ");

        system.updatePatient(id, firstName, lastName, age, gender, condition);
        System.out.println("Patient " + id + " updated successfully.");
    }

    private static void deletePatient() throws PatientNotFoundException {
        System.out.println("\n-- Delete Patient --");
        String id = readString("Enter Patient ID to delete: ");
        system.deletePatient(id);
        System.out.println("Patient " + id + " deleted (any assigned bed has been released).");
    }

    private static void displayAllPatients() {
        System.out.println("\n-- All Registered Patients --");
        ArrayList<Patient> all = system.getAllPatients();
        if (all.isEmpty()) {
            System.out.println("No patients registered yet.");
            return;
        }
        for (Patient p : all) {
            p.displayDetails();
        }
    }

    private static void displaySortedPatients() {
        System.out.println("\n1. Sort by surname");
        System.out.println("2. Sort by Patient ID");
        int choice = readInt("Choose an option: ");
        ArrayList<Patient> sorted = (choice == 2)
            ? system.getPatientsSortedById()
            : system.getPatientsSortedBySurname();

        if (sorted.isEmpty()) {
            System.out.println("No patients registered yet.");
            return;
        }
        for (Patient p : sorted) {
            p.displayDetails();
        }
    }
    // bed management 
    private static void allocateBed() throws PatientNotFoundException, BedUnavailableException {
        System.out.println("\n-- Allocate Bed --");
        String id = readString("Enter Patient ID: ");
        system.allocateBed(id);
        Patient p = system.findPatient(id);
        Inpatient inpatient = (Inpatient) p;
        System.out.println("Bed B" + inpatient.getBedNumber() + " allocated to patient " + id);
    }

    private static void releaseBed() throws PatientNotFoundException, BedUnavailableException {
        System.out.println("\n-- Release Bed (Discharge) --");
        String id = readString("Enter Patient ID: ");
        system.releaseBed(id);
        System.out.println("Bed released for patient " + id);
    }

    private static void displayBedList(ArrayList<Bed> beds, String title) {
        System.out.println("\n-- " + title + " (" + beds.size() + ") --");
        if (beds.isEmpty()) {
            System.out.println("None.");
            return;
        }
        for (Bed b : beds) {
            System.out.println(b);
        }
    }
        // input helpers
    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static PatientCategory readCategory() {
        while (true) {
            String input = readString("Category (Inpatient / Outpatient / Emergency): ").trim();
            try {
                return PatientCategory.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter exactly one of: Inpatient, Outpatient, Emergency.");
            }
        }
    }
    
        
    }//end of class 

