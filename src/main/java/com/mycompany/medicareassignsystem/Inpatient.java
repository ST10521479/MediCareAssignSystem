
package com.mycompany.medicareassignsystem;

// it extends the patient and adds bed number 
public class Inpatient extends Patient {// start 
    
    private int wardNumber;
    private int bedNumber;
    
public Inpatient(String patientId, String firstName, String lastName, int age,
                      String gender, String medicalCondition) {
        
        super(patientId, firstName, lastName, age, gender, medicalCondition, PatientCategory.INPATIENT);
        this.wardNumber = 1;
        this.bedNumber = 0;
    }
    public int getWardNumber() { return wardNumber; }
    public int getBedNumber() { return bedNumber; }

    public void setBedNumber(int bedNumber) { this.bedNumber = bedNumber; }

    public boolean hasBed() {
    return bedNumber != 0;
    }
    
    @Override
    public void displayDetails() {
        super.displayDetails();
        String bedInfo = hasBed() ? ("B" + bedNumber) : "None";
        System.out.printf("    -> Ward: %d | Bed: %s%n", wardNumber, bedInfo);
    }
}// end 