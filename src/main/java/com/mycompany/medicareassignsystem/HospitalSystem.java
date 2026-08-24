
package com.mycompany.medicareassignsystem;
import java.util.ArrayList;
import java.util.Collections;


public class HospitalSystem {// start 
    private static final int TOTAL_BEDS = 20;
    private static final int BEDS_PER_ROW = 5;
    
    private ArrayList<Patient> patients;
    private Bed[] beds;
    
    
    public HospitalSystem() {
        patients = new ArrayList<>();
        beds = new Bed[TOTAL_BEDS];
        for (int i = 0; i<TOTAL_BEDS; i++) {
            beds[i] = new Bed(i +1);
        }
    }
 
   // FEATURE 1: patient management
  
    // for registering a patient that already exists 
    public void registerPatient(Patient patient) throws DuplicatePatientIdException {
        if (findPatient(patient.getPatientId())!= null ) {
            throw new DuplicatePatientIdException(
            "A patient with ID" + patient.getPatientId() + "already exists ");
        
    }
        patients.add(patient);
    }
    public Patient findPatient(String patientId) {
        for(Patient p : patients) {
            if (p.getPatientId().equalsIgnoreCase(patientId)) {
                return p;
            }
        }
        return null;
    }
    public Patient searchPatient(String patientId ) throws PatientNotFoundException {
        Patient p = findPatient(patientId);
        if (p == null) {
            throw new PatientNotFoundException("No patient found with ID " + patientId);
        }
        return p;
    }

  
    public void updatePatient(String patientId, String firstName, String lastName, int age,
                               String gender, String medCondition) throws PatientNotFoundException {
        Patient p = searchPatient(patientId);
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setAge(age);
        p.setGender(gender);
        p.setMedicalCondition(medCondition);
    }

    public void deletePatient(String patientId) throws PatientNotFoundException {
        Patient p = searchPatient(patientId);
        if (p instanceof Inpatient) {
            Inpatient inpatient = (Inpatient) p;
            if (inpatient.hasBed()) {
                Bed bed = beds[inpatient.getBedNumber() - 1];
                bed.free();
                inpatient.setBedNumber(0);
            }
        }
        patients.remove(p);
    }

    public ArrayList<Patient> getAllPatients() {
        return patients;
    }

    public ArrayList<Patient> getPatientsSortedBySurname() {
        ArrayList<Patient> sorted = new ArrayList<>(patients);
        Collections.sort(sorted); 
        return sorted;
    }

  
    public ArrayList<Patient> getPatientsSortedById() {
        ArrayList<Patient> sorted = new ArrayList<>(patients);
        sorted.sort((a, b) -> a.getPatientId().compareToIgnoreCase(b.getPatientId()));
        return sorted;
    }

    
    // FEATURE 2: BED MANAGEMENT 
    

    public Bed[] getAllBeds() {
        return beds;
    }
    
    public void allocateBed(String patientId) throws PatientNotFoundException, BedUnavailableException {
        Patient p = searchPatient(patientId);
        if (!(p instanceof Inpatient)) {
            throw new BedUnavailableException(
                "Only inpatients may be allocated a bed. Patient " + patientId + " is " + p.getCategory() + ".");
        }
        Inpatient inpatient = (Inpatient) p;
        if (inpatient.hasBed()) {
            throw new BedUnavailableException("Patient " + patientId + " already has a bed assigned (B" + inpatient.getBedNumber() + ").");
        }

        Bed freeBed = null;
        for (Bed b : beds) {
            if (!b.isOccupied()) {
                freeBed = b;
                break;
            }
        }
        if (freeBed == null) {
            throw new BedUnavailableException("No beds available. The ward is full (20/20 occupied).");
        }

        freeBed.occupy(patientId);
        inpatient.setBedNumber(freeBed.getBedNumber());
    }

    public void releaseBed(String patientId) throws PatientNotFoundException, BedUnavailableException {
        Patient p = searchPatient(patientId);
        if (!(p instanceof Inpatient)) {
            throw new BedUnavailableException("Patient " + patientId + " is not an inpatient and has no bed.");
        }
        Inpatient inpatient = (Inpatient) p;
        if (!inpatient.hasBed()) {
            throw new BedUnavailableException("Patient " + patientId + " does not currently have a bed assigned.");
        }
        beds[inpatient.getBedNumber() - 1].free();
        inpatient.setBedNumber(0);
    }

 
    public String getWardLayout() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== WARD LAYOUT =====\n");
        for (int i = 0; i < TOTAL_BEDS; i++) {
            sb.append(String.format("%-16s", beds[i].toString()));
            if ((i + 1) % BEDS_PER_ROW == 0) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public ArrayList<Bed> getAvailableBeds() {
        ArrayList<Bed> available = new ArrayList<>();
        for (Bed b : beds) {
            if (!b.isOccupied()) available.add(b);
        }
        return available;
    }

    public ArrayList<Bed> getOccupiedBeds() {
        ArrayList<Bed> occupied = new ArrayList<>();
        for (Bed b : beds) {
            if (b.isOccupied()) occupied.add(b);
        }
        return occupied;
    }

    public int countAvailableBeds() {
        return getAvailableBeds().size();
    }

    public int countOccupiedBeds() {
        return getOccupiedBeds().size();
    }

 
    // FEATURE 3: REPORTS
  

    public double getWardOccupancyPercentage() {
        return (countOccupiedBeds() / (double) TOTAL_BEDS) * 100.0;
    }

    public String generateSummaryReport() {
        // declaring the stringbuilder 
        StringBuilder sb = new StringBuilder();
        sb.append("========== WARD REPORT ==========\n");
        sb.append("Total registered patients : ").append(patients.size()).append("\n");
        sb.append("Total occupied beds        : ").append(countOccupiedBeds()).append("\n");
        sb.append("Total available beds       : ").append(countAvailableBeds()).append("\n");
        sb.append(String.format("Ward occupancy percentage  : %.1f%%%n", getWardOccupancyPercentage()));
        sb.append("==================================\n");
        return sb.toString();
    }
}// end
    
    

