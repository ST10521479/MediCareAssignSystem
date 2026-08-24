package com.mycompany.medicareassignsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class HospitalSystemTest {// start of test class 

    private HospitalSystem system;

    @BeforeEach
    void setUp() {
        system = new HospitalSystem();
    }

    // ---------- Patient management ----------

    @Test
    void testRegisterPatient() throws Exception {
        Patient p = new Patient("P001", "John", "Doe", 34, "Male", "Flu", PatientCategory.OUTPATIENT);
        system.registerPatient(p);
        assertEquals(1, system.getAllPatients().size());
        assertEquals("P001", system.getAllPatients().get(0).getPatientId());
    }

    @Test
    void testSearchPatient() throws Exception {
        Patient p = new Patient("P001", "John", "Doe", 34, "Male", "Flu", PatientCategory.OUTPATIENT);
        system.registerPatient(p);
        Patient found = system.searchPatient("P001");
        assertEquals("Doe", found.getLastName());
    }

    @Test
    void testSearchPatientNotFoundThrows() {
        assertThrows(PatientNotFoundException.class, () -> system.searchPatient("NOPE"));
    }

    @Test
    void testUpdatePatientDetails() throws Exception {
        Patient p = new Patient("P001", "John", "Doe", 34, "Male", "Flu", PatientCategory.OUTPATIENT);
        system.registerPatient(p);
        system.updatePatient("P001", "Jonathan", "Doe", 35, "Male", "Recovered");
        Patient updated = system.searchPatient("P001");
        assertEquals("Jonathan", updated.getFirstName());
        assertEquals(35, updated.getAge());
        assertEquals("Recovered", updated.getMedCondition());
    }

    @Test
    void testDeletePatient() throws Exception {
        Patient p = new Patient("P001", "John", "Doe", 34, "Male", "Flu", PatientCategory.OUTPATIENT);
        system.registerPatient(p);
        system.deletePatient("P001");
        assertTrue(system.getAllPatients().isEmpty());
        assertThrows(PatientNotFoundException.class, () -> system.searchPatient("P001"));
    }

    @Test
    void testPreventDuplicatePatientIds() throws Exception {
        Patient p1 = new Patient("P001", "John", "Doe", 34, "Male", "Flu", PatientCategory.OUTPATIENT);
        Patient p2 = new Patient("P001", "Jane", "Smith", 40, "Female", "Cold", PatientCategory.EMERGENCY);
        system.registerPatient(p1);
        assertThrows(DuplicatePatientIdException.class, () -> system.registerPatient(p2));
        assertEquals(1, system.getAllPatients().size()); // second registration must not have been added
    }

    @Test
    void testSortPatientsBySurname() throws Exception {
        system.registerPatient(new Patient("P001", "Zoe", "Zulu", 20, "F", "Flu", PatientCategory.OUTPATIENT));
        system.registerPatient(new Patient("P002", "Amy", "Adams", 25, "F", "Cold", PatientCategory.OUTPATIENT));
        system.registerPatient(new Patient("P003", "Mike", "Nash", 30, "M", "Cough", PatientCategory.OUTPATIENT));

        ArrayList<Patient> sorted = system.getPatientsSortedBySurname();
        assertEquals("Adams", sorted.get(0).getLastName());
        assertEquals("Nash", sorted.get(1).getLastName());
        assertEquals("Zulu", sorted.get(2).getLastName());
    }

    @Test
    void testSortPatientsById() throws Exception {
        system.registerPatient(new Patient("P003", "Mike", "Nash", 30, "M", "Cough", PatientCategory.OUTPATIENT));
        system.registerPatient(new Patient("P001", "Zoe", "Zulu", 20, "F", "Flu", PatientCategory.OUTPATIENT));
        system.registerPatient(new Patient("P002", "Amy", "Adams", 25, "F", "Cold", PatientCategory.OUTPATIENT));

        ArrayList<Patient> sorted = system.getPatientsSortedById();
        assertEquals("P001", sorted.get(0).getPatientId());
        assertEquals("P002", sorted.get(1).getPatientId());
        assertEquals("P003", sorted.get(2).getPatientId());
    }

    // ---------- Bed management ----------

    @Test
    void testAllocateBed() throws Exception {
        system.registerPatient(new Inpatient("P001", "John", "Doe", 34, "Male", "Surgery"));
        system.allocateBed("P001");
        Inpatient p = (Inpatient) system.findPatient("P001");
        assertTrue(p.hasBed());
        assertEquals(1, system.countOccupiedBeds());
    }

    @Test
    void testReleaseBed() throws Exception {
        system.registerPatient(new Inpatient("P001", "John", "Doe", 34, "Male", "Surgery"));
        system.allocateBed("P001");
        system.releaseBed("P001");
        Inpatient p = (Inpatient) system.findPatient("P001");
        assertFalse(p.hasBed());
        assertEquals(0, system.countOccupiedBeds());
    }

    @Test
    void testPreventAllocatingBedToNonInpatient() throws Exception {
        system.registerPatient(new Patient("P001", "Amy", "Adams", 25, "F", "Cold", PatientCategory.OUTPATIENT));
        assertThrows(BedUnavailableException.class, () -> system.allocateBed("P001"));
    }

    @Test
    void testPreventAllocatingSecondBedToSamePatient() throws Exception {
        system.registerPatient(new Inpatient("P001", "John", "Doe", 34, "Male", "Surgery"));
        system.allocateBed("P001");
        // Patient already has a bed so a second allocation attempt must be rejected
        assertThrows(BedUnavailableException.class, () -> system.allocateBed("P001"));
    }

    @Test
    void testPreventBedAllocationWhenWardFull() throws Exception {
        // Fill all 20 beds with 20 different inpatients
        for (int i = 1; i <= 20; i++) {
            String id = "P" + String.format("%03d", i);
            system.registerPatient(new Inpatient(id, "First" + i, "Last" + i, 30, "M", "Condition"));
            system.allocateBed(id);
        }
        assertEquals(20, system.countOccupiedBeds());
        assertEquals(0, system.countAvailableBeds());

        // The 21st inpatient should not be able to get a bed
        system.registerPatient(new Inpatient("P021", "Extra", "Patient", 30, "M", "Condition"));
        assertThrows(BedUnavailableException.class, () -> system.allocateBed("P021"));
    }

    @Test
    void testNoTwoPatientsShareTheSameBed() throws Exception {
        system.registerPatient(new Inpatient("P001", "A", "A", 30, "M", "X"));
        system.registerPatient(new Inpatient("P002", "B", "B", 30, "M", "X"));
        system.allocateBed("P001");
        system.allocateBed("P002");

        Inpatient p1 = (Inpatient) system.findPatient("P001");
        Inpatient p2 = (Inpatient) system.findPatient("P002");
        assertNotEquals(p1.getBedNumber(), p2.getBedNumber());
    }
}// end 