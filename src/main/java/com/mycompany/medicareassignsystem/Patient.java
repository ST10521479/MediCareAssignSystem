
package com.mycompany.medicareassignsystem;


public class Patient implements Comparable<Patient> {// start
    // what we need from the user to register them at the hospital
    
    private String patientId;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String medCondition;
    private PatientCategory category;
    
public Patient(String patientId, String firstName, String lastName, int age,
        String gender, String medicalCondition, PatientCategory category) {
    
    this.patientId = patientId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
    this.gender = gender;
    this.medCondition = medicalCondition;
    this.category = category;
    
}
    // Getters
    public String getPatientId() { return patientId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getMedCondition() { return medCondition; }
    public PatientCategory getCategory() { return category; }

// Setters 
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setMedicalCondition(String medicalCondition) { this.medCondition = medicalCondition; }
    public void displayDetails() {
        System.out.printf(
            "Patient ID: %-6s | Name: %-20s | Age: %-3d | Gender: %-6s | Condition: %-18s | Category: %s%n",
            patientId, (firstName + " " + lastName), age, gender, medCondition, category
        );
    }


    
    @Override
    public int compareTo(Patient other) {
        int lastNameCompare = this.lastName.compareToIgnoreCase(other.lastName);
        if (lastNameCompare != 0) {
            return lastNameCompare;
        }
        return this.firstName.compareToIgnoreCase(other.firstName);
    }

    @Override
    public String toString() {
        return patientId + " - " + firstName + " " + lastName;
    }
}// end 
    
